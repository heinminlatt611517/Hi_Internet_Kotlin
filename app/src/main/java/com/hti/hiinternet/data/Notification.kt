package com.hti.hiinternet.data

import androidx.room.ColumnInfo
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "notification")
data class Notification(
    @PrimaryKey(autoGenerate = true)
    var ID: Int = 0,
    @ColumnInfo(name = "is_read")
    var isRead: Boolean = false,
    var title: String = "",
    var message: String = "",
    var type: String = "",
    var type_name: String = "",
    var actionUrl: String ="",
    var createDate: String = ""
) : Serializable

