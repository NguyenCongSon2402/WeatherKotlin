package com.example.weatherkotlin.data.model1.HourlyForecasts

import com.google.gson.annotations.SerializedName


data class Wind (

  @SerializedName("Speed"     ) var Speed     : Speed?     = Speed(),
  @SerializedName("Direction" ) var Direction : Direction? = Direction()

)