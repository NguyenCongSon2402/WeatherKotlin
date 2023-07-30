package com.example.weatherkotlin.data.model1.FiveDayForecast

import com.google.gson.annotations.SerializedName


data class Sun (

  @SerializedName("Rise"      ) var Rise      : String? = null,
  @SerializedName("EpochRise" ) var EpochRise : Int?    = null,
  @SerializedName("Set"       ) var Set       : String? = null,
  @SerializedName("EpochSet"  ) var EpochSet  : Int?    = null

)