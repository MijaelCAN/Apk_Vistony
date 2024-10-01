package com.vistony.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFireBaseMessagingserService: FirebaseMessagingService(){
    override fun onMessageReceived(message: RemoteMessage) {
        //super.onMessageReceived(message)
        val title = message.notification?.title
        val body = message.notification?.body

        sendNotification(title, body)
    }

    private fun sendNotification(title: String?, messageBody: String?) {
        val notificationBuilder = NotificationCompat.Builder(this,CHANNEL_ID)
            .setSmallIcon(R.mipmap.logo)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID,"Canal de Notificaciones",NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0,notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("TAGH", "Nuevo token: " + token);
    }
    companion object{
        private const val CHANNEL_ID = "my_channel_id"
    }
}