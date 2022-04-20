package com.hti.hiinternet.data.request

import com.google.gson.annotations.SerializedName
import com.hti.hiinternet.BuildConfig
import com.hti.hiinternet.data.repo.PreFerenceRepo

data class RequestComplainCategory(
    @SerializedName("user_id") val user_id: String? = PreFerenceRepo.userId,
    @SerializedName("app_version") val app_version: Int = BuildConfig.VERSION_CODE
)
