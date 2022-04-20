package com.hti.hiinternet.data

import com.google.gson.annotations.SerializedName

data class PaymentModel (
    @SerializedName("paid_status") val paid_status: String,
    @SerializedName("due_date") val due_date: String,
    @SerializedName("invoice_id") val invoice_id: String,
    @SerializedName("amount") val amount: String,
    @SerializedName("creation_date") val creation_date: String,
    @SerializedName("modified_date") val modified_date: String
)