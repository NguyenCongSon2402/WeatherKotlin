package com.example.weatherkotlin.data.model1.HourlyForecasts

import com.google.gson.annotations.SerializedName


data class RealFeelTemperature (

  @SerializedName("Value"    ) var Value    : Int?    = null,
  @SerializedName("Unit"     ) var Unit     : String? = null,
  @SerializedName("UnitType" ) var UnitType : Int?    = null,
  @SerializedName("Phrase"   ) var Phrase   : String? = null

)