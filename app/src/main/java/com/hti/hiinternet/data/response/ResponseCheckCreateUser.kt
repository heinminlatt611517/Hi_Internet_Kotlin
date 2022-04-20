package com.hti.hiinternet.data.response

import com.google.gson.annotations.SerializedName

data class ResponseCheckCreateUser (
    @SerializedName("is_require_update") val requiredUpdate: String

    , @SerializedName("is_force_update") val isForceUpdate: String
    , @SerializedName("error_code") val errorCode: String
    , @SerializedName("check_createduser") val checkCreateUser: String
)
