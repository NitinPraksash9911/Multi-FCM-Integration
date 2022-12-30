package com.example.multi_fcm_integration

import android.Manifest.permission.READ_PHONE_STATE
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var textView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.tv)
//        initFCM()
        getDetails()
        handleDeepLink(intent)
        getPsuedoID()
    }

    private fun getPsuedoID(): String {
        val m_szDevIDShort =
            "35" + Build.BOARD.length % 10 +
                    Build.BRAND.length % 10 +
                    Build.CPU_ABI.length % 10 +
                    Build.DEVICE.length % 10 +
                    Build.DISPLAY.length % 10 +
                    Build.HOST.length % 10 +
                    Build.ID.length % 10 +
                    Build.MANUFACTURER.length % 10 +
                    Build.MODEL.length % 10 +
                    Build.PRODUCT.length % 10 +
                    Build.TAGS.length % 10 +
                    Build.TYPE.length % 10 +
                    Build.USER.length % 10

        val serial = Build.getRadioVersion()
        return UUID(m_szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()

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
                Toast.makeText(this, id, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getSIMDetails() {
        var MSG = ""
        val mTelephonyMgr = this.getSystemService(TELEPHONY_SERVICE) as TelephonyManager

        val countryiso: String = mTelephonyMgr.simCountryIso

        val simOperator: String = mTelephonyMgr.simOperator

        val simOperatorName: String = mTelephonyMgr.simOperatorName
        val androidID: String =
            Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)


        val subscriptionManager =
            getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
        val activeSubscriptionInfoList: List<SubscriptionInfo> =
            subscriptionManager.activeSubscriptionInfoList

        var subscriptionId = ""


        activeSubscriptionInfoList.forEach {
            subscriptionId += it.subscriptionId
        }

        MSG += "Country: $countryiso \nsimOperator: $simOperator" +
                " \nsubscriptionId: $subscriptionId \nsimOperatorName: $simOperatorName \nAndroid Id: $androidID \n"
        MSG += "Sim Status: "
        when (mTelephonyMgr.simState) {
            TelephonyManager.SIM_STATE_ABSENT -> {
                MSG += " absent"
            }
            TelephonyManager.SIM_STATE_NETWORK_LOCKED -> {
                MSG += "SIM_STATE_NETWORK_LOCKED"

            }
            TelephonyManager.SIM_STATE_PIN_REQUIRED -> {
                MSG += "SIM_STATE_PIN_REQUIRED"

            }
            TelephonyManager.SIM_STATE_PUK_REQUIRED -> {
                MSG += "SIM_STATE_PUK_REQUIRED"
            }
            TelephonyManager.SIM_STATE_READY -> {
                MSG += "SIM_STATE_READY"

            }
            TelephonyManager.SIM_STATE_UNKNOWN -> {
                MSG += "SIM_STATE_UNKNOWN"

            }
        }
        textView.text = MSG
        printData(msg = MSG)
    }


    fun getDetails() {
        if (ActivityCompat.checkSelfPermission(
                this,
                READ_PHONE_STATE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getSIMDetails()
        } else {
            // Ask for permission
            requestPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> {
                getDetails()
            }
            else -> throw IllegalStateException("Unexpected value: $requestCode")
        }
    }


    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(READ_PHONE_STATE), 100)
        }
    }

    private fun printData(msg: String) {
        Log.d("APPSIM", msg)
//        Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
    }

    private fun initFCM() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG_ONE, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            // Log and toast
            Log.d(TAG_ONE, token)
//            Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
        })
    }


}