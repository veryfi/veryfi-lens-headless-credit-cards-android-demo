package com.veryfi.lens.headless.credit.cards.demo

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.veryfi.lens.headless.credit.cards.demo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewBinding.captureCreditCard.setOnClickListener {
            val intent = Intent(this, CaptureCreditCardActivity::class.java)
            startActivity(intent)
        }
    }
}