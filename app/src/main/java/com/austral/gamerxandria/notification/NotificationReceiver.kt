package com.austral.gamerxandria.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.austral.gamerxandria.MainActivity
import com.austral.gamerxandria.R
import kotlin.random.Random

const val notificationChannelID = "gamerxandria_notification_channel"

// BroadcastReceiver for handling notifications
class NotificationReceiver : BroadcastReceiver() {

    // Method called when the broadcast is received
    override fun onReceive(context: Context, intent: Intent) {

        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, openAppIntent, PendingIntent.FLAG_IMMUTABLE)

        val shelfName = intent.getStringExtra("shelfName") ?: "Unknown Shelf"

        val notificationManager = context.getSystemService(NotificationManager::class.java)

        val notification = NotificationCompat.Builder(context, notificationChannelID)
            .setContentTitle(context.getString(R.string.saved_in_shelf_notif_message) + shelfName)
            .setContentText(context.getString(R.string.saved_in_shelves_notif_message))
            .setSmallIcon(R.drawable.office_shelf_1__streamline_ultimate)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(Random.nextInt(), notification)
    }
}
