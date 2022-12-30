package com.example.mysdk

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SecondSDkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_sdk)
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent?) {
        val appLinkAction: String? = intent?.action
        val appLinkData: Uri? = intent?.data
        showDeepLinkOffer(appLinkAction, appLinkData)
    }

    private fun showDeepLinkOffer(appLinkAction: String?, appLinkData: Uri?) {
        if (appLinkAction == Intent.ACTION_VIEW && appLinkData != null) {
            val id = appLinkData.getQueryParameter("id")
            if (id.isNullOrBlank().not()) {
                Toast.makeText(this, "deep link data: $id", Toast.LENGTH_LONG).show()
            }
        }
    }
}