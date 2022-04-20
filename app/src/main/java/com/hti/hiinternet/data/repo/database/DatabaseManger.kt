package com.hti.hiinternet.data.repo.database

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
class DatabaseManger {
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