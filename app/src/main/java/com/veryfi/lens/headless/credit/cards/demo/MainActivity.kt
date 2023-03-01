package com.veryfi.lens.headless.credit.cards.demo

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.veryfi.lens.headless.credit.cards.demo.databinding.ActivityMainBinding
import com.veryfi.lens.headless.credit.cards.demo.helpers.ThemeHelper

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding
    private var cardHolderNameOn = true
    private var cardDateOn = true
    private var cardCvcOn = true

    override fun onStart() {
        super.onStart()
        initVeryfiLogo()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        ThemeHelper.setBackgroundColorToStatusBar(this, this)
        setUpClickEvents()
    }

    private fun initVeryfiLogo() {
        when (resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                viewBinding.veryfiLogo.setImageResource(R.drawable.ic_veryfi_logo_white)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                viewBinding.veryfiLogo.setImageResource(R.drawable.ic_veryfi_logo_black)
            }
        }
    }

    private fun setUpClickEvents() {
        viewBinding.captureCreditCard.setOnClickListener {
            val intent = Intent(this, CaptureCreditCardActivity::class.java)
            intent.putExtra(CARD_HOLDER_NAME, cardHolderNameOn)
            intent.putExtra(CARD_DATE, cardDateOn)
            intent.putExtra(CARD_CVC, cardCvcOn)
            startActivity(intent)
        }

        viewBinding.txtPrivacyPolice.setOnClickListener {
            val uris = Uri.parse(resources.getString(R.string.privacy_police_link))
            val intents = Intent(Intent.ACTION_VIEW, uris)
            val b = Bundle()
            b.putBoolean(NEW_WINDOW, true)
            intents.putExtras(b)
            this@MainActivity.startActivity(intents)
        }

        viewBinding.switchCardHolder.setOnCheckedChangeListener { _, isChecked ->
            cardHolderNameOn = isChecked
        }

        viewBinding.switchExpiryDate.setOnCheckedChangeListener { _, isChecked ->
            cardDateOn = isChecked
        }

        viewBinding.switchCode.setOnCheckedChangeListener { _, isChecked ->
            cardCvcOn = isChecked
        }
    }

    companion object {
        const val NEW_WINDOW = "new_window"
        const val CARD_HOLDER_NAME = "card_holder_name"
        const val CARD_DATE = "card_date"
        const val CARD_CVC = "card_cvc"
    }
}