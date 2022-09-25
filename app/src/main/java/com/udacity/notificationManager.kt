package com.udacity

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
private const val  notificationId = 0
fun NotificationManager.sendMessage(applicationContext:Context,channelId:String,status :String,fileName:String){
    var intent = Intent(applicationContext,DetailActivity::class.java)
    intent.putExtra("status",status)
    intent.putExtra("file name",fileName)
    var pendingIntent = PendingIntent.getActivity(applicationContext,
        notificationId,intent,PendingIntent.FLAG_UPDATE_CURRENT)
    val builder = NotificationCompat.Builder(
        applicationContext,
        channelId
    ).setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(applicationContext.getString(R.string.notification_description))
        .setAutoCancel(true)
        .addAction(R.drawable.ic_assistant_black_24dp,applicationContext.getString(R.string.notification_button),pendingIntent)
        .setPriority(NotificationCompat.PRIORITY_HIGH).build()
    notify(notificationId,builder)
}

