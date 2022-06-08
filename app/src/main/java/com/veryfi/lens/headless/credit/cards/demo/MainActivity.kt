package com.veryfi.lens.headless.credit.cards.demo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.veryfi.lens.headless.credit.cards.demo.databinding.ActivityMainBinding
import com.veryfi.lens.headless.credit.cards.demo.helpers.ThemeHelper
import com.veryfi.lens.headless.creditcards.VeryfiLensHeadless.context

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        context?.let { ThemeHelper.setBackgroundColorToStatusBar(this, it) }
        setUpClickEvents()
    }

    private fun setUpClickEvents() {
        viewBinding.captureCreditCard.setOnClickListener {
            val intent = Intent(this, CaptureCreditCardActivity::class.java)
            startActivity(intent)
        }
    }
}