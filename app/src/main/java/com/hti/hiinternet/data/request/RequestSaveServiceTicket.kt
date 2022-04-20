package com.hti.hiinternet.data.request

import com.google.gson.annotations.SerializedName
import com.hti.hiinternet.BuildConfig
import com.hti.hiinternet.data.repo.PreFerenceRepo
import com.hti.hiinternet.data.response.Category

data class RequestSaveServiceTicket (
    @SerializedName("user_id") val user_id: String?,
    @SerializedName("app_version") val app_version: Int,
    @SerializedName("phone") val phone :String,
    @SerializedName("description") val description :String,
    @SerializedName("category") val category :Category



)
