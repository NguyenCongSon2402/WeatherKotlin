package com.example.weatherkotlin.data.model1.FiveDayForecast

import com.google.gson.annotations.SerializedName


data class Moon (

  @SerializedName("Rise"      ) var Rise      : String? = null,
  @SerializedName("EpochRise" ) var EpochRise : Int?    = null,
  @SerializedName("Set"       ) var Set       : String? = null,
  @SerializedName("EpochSet"  ) var EpochSet  : Int?    = null,
  @SerializedName("Phase"     ) var Phase     : String? = null,
  @SerializedName("Age"       ) var Age       : Int?    = null

)