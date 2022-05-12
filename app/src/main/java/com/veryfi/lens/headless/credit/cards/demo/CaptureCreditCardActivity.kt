package com.veryfi.lens.headless.credit.cards.demo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
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
import com.veryfi.lens.headless.creditcards.VeryfiLensHeadless
import com.veryfi.lens.headless.creditcards.VeryfiLensHeadlessCredentials
import com.veryfi.lens.headless.creditcards.VeryfiLensHeadlessDelegate
import com.veryfi.lens.headless.creditcards.VeryfiLensHeadlessSettings
import com.veryfi.lens.headless.creditcards.helpers.Frame
import com.veryfi.lens.headless.creditcards.helpers.ScreenUtils
import org.json.JSONObject
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule

class CaptureCreditCardActivity : AppCompatActivity() {

    private var autoTorchMenuItem: MenuItem? = null
    private var isTorchOn: Boolean = false
    private var autoTorch: Boolean = false
    private var camera: Camera? = null
    private var flipExtractionRequired: Boolean = false
    private var cardData: CardData = CardData()
    private var cameraProvider: ProcessCameraProvider? = null
    private lateinit var viewBinding: ActivityCaptureCreditCardBinding
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCaptureCreditCardBinding.inflate(layoutInflater)
        val veryfiLensHeadlessCredentials = VeryfiLensHeadlessCredentials()
        val veryfiLensHeadlessSetting = VeryfiLensHeadlessSettings()
        veryfiLensHeadlessCredentials.apiKey = Application.AUTH_API_KEY
        veryfiLensHeadlessCredentials.username = Application.AUTH_USERNAME
        veryfiLensHeadlessCredentials.clientId = Application.CLIENT_ID
        VeryfiLensHeadless.configure(
            this.application,
            veryfiLensHeadlessCredentials,
            veryfiLensHeadlessSetting
        ) {
        }
        setContentView(viewBinding.root)
        setSupportActionBar(viewBinding.toolbar)
        setUpVeryfiLensDelegate()
        checkPermissions()
        cameraExecutor = Executors.newSingleThreadExecutor()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        viewBinding.toolbar.setNavigationIcon(R.drawable.ic_vector_back)
        viewBinding.toolbar.setNavigationOnClickListener {
            finish()
        }
        viewBinding.contentCameraProcessing.enterDetails.setOnClickListener {
            browseToCardDetails()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_auto_torch, menu)
        autoTorchMenuItem = menu?.findItem(R.id.menu_auto_torch_item)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_auto_torch_item) {
            if (autoTorch) {
                autoTorch = false
                isTorchOn = false
                item.setIcon(R.drawable.flash_auto)
                if (camera?.cameraInfo?.hasFlashUnit() == true)
                    camera?.cameraControl?.enableTorch(false)
            } else {
                autoTorch = true
                item.setIcon(R.drawable.flash_off)
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
        viewBinding.contentCardData.cardNumber.setText(cardData.cardNumber)
        viewBinding.contentCardData.cardDate.setText(cardData.cardExpDate)
        viewBinding.contentCardData.cardName.setText(cardData.cardName)
        viewBinding.contentCardData.cardType.setText(cardData.cardType)
        viewBinding.contentCardData.cardCVC.setText(cardData.cardCvc)
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            viewBinding.contentCameraProcessing.scanSubtitle.setTextColor(getColor(R.color.black))
        }
        viewBinding.contentCameraProcessing.flipImage.visibility = View.GONE
        cardData = CardData() // clean data
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewBinding.cameraPreview.surfaceProvider)
                }
            val screenAspectRatio = aspectRatio()
            val imageAnalyzer = ImageAnalysis.Builder().setTargetAspectRatio(screenAspectRatio)
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
                cameraProvider?.unbindAll()
                val useCaseGroup = UseCaseGroup.Builder()
                    .addUseCase(preview)
                    .addUseCase(imageAnalyzer)
                    .build()
                camera = cameraProvider?.bindToLifecycle(
                    this,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    useCaseGroup
                )
                startAutoFocusing()
                startFocusOnClick()

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
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
                            //focus only when the user tap the preview
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                viewBinding.contentCameraProcessing.scanSubtitle.setTextColor(getColor(R.color.green))
            }
            viewBinding.contentCameraProcessing.flipImage.visibility = View.VISIBLE
            Timer("SettingUp", false).schedule(4000) {
                VeryfiLensHeadless.reset()
            }
        }
    }

    private fun requiredCardDataCompleted(): Boolean {
        cardData.apply {
            return cardNumber.isNotEmpty()
                    && cardName.isNotEmpty()
                    && cardExpDate.isNotEmpty()
                    && cardType.isNotEmpty()
                    && cardCvc.isNotEmpty()
        }
    }

    private fun updatePlaceHolders(data: JSONObject) {
        val cardData = getCardData(data)
        viewBinding.contentCameraProcessing.creditCardData.visibility = View.VISIBLE
        viewBinding.contentCameraProcessing.cardNumber.text = cardData.cardNumber
        viewBinding.contentCameraProcessing.cardDate.text = cardData.cardExpDate
        viewBinding.contentCameraProcessing.cardName.text = cardData.cardName
        viewBinding.contentCameraProcessing.cardCVC.text = cardData.cardCvc
    }

    private fun getCardData(data: JSONObject): CardData {
        return CardData(
            data.getString("card_number"),
            data.getJSONArray("card_dates")[0].toString(),
            data.getString("card_name"),
            data.getString("card_type"),
            data.getString("card_cvc")
        )
    }

    private fun updateCardData(data: JSONObject) {
        if (cardData.cardNumber.isEmpty()) {
            cardData.cardNumber = data.getString("card_number")
        }
        if (cardData.cardExpDate.isEmpty()) {
            cardData.cardExpDate = data.getJSONArray("card_dates")[0].toString()
        }
        if (cardData.cardName.isEmpty()) {
            cardData.cardName = data.getString("card_name")
        }
        if (cardData.cardType.isEmpty()) {
            cardData.cardType = data.getString("card_type")
        }
        if (cardData.cardCvc.isEmpty()) {
            cardData.cardCvc = data.getString("card_cvc")
        }
    }

    private fun aspectRatio(): Int {
        val width = ScreenUtils.getScreenWidth(this)
        val height = ScreenUtils.getScreenHeight(this)
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
        private const val TAG = "CameraXApp"
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
