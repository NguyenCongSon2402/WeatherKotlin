package com.example.weatherkotlin.data.model1.FiveDayForecast

import com.google.gson.annotations.SerializedName


data class Wind (

  @SerializedName("Speed"     ) var Speed     : Speed?     = Speed(),
  @SerializedName("Direction" ) var Direction : Direction? = Direction()

)