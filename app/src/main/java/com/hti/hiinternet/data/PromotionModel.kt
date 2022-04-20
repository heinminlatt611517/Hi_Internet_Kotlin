package com.hti.hiinternet.data

import com.google.gson.annotations.SerializedName


data class PromotionModel(
    @SerializedName("slide_id") val slideId: String,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("image") val image: String,
    @SerializedName("image_mm") val image_mm: String,
    @SerializedName("link") val link: String,
    @SerializedName("creation_date") val creationDate: String,
    @SerializedName("modified_date") val modifiedDate: String,
    @SerializedName("video_url") val videoUrl: String
    

)
