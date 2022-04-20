package com.hti.hiinternet.data.response

import com.google.gson.annotations.SerializedName

class UserInfo(
    @SerializedName("status") val status: String,
    @SerializedName("error_code") val error_code: String,
    @SerializedName("message") val message: String,
    @SerializedName("token") val token: String,
    @SerializedName("user_id") val user_id: String,
    @SerializedName("phone") val user_phone: String,
    @SerializedName("payment_channel") val payment_channel: String

)