package com.hti.hiinternet.data.request

import com.google.gson.annotations.SerializedName


data class RequestLogin(
    @SerializedName("user_id") val userId: String
    //, @SerializedName("firebase_token") val token: String
    , @SerializedName("facebook_id") val facebookId: String
    , @SerializedName("profile_link") val profileLink: String

)