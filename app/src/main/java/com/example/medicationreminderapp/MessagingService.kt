package com.example.medicationreminderapp
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.Calendar

class MessagingService  : FirebaseMessagingService() {

    override fun onNewToken(token: String) {

    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Handle FCM messages and show notifications
        showNotification(remoteMessage.data["title"], remoteMessage.data["body"])
    }

    private fun showNotification(title: String?, body: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        val channelId = "MedNotificId" // Unique channel ID for your notifications
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.pillicon)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Mednotificaiton", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }
}

fun scheduleDailyAlarm(context: Context, medicationHour: Int, medicationMinute: Int, medicationName: String) {
    // Create a Calendar instance
    val calendar = Calendar.getInstance().apply {
        // Set the alarm time to 8:00 AM
        set(Calendar.HOUR_OF_DAY, medicationHour)
        set(Calendar.MINUTE, medicationMinute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)

        // Ensure that the alarm is scheduled for the next day if the current time has passed
        if (timeInMillis <= System.currentTimeMillis()) {
            add(Calendar.DAY_OF_MONTH, 1)
        }
    }

    // Convert the Calendar time to milliseconds
    val medicationTimeMillis = calendar.timeInMillis

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val alarmIntent = Intent(context, AlarmReceiver::class.java).apply {
        putExtra("MEDICATION_NAME", medicationName)
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        alarmIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    // Schedule the daily alarm
    alarmManager.setRepeating(
        AlarmManager.RTC_WAKEUP,
        medicationTimeMillis,
        AlarmManager.INTERVAL_DAY,
        pendingIntent
    )
}





