package com.hti.hiinternet.data.response

import com.google.gson.annotations.SerializedName
import com.hti.hiinternet.data.PaymentModel

data class Category(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String

)