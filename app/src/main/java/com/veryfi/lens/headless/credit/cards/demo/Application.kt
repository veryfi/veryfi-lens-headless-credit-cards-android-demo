package com.veryfi.lens.headless.credit.cards.demo

import android.app.Application
import android.util.Log
import com.veryfi.lens.headless.creditcards.VeryfiLensHeadless
import com.veryfi.lens.headless.creditcards.VeryfiLensHeadlessCredentials
import com.veryfi.lens.headless.creditcards.VeryfiLensHeadlessSettings

class Application: Application() {

    override fun onCreate() {
        super.onCreate()
        configureLens()
    }

    private fun configureLens() {
        val veryfiLensHeadlessCredentials = VeryfiLensHeadlessCredentials()
        val veryfiLensHeadlessSetting = VeryfiLensHeadlessSettings()
        veryfiLensHeadlessCredentials.apiKey = AUTH_API_KEY
        veryfiLensHeadlessCredentials.username = AUTH_USERNAME
        veryfiLensHeadlessCredentials.clientId = CLIENT_ID

        Log.d(TAG, "VeryfiLens.configure loading...")
        VeryfiLensHeadless.configure(this, veryfiLensHeadlessCredentials, veryfiLensHeadlessSetting) {
            Log.d(TAG, "VeryfiLens.configure $it")
        }
    }

    companion object {
        //REPLACE YOUR KEYS HERE
        const val CLIENT_ID = BuildConfig.VERYFI_CLIENT_ID
        const val AUTH_USERNAME = BuildConfig.VERYFI_USERNAME
        const val AUTH_API_KEY = BuildConfig.VERYFI_API_KEY
        const val TAG = "CreditCardDemo"
    }

}