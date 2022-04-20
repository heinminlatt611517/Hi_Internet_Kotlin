package com.hti.hiinternet.Firebase

import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.hti.hiinternet.MainActivity
import com.hti.hiinternet.NotificationService
import com.hti.hiinternet.R
import com.hti.hiinternet.data.Notification
import com.hti.hiinternet.data.greedbootevent.EventRecieveNewNoti
import com.hti.hiinternet.data.repo.database.DatabaseManger
import org.greenrobot.eventbus.EventBus

//class MyFirebaseMessagingService : FirebaseMessagingService() {
//
//    private val NOTI_EXTRA="Notification"
//
//    override fun onMessageReceived(p0: RemoteMessage) {
//        super.onMessageReceived(p0)
//
//
//        p0!!.data.let {
//            if (p0.data.isNotEmpty()) {
//                var title = it["title"] as String
//                var messge = it["message"] as String
//                var type = it["type"] as String
//                var type_name = it["type_name"] as String
//                var created_date = it["created"] as String
//                var action_url = it["action_url"] as String
//                Log.d("notiStatus", "recieve noti")
//
//
//                saveNotiFication(
//                    Notification(
//                        title = title,
//                        message = messge,
//                        type = type,
//                        type_name = type_name,
//                        createDate = created_date,
//                        actionUrl = action_url
//                    )
//                )
//
//                showNotiFication(title, messge)
//
//
//            }
//
//        }
//
//    }
//
//
//    override fun onNewToken(p0: String) {
//        super.onNewToken(p0)
//
//
//    }
//  /*  override fun onMessageReceived(remoteMessage: RemoteMessage?) {
//        super.onMessageReceived(remoteMessage!!)
//        Log.d("notiStatus", "recieve noti")
//
//        remoteMessage!!.data.let {
//            if (remoteMessage.data.isNotEmpty()) {
//                var title = it["title"] as String
//                var messge = it["message"] as String
//                var type = it["type"] as String
//                var type_name = it["type_name"] as String
//                var created_date = it["created"] as String
//                var action_url = it["action_url"] as String
//                Log.d("notiStatus", "recieve noti")
//                showNotiFication(title, messge)
//                saveNotiFication(
//                    Notification(
//                        title = title,
//                        message = messge,
//                        type = type,
//                        type_name = type_name,
//                        createDate = created_date,
//                        actionUrl = action_url
//                    )
//                )
//
//
//            }
//
//        }
//
//
//    }
//
//
//    override fun onNewToken(token: String?) {
//        super.onNewToken(token!!)
//        Log.d("firebasetokenStatus", "new token $token")
//    }*/
//
//    fun saveNotiFication(noti: Notification) {
//        /* DatabaseManger.getDataBase(application)
//             .NotificationDao()
//             .insert(noti)*/
//        //insert data in db
//        startNotificationService(noti)
//        postNewNotiEvent()
//    }
//
//    fun postNewNotiEvent() {
//        EventBus.getDefault().post(EventRecieveNewNoti(0))
//
//    }
//
//    private fun startNotificationService(noti: Notification) {
//        val i = Intent(this, NotificationService::class.java)
//        val bundle = Bundle()
//        bundle.putSerializable("data", noti)
//        i.putExtra("data", bundle)
//        if (isServiceRunning()) {
//            Log.d("insertDataService", "already start ")
//            stopService(i)
//            startService(i)
//        } else {
//            Log.d("insertDataService", "not start ")
//            startService(i)
//        }
//    }
//
//    private fun isServiceRunning(): Boolean {
//        val manager =
//            getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
//            if ("com.hti.hiinternet.NotificationService" == service.service.className) {
//                return true
//            }
//        }
//        return false
//    }
//
//
//    fun showNotiFication(title: String?, body: String) {
//
//
//        val intent = MainActivity.newIntent(this,"Notification")
//
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val pendingIntent = PendingIntent.getActivity(
//            this, 0, intent,
//            PendingIntent.FLAG_ONE_SHOT
//
//        )
//
//        val notificationManager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        val noti_channel_id = "hti_noti"
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//
//            val notificationChannel = NotificationChannel(
//                noti_channel_id,
//                "hti",
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            notificationChannel.description = "promotion noti for hti"
//            notificationChannel.enableLights(false)
//            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
//            notificationChannel.enableVibration(true)
//            notificationManager.createNotificationChannel(notificationChannel)
//
//
//            val notification =
//                NotificationCompat.Builder(this, noti_channel_id)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setContentIntent(pendingIntent)
//                    .setAutoCancel(true)
//                    .setContentTitle(title)
//                    .setContentText(body)
//                    .setPriority(android.app.Notification.PRIORITY_HIGH)
//                    .setTicker("hti")
//
//                    .build()
//            notificationManager.notify(1, notification)
//
//        }
//    }
//
//
//}
class  MyFirebaseMessagingService:FirebaseMessagingService() {
    private val NOTI_EXTRA = "Notification"
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        p0 !!. data . let {
            if (p0.data.isNotEmpty()) {
                var title = it["title"] as String
                var messge = it["message"] as String
                var type = it["type"] as String
                var type_name = it["type_name"] as String
                var created_date = it["created"] as String
                var action_url =it["action_url"] as String

                Log . d ("notiStatus", "recieve noti")
                saveNotiFication (Notification (title = title, message = messge, type = type, type_name = type_name, createDate = created_date, actionUrl = action_url))
                showNotiFication (title, messge)
            }
        }

    }
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

    fun saveNotiFication(noti: Notification) {
        DatabaseManger.getDataBase(this).NotificationDao()
            .insert(noti)

    }
        fun showNotiFication(title: String?, body: String) {


            val intent = MainActivity.newIntent(this, "Notification")

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT

            )

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val noti_channel_id = "hti_noti"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


                val notificationChannel = NotificationChannel(
                    noti_channel_id,
                    "hti",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationChannel.description = "promotion noti for hti"
                notificationChannel.enableLights(false)
                notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
                notificationChannel.enableVibration(true)
                notificationManager.createNotificationChannel(notificationChannel)


                val notification =
                    NotificationCompat.Builder(this, noti_channel_id)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setPriority(android.app.Notification.PRIORITY_HIGH)
                        .setTicker("hti")

                        .build()
                notificationManager.notify(1, notification)

            }
        }

    }