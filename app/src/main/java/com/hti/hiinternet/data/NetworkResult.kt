package com.hti.hiinternet.data

import com.google.gson.annotations.SerializedName

data class NetworkResult<T>(
    @SerializedName("list")
    var data: T? = null,
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("error_code")
    var errorCode: String = "",
    @SerializedName("status")
    var statusMessage: String = "",
    @SerializedName("is_require_update")
    var is_require_update: Boolean = false,
    @SerializedName("is")
    var is_force_update: Boolean = false,
    @SerializedName("hotline_phone")
    var hotline_phone: String="",
    @SerializedName("ticket_id")
    var ticketID: String=""


)

