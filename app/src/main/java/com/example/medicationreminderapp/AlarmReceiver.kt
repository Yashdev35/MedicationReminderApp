package com.example.medicationreminderapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationCompat
import androidx.wear.compose.material.Colors

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        // Ensure that the context and intent are not null
        context ?: return
        intent ?: return

        // Extract data from the intent
        val medicationName = intent.getStringExtra("MEDICATION_NAME")

        // Show notification using NotificationManager
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Check if the Android version is Oreo or higher
        val channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        } else {
            ""
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Medication Reminder")
            .setContentText("It's time to take your $medicationName.")
            .setSmallIcon(R.drawable.pillicon)
            .setContentIntent(getPendingIntent(context, medicationName))
            .setAutoCancel(true)
            .build()

        // Notify with a unique ID (use a unique ID for each notification)
        notificationManager.notify(medicationName.hashCode(), notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager): String {
        val channelId = "MedNotificId"
        val channelName = "Mednotificaiton"
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
            description = "Channel for Medication Reminders"
            enableLights(true)
            lightColor = Color.Blue.toArgb()
        }
        notificationManager.createNotificationChannel(channel)
        return channelId
    }

    private fun getPendingIntent(context: Context, medicationName: String?): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("MEDICATION_NAME", medicationName)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}
