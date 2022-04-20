package com.hti.hiinternet.data.request

import com.google.gson.annotations.SerializedName
import com.hti.hiinternet.BuildConfig
import com.hti.hiinternet.data.repo.PreFerenceRepo

data class RequestSaveFcm(
    @SerializedName("firebase_token")
    val fcmToken: String,
    @SerializedName("user_id")
    val userId: String? = PreFerenceRepo.userId,
    @SerializedName("app_version")
    val app_version: Int = BuildConfig.VERSION_CODE
)