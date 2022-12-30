package com.example.multi_fcm_integration

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.mysdk.MySdk

const val FCM_ONE_ID = "FCM_ONE_ID"
class AppClass : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        MySdk.init(this)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "FCM_ONE_CHANNEL_NAME"
            val descriptionText = "FCM_ONE_CHANNEL_DESC"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(FCM_ONE_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}