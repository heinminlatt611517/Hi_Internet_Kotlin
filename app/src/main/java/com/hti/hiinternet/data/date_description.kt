package com.hti.hiinternet.data

import com.google.gson.annotations.SerializedName
import java.time.Year

data class date_description (
    @SerializedName("day") val day : String="",
   @SerializedName("month_year") val monthYear : String=""
)
