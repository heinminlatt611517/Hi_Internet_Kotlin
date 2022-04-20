package com.hti.hiinternet.data.response

import com.google.gson.annotations.SerializedName
import com.hti.hiinternet.data.OurAppModel
import com.hti.hiinternet.data.PromotionModel

data class ResponseHomeData(

    @SerializedName("up_images")
    val upImage: ArrayList<PromotionModel>,
    @SerializedName("middle_images")
    val middleImage: ArrayList<PromotionModel>
    ,
    @SerializedName("down_images")
    val downlImagea: ArrayList<OurAppModel>

)