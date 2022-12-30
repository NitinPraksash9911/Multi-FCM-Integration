package com.example.mysdk

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging

const val FCM_SDK_ID = "FCM_SDK_ID"

object MySdk {

    fun init(application: Application) {
        createNotificationChannel(application)
//        secondWayForFirebase(application)
//        initFCM()
    }

    private fun createNotificationChannel(application: Application) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "FCM_SDK_CHANNEL_NAME"
            val descriptionText = "FCM_SDK_CHANNEL_DESC"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(FCM_SDK_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun secondWayForFirebase(application: Application) {
        val firebaseOptions = FirebaseOptions.Builder()
            .setApiKey("AIzaSyDChYxg9HCknELb4h49N5T0fGSSqwZeq_4")
            .setApplicationId("1:939184647497:android:0f3901175e9d4fd3617e98")
            .setGcmSenderId("939184647497")
            .setStorageBucket("fcm-second-5bd86.appspot.com")
            .setProjectId("fcm-second-5bd86")
            .build()

        FirebaseApp.initializeApp(application, firebaseOptions, "Secondary")
        val secondary = FirebaseApp.getInstance("Secondary")
    }

    private fun initFCM() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG_TWO, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            // Log
            Log.d(TAG_TWO, token)
        })
    }
}

/**
 *
 * TODO: 1. Check if project firebase->dynamic link dependency required
 *       2. Then check from where we are getting query.parameter in deep link
* */