package com.example.geof

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat

fun createChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(CHANNEL_ID, "channel_name", NotificationManager.IMPORTANCE_HIGH).apply {
            setShowBadge(false)
        }

        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.description = "notification_channel_description"
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)
    }
}

fun NotificationManager.sendGeofenceEnteredNotification(
    context: Context,
    foundIndex: Int,
    flag: Boolean? = true
) {
    var text = "You Entered Zone "
    if (!flag!!){
        text = "You Leave Zone "
    }
    val contentIntent = Intent(context, MainActivity::class.java)
    contentIntent.putExtra("GEOFENCE_INDEX", foundIndex)
    val contentPendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    val mapImage = BitmapFactory.decodeResource(context.resources, R.drawable.aaa)
    val bigPicStyle = NotificationCompat.BigPictureStyle().bigPicture(mapImage).bigLargeIcon(null)

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setContentTitle(context.getString(R.string.app_name))
        .setContentText(text + MainActivity.dGeoArr[foundIndex].id)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setContentIntent(contentPendingIntent)
        .setSmallIcon(R.drawable.aaa)
//        .setStyle(bigPicStyle)
        .setLargeIcon(mapImage)

    notify(NOTIFICATION_ID, builder.build())
}

private const val NOTIFICATION_ID = 33
private const val CHANNEL_ID = "GeofenceChannel"


