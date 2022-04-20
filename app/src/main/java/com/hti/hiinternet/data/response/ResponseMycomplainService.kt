package com.hti.hiinternet.data.response

import com.google.gson.annotations.SerializedName

data class ResponseMycomplainService (
    @SerializedName("ticket_id") val ticket_id: String,
    @SerializedName("type") val type: String,
    @SerializedName("topic") val topic: String,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: String,
    @SerializedName("creation_date") val creation_date: String,
    @SerializedName("resloved_time") val resloved_time: String

    )