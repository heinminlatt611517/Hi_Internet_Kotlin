package com.hti.hiinternet.data.request

import com.google.gson.annotations.SerializedName
import com.hti.hiinternet.BuildConfig
import com.hti.hiinternet.data.repo.PreFerenceRepo

data class RequestFbId (
    @SerializedName("facebook_id") val facebookID : String? = PreFerenceRepo.ufid ,
    @SerializedName("app_version") val app_version: Int = BuildConfig.VERSION_CODE
)
