package com.example.weatherkotlin.data.model1.CurrentConditions

import com.google.gson.annotations.SerializedName


data class Pressure (

  @SerializedName("Metric"   ) var Metric   : Metric?   = Metric(),
  @SerializedName("Imperial" ) var Imperial : Imperial? = Imperial()

)