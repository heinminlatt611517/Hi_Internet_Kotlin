package com.hti.hiinternet

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import com.hti.hiinternet.data.Notification
import com.hti.hiinternet.data.repo.PreFerenceRepo
import com.hti.hiinternet.data.repo.database.DatabaseManger

class NotificationService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        (intent?.getBundleExtra("data") as Bundle).let {
            saveNotiFication((it.getSerializable("data") as Notification))

        }
        return START_NOT_STICKY

    }

    fun saveNotiFication(noti: Notification) {
        DatabaseManger.getDataBase(application)
            .NotificationDao()
            .insertBackground(noti)

    }


}