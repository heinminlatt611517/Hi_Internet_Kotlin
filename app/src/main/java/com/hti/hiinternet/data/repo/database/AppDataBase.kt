package com.hti.hiinternet.data.repo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hti.hiinternet.data.Notification


@Database(entities = arrayOf(Notification::class), version = 2,exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun NotificationDao(): NotiFicationDao



    companion object {
        fun getDataBase(app: Context): AppDataBase = Room.databaseBuilder(
            app
            , AppDataBase::class.java, "notidatabase"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

}