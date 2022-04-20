package com.hti.hiinternet.data.repo.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.hti.hiinternet.data.Notification

@Dao
interface NotiFicationDao {
    @Query("SELECT * FROM notification")
    fun getAllNoti(): LiveData<List<Notification>>

    @Query("SELECT COUNT(is_read) FROM notification where is_read=0")
    fun getNotiCount(): LiveData<Int>

    @Insert
    fun insert(noti: Notification)

    @Insert
    fun insertBackground(noti: Notification)

    @Update
    fun update(noti: Notification)


}