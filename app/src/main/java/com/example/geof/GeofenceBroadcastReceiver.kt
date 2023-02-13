package com.example.geof

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.geof.MainActivity.Companion.ACTION_GEOFENCE_EVENT
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_GEOFENCE_EVENT) {
            val geofencingEvent = GeofencingEvent.fromIntent(intent)

//            if (geofencingEvent!!.hasError()) {
//                val errorMessage = errorMessage(context, geofencingEvent.errorCode)
//                Log.e(TAG, errorMessage)
//                return
//            }

            if (geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                Log.v(TAG, "geofence_entered")

                val fenceId = when {geofencingEvent.triggeringGeofences!!.isNotEmpty() -> geofencingEvent.triggeringGeofences!![0].requestId
                    else -> {
                        Log.e(TAG, "No Geofence Trigger Found! Abort mission!")
                        return
                    }
                }

                val foundIndex = MainActivity.dGeoArr.indexOfFirst { it.id == fenceId }

                if ( -1 == foundIndex ) {
                    Log.e(TAG, "Unknown Geofence: Abort Mission")
                    return
                }
                val notificationManager = ContextCompat.getSystemService(context, NotificationManager::class.java) as NotificationManager
                notificationManager.sendGeofenceEnteredNotification(context, foundIndex, true)
            } else if (geofencingEvent.geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                Log.v(TAG, "geofence_exit")

                val fenceId = when {geofencingEvent.triggeringGeofences!!.isNotEmpty() -> geofencingEvent.triggeringGeofences!![0].requestId
                    else -> {
                        Log.e(TAG, "No Geofence Trigger Found! Abort mission!")
                        return
                    }
                }

                val foundIndex = MainActivity.dGeoArr.indexOfFirst { it.id == fenceId }

                if ( -1 == foundIndex ) {
                    Log.e(TAG, "Unknown Geofence: Abort Mission")
                    return
                }
                val notificationManager = ContextCompat.getSystemService(context, NotificationManager::class.java) as NotificationManager
                notificationManager.sendGeofenceEnteredNotification(context, foundIndex, false)
            }
        }
    }
}

private const val TAG = "GeofenceReceiver"
