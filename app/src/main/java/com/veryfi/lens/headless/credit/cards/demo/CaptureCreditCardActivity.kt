package com.veryfi.lens.headless.credit.cards.demo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.veryfi.lens.headless.credit.cards.demo.databinding.ActivityCaptureCreditCardBinding
import com.veryfi.lens.headless.credit.cards.demo.helpers.ThemeHelper
import com.veryfi.lens.headless.creditcards.VeryfiLensHeadless
import com.veryfi.lens.headless.creditcards.VeryfiLensHeadlessDelegate
import com.veryfi.lens.helpers.*
import org.json.JSONObject
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule


class CaptureCreditCardActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityCaptureCreditCardBinding
    private lateinit var cameraExecutor: ExecutorService
    private var autoTorchMenuItem: MenuItem? = null
    private var isTorchOn: Boolean = false
    private var autoTorch: Boolean = false
    private var camera: Camera? = null
    private var flipExtractionRequired: Boolean = false
    private var cardData: CardData = CardData()
    private var cameraProvider: ProcessCameraProvider? = null
    private var cardHolderNameOn: Boolean = true
    private var cardDateOn: Boolean = true
    private var cardCvcOn: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCaptureCreditCardBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        cameraExecutor = Executors.newSingleThreadExecutor()
        ThemeHelper.setSecondaryColorToStatusBar(this, this)
        setupHeadless()
        setUpToolBar()
        setUpVeryfiLensDelegate()
        checkPermissions()

        viewBinding.contentCameraProcessing.enterDetails.setOnClickListener {
            browseToCardDetails()
        }
    }

    private fun setupHeadless() {
        val veryfiLensHeadlessCredentials = VeryfiLensCredentials()
        val veryfiLensSettings = VeryfiLensSettings()
        cardHolderNameOn = intent.extras?.getSerializable(CARD_HOLDER_NAME) as Boolean
        cardDateOn = intent.extras?.getSerializable(CARD_DATE) as Boolean
        cardCvcOn = intent.extras?.getSerializable(CARD_CVC) as Boolean
        veryfiLensSettings.creditCardMarginTop = 100
        veryfiLensSettings.creditCardMarginBottom = 50
        veryfiLensSettings.creditCardDetectCardHolderName = cardHolderNameOn
        veryfiLensSettings.creditCardDetectCardDate = cardDateOn
        veryfiLensSettings.creditCardDetectCardCVC = cardCvcOn
        veryfiLensSettings.creditCardAutoCaptureMode =
            VeryfiLensSettings.CreditCardAutoCaptureMode.Normal //Speed vs Accuracy
        veryfiLensSettings.ignoreRemoteSettings = true
        veryfiLensHeadlessCredentials.apiKey = AUTH_API_KEY
        veryfiLensHeadlessCredentials.username = AUTH_USERNAME
        veryfiLensHeadlessCredentials.clientId = CLIENT_ID
        VeryfiLensHeadless.configure(
            application, veryfiLensHeadlessCredentials, veryfiLensSettings
        ) {}
    }

    private fun setUpToolBar() {
        setSupportActionBar(viewBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        viewBinding.toolbar.setNavigationIcon(R.drawable.ic_vector_close_shape)
        viewBinding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_auto_torch, menu)
        autoTorchMenuItem = menu.findItem(R.id.menu_auto_torch_item)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_auto_torch_item) {
            if (autoTorch) {
                autoTorch = false
                isTorchOn = false
                item.setIcon(R.drawable.ic_vector_flash_auto_shape)
                if (camera?.cameraInfo?.hasFlashUnit() == true)
                    camera?.cameraControl?.enableTorch(false)
            } else {
                autoTorch = true
                item.setIcon(R.drawable.ic_vector_flash_off_shape)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun browseToCardDetails() {
        autoTorchMenuItem?.isVisible = false
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        viewBinding.toolbar.setTitle(R.string.details)
        viewBinding.cameraPreview.visibility = View.GONE
        viewBinding.toolbar.setNavigationOnClickListener {
            backToCapture()
            VeryfiLensHeadless.reset()
        }
        viewBinding.contentCameraProcessing.root.visibility = View.GONE
        viewBinding.contentCardData.root.visibility = View.VISIBLE
        viewBinding.contentCardData.cardNumber.text = cardData.cardNumber
        viewBinding.contentCardData.cardDate.text = cardData.cardExpDate
        viewBinding.contentCardData.cardName.text = cardData.cardName
        viewBinding.contentCardData.cardType.text = cardData.cardType
        viewBinding.contentCardData.cardCVC.text = cardData.cardCvc
        viewBinding.contentCardData.processOtherCreditCard.setOnClickListener {
            backToCapture()
            VeryfiLensHeadless.reset()
        }
    }

    private fun backToCapture() {
        //back to initial state
        flipExtractionRequired = false
        autoTorchMenuItem?.isVisible = true
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        viewBinding.toolbar.setNavigationOnClickListener {
            finish()
        }
        viewBinding.toolbar.setTitle(R.string.scan)
        viewBinding.contentCameraProcessing.root.visibility = View.VISIBLE
        viewBinding.contentCardData.root.visibility = View.GONE
        viewBinding.cameraPreview.visibility = View.VISIBLE
        viewBinding.contentCameraProcessing.creditCardData.visibility = View.GONE
        viewBinding.contentCameraProcessing.scanSubtitle.setText(R.string.scan)
        viewBinding.contentCameraProcessing.flipImage.visibility = View.GONE
        cardData = CardData() // clean data
        VeryfiLensHelper.settings.creditCardDetectCardNumber = true
        VeryfiLensHelper.settings.creditCardDetectCardHolderName = cardHolderNameOn
        VeryfiLensHelper.settings.creditCardDetectCardDate = cardDateOn
        VeryfiLensHelper.settings.creditCardDetectCardCVC = cardCvcOn
    }

    private fun startCamera() {
        // Wait for the views to be properly laid out
        viewBinding.cameraPreview.post {
            Executors.newSingleThreadExecutor().execute {
                val cameraProvider = ProcessCameraProvider.getInstance(this).get()
                runOnUiThread {
                    bindCameraUseCases(cameraProvider)
                }
            }
        }
    }

    private fun bindCameraUseCases(cameraProvider: ProcessCameraProvider) {
        val preview = Preview.Builder()
            .build()
            .also {
                it.setSurfaceProvider(viewBinding.cameraPreview.surfaceProvider)
            }
        val screenAspectRatio = aspectRatio()
        val imageAnalyzer = ImageAnalysis.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(Surface.ROTATION_0).build()
        imageAnalyzer.setAnalyzer(
            Executors.newSingleThreadExecutor(),
            CameraAnalyzer { byteArray, width, height ->
                if (autoTorch) {
                    val pixels = byteArray.map { it.toInt() and 0xFF }
                    if (pixels.average() < MIN_LUMEN_FLASH_TRIGGER) {
                        runOnUiThread {
                            if (!isTorchOn) {
                                camera?.cameraControl?.enableTorch(true)
                                isTorchOn = true
                            }
                        }
                    }
                }
                //Integration with Lens SDK
                if (viewBinding.cameraPreview.visibility != View.GONE) {
                    VeryfiLensHeadless.processCreditCard(Frame(byteArray, width, height))
                }
            })
        try {
            cameraProvider.unbindAll()
            val useCaseGroup = UseCaseGroup.Builder()
                .addUseCase(preview)
                .addUseCase(imageAnalyzer)
                .build()
            camera = cameraProvider.bindToLifecycle(
                this,
                CameraSelector.DEFAULT_BACK_CAMERA,
                useCaseGroup
            )
            startAutoFocusing()
            startFocusOnClick()
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }

    private fun startAutoFocusing() {
        val autoFocusPoint = SurfaceOrientedMeteringPointFactory(1f, 1f)
            .createPoint(.2f, .5f)
        try {
            val autoFocusAction = FocusMeteringAction.Builder(
                autoFocusPoint,
                FocusMeteringAction.FLAG_AF
            ).apply {
                setAutoCancelDuration(AUTO_FOCUS_TIME, TimeUnit.SECONDS)
            }.build()
            camera?.cameraControl?.startFocusAndMetering(autoFocusAction)
        } catch (e: CameraInfoUnavailableException) {
            Log.e(TAG, "Can't do auto focus", e)
        }
    }

    private fun startFocusOnClick() {
        viewBinding.cameraPreview.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val factory: MeteringPointFactory = SurfaceOrientedMeteringPointFactory(
                    viewBinding.cameraPreview.width.toFloat(),
                    viewBinding.cameraPreview.height.toFloat()
                )
                val autoFocusPoint = factory.createPoint(event.x, event.y)
                try {
                    camera?.cameraControl?.startFocusAndMetering(
                        FocusMeteringAction.Builder(
                            autoFocusPoint,
                            FocusMeteringAction.FLAG_AF
                        ).apply {
                            disableAutoCancel()
                        }.build()
                    )
                } catch (e: CameraInfoUnavailableException) {
                    Log.e(TAG, "Can't do manual focus", e)
                }
            }
            viewBinding.cameraPreview.performClick()
        }
    }

    private fun setUpVeryfiLensDelegate() {
        VeryfiLensHeadless.setDelegate(object : VeryfiLensHeadlessDelegate {
            override fun veryfiLensClose(json: JSONObject) {
                Toast.makeText(
                    this@CaptureCreditCardActivity,
                    "Veryfi Lens Credit Cards close",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun veryfiLensError(json: JSONObject) {
                Log.d(TAG, json.toString(2))
            }

            override fun veryfiLensSuccess(json: JSONObject) {
                Log.d(TAG, "veryfiLensSuccess")
                Log.d(TAG, json.toString(2))
                finishOrFlip(json)
            }

            override fun veryfiLensUpdate(json: JSONObject) {
                Log.d(TAG, json.toString(2))
                updatePlaceHolders(json)
            }
        })
    }

    private fun finishOrFlip(data: JSONObject) {
        updatePlaceHolders(data)
        updateCardData(data)
        if (flipExtractionRequired || requiredCardDataCompleted()) {
            //we got all we need :) or we do processed both credit card sides
            browseToCardDetails()
        } else {
            //update UI and ask for flip the card to user
            flipExtractionRequired = true
            viewBinding.contentCameraProcessing.scanSubtitle.setText(R.string.flip)
            viewBinding.contentCameraProcessing.flipImage.visibility = View.VISIBLE
            viewBinding.contentCameraProcessing.flipImage.animate().rotationYBy(720f)
            viewBinding.contentCameraProcessing.flipImage.animate().duration = 4000
            viewBinding.contentCameraProcessing.flipImage.animate().start()
            Timer("SettingUp", false).schedule(4000) {
                VeryfiLensHeadless.reset()
                runOnUiThread {
                    viewBinding.contentCameraProcessing.flipImage.visibility = View.GONE
                }
            }
        }
    }

    private fun requiredCardDataCompleted(): Boolean {
        cardData.apply {
            return (cardNumber.isNotEmpty()
                    && cardType.isNotEmpty()
                    && validateCardName()
                    && validateCardDate()
                    && validateCardCode())
        }
    }

    private fun validateCardName(): Boolean {
        cardData.apply {
            return if (cardHolderNameOn) {
                cardName.isNotEmpty()
            } else {
                true
            }
        }
    }

    private fun validateCardDate(): Boolean {
        cardData.apply {
            return if (cardDateOn) {
                cardExpDate.isNotEmpty()
            } else {
                true
            }
        }
    }

    private fun validateCardCode(): Boolean {
        cardData.apply {
            return if (cardCvcOn) {
                cardCvc.isNotEmpty()
            } else {
                true
            }
        }
    }

    private fun updatePlaceHolders(data: JSONObject) {
        val cardData = getCardData(data)
        viewBinding.contentCameraProcessing.creditCardData.visibility = View.VISIBLE
        if (this.cardData.cardNumber.isEmpty()) {
            viewBinding.contentCameraProcessing.cardNumber.text = cardData.cardNumber
        }
        if (this.cardData.cardName.isEmpty()) {
            viewBinding.contentCameraProcessing.cardName.text = cardData.cardName
        }
        if (this.cardData.cardExpDate.isEmpty()) {
            viewBinding.contentCameraProcessing.cardDate.text = cardData.cardExpDate
        }
        if (this.cardData.cardCvc.isEmpty()) {
            viewBinding.contentCameraProcessing.cardCVC.text = cardData.cardCvc
        }
    }

    private fun getCardData(data: JSONObject): CardData {
        return CardData(
            try { data.getString("card_number") } catch(e: Exception) { "" },
            try { data.getJSONArray("card_dates")[0].toString() } catch(e: Exception) { "" },
            try { data.getString("card_name") } catch(e: Exception) { "" },
            try { data.getString("card_type") } catch(e: Exception) { "" },
            try { data.getString("card_cvc") } catch(e: Exception) { "" }
        )
    }

    private fun updateCardData(data: JSONObject) {
        if (cardData.cardNumber.isEmpty()) {
            cardData.cardNumber = try { data.getString("card_number") } catch(e: Exception) { "" }
            VeryfiLensHelper.settings.creditCardDetectCardNumber = cardData.cardNumber.isEmpty()
        }
        if (cardData.cardExpDate.isEmpty()) {
            cardData.cardExpDate =try { data.getJSONArray("card_dates")[0].toString() } catch(e: Exception) { "" }
            VeryfiLensHelper.settings.creditCardDetectCardDate = cardData.cardExpDate.isEmpty()
        }
        if (cardData.cardName.isEmpty()) {
            cardData.cardName = try { data.getString("card_name") } catch(e: Exception) { "" }
            VeryfiLensHelper.settings.creditCardDetectCardHolderName = cardData.cardName.isEmpty()
        }
        if (cardData.cardType.isEmpty()) {
            cardData.cardType = try { data.getString("card_type") } catch(e: Exception) { "" }
        }
        if (cardData.cardCvc.isEmpty()) {
            cardData.cardCvc = try { data.getString("card_cvc") } catch(e: Exception) { "" }
            VeryfiLensHelper.settings.creditCardDetectCardCVC = cardData.cardCvc.isEmpty()
        }
    }

    private fun aspectRatio(): Int {
        val width = ScreenHelper.getScreenWidth(this)
        val height = ScreenHelper.getScreenHeight(this)
        val previewRatio = kotlin.math.max(width, height).toDouble() / kotlin.math.min(
            width,
            height
        )
        if (kotlin.math.abs(previewRatio - RATIO_4_3_VALUE) <= kotlin.math.abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }

    private fun checkPermissions() {
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        backToCapture()
        VeryfiLensHeadless.reset()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        const val CLIENT_ID = BuildConfig.VERYFI_CLIENT_ID
        const val AUTH_USERNAME = BuildConfig.VERYFI_USERNAME
        const val AUTH_API_KEY = BuildConfig.VERYFI_API_KEY

        private const val TAG = "CameraXApp"
        private const val CARD_HOLDER_NAME = "card_holder_name"
        private const val CARD_DATE = "card_date"
        private const val CARD_CVC = "card_cvc"
        private const val MIN_LUMEN_FLASH_TRIGGER = 105
        private const val AUTO_FOCUS_TIME = 3L
        private const val REQUEST_CODE_PERMISSIONS = 10
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA
            ).toTypedArray()
    }
}