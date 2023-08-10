package com.example.weatherkotlin.data.model1.CurrentConditions

import com.google.gson.annotations.SerializedName


data class Wind (

  @SerializedName("Direction" ) var Direction : Direction? = Direction(),
  @SerializedName("Speed"     ) var Speed     : Speed?     = Speed()

)