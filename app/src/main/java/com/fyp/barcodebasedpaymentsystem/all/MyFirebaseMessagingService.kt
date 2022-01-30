package com.fyp.barcodebasedpaymentsystem.all

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.fyp.barcodebasedpaymentsystem.R
import com.fyp.barcodebasedpaymentsystem.all.activity.Dashboard
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.os.PowerManager
import android.provider.Settings


const val channelId = "notification_channel"
const val channelName = "com.fyp.barcodebasedpaymentsystem.all"

class MyFirebaseMessagingService: FirebaseMessagingService() {


    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

        /*
        val refreshToken = FirebaseInstanceId.getInstance().token
        Log.e("refresh token", refreshToken!!)
    */

        Log.d(TAG, "Token :$p0")


    }


    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        showNotification(p0.notification!!.title.toString(),p0.notification!!.body.toString())
    }



    fun showNotification(title: String, message: String){

        val builder = NotificationCompat.Builder(this, channelId)
            .setContentText(title)
            .setContentText(message)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager



        //val manager = NotificationManagerCompat.from(this)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            val notificationChannel = NotificationChannel(channelId, channelName,NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(notificationChannel)
        }


        manager.notify(222,builder.build())

    }





    /*
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
      if(remoteMessage.notification != null){
          generateNotification(remoteMessage.notification!!.title!!,remoteMessage.notification!!.body!!)
      }
    }



    fun getRemoteView(title: String, message: String): RemoteViews {
        val remoteView = RemoteViews("com.fyp.barcodebasedpaymentsystem.all", R.layout.notification)

        remoteView.setTextViewText(R.id.title_noti,title)
        remoteView.setTextViewText(R.id.message_noti,message)
        remoteView.setImageViewResource(R.id.app_logo, R.drawable.ic_notify_icon)

        return remoteView
    }

    fun generateNotification(title: String, message: String){
        val intent = Intent(this, Dashboard::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)

        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext,
            channelId)
            .setSmallIcon(R.drawable.ic_notify_icon)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title,message))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(channelId, channelName,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)

        }

        notificationManager.notify(0,builder.build())

    }

     */

}