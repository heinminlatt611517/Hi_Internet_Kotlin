package com.hti.hiinternet.data.response

import com.google.gson.annotations.SerializedName
import com.hti.hiinternet.data.PaymentModel
import com.hti.hiinternet.data.date_description

data class ResponsePayment(
    @SerializedName("paid_status") val paid_status: String,
    @SerializedName("due_date") val due_date: date_description,
    @SerializedName("invoice_id") val invoice_id: String,
    @SerializedName("amount") val amount: String,
    @SerializedName("creation_date") val creation_date: String,
    @SerializedName("paid_url") val paid_url: String,
    @SerializedName("modified_date") val modified_date: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    @SerializedName("paid_date") val paid_date: date_description

)


