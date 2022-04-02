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
        veryfiLensHeadlessCredentials.apiKey = AUTH_API_K
        veryfiLensHeadlessCredentials.username = AUTH_USRNE
        veryfiLensHeadlessCredentials.clientId = CLIENT_ID

        Log.d(TAG, "VeryfiLens.configure loading...")
        VeryfiLensHeadless.configure(this, veryfiLensHeadlessCredentials, veryfiLensHeadlessSetting) {
            Log.d(TAG, "VeryfiLens.configure $it")
        }
    }

    companion object {

        const val CLIENT_ID = ""
        const val AUTH_USRNE = ""
        const val AUTH_API_K = ""
        const val TAG = "CreditCardDemo"
    }

}