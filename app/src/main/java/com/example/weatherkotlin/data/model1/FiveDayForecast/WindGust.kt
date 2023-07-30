package com.example.weatherkotlin.data.model1.FiveDayForecast

import com.google.gson.annotations.SerializedName


data class WindGust (

  @SerializedName("Speed"     ) var Speed     : Speed?     = Speed(),
  @SerializedName("Direction" ) var Direction : Direction? = Direction()

)