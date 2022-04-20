package com.hti.hiinternet.data.response

import com.google.gson.annotations.SerializedName

class ResponseAccountData (
    @SerializedName("user_id") val user_id: String,
    @SerializedName("name") val user_name: String,
    @SerializedName("plan") val plan: String,
    @SerializedName("activate_date") val activate_date: String,
    @SerializedName("account_status") val account_status: String,
    @SerializedName("service") val service: String,
    @SerializedName("mobile_no") val mobile_no: String,
    @SerializedName("address") val address: String,
    @SerializedName("created_date") val created_date: String,
    @SerializedName("modified_date") val modified_date: String

    )
