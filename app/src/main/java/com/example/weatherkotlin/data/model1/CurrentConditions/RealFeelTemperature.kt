package com.example.weatherkotlin.data.model1.CurrentConditions

import com.google.gson.annotations.SerializedName


data class RealFeelTemperature (

  @SerializedName("Metric"   ) var Metric   : Metric?   = Metric(),
  @SerializedName("Imperial" ) var Imperial : Imperial? = Imperial()

)