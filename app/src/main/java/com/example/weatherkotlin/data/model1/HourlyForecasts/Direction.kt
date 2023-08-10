package com.example.weatherkotlin.data.model1.HourlyForecasts

import com.google.gson.annotations.SerializedName


data class Direction (

  @SerializedName("Degrees"   ) var Degrees   : Int?    = null,
  @SerializedName("Localized" ) var Localized : String? = null,
  @SerializedName("English"   ) var English   : String? = null

)