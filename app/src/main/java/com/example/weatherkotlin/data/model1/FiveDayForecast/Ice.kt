package com.example.weatherkotlin.data.model1.FiveDayForecast

import com.google.gson.annotations.SerializedName


data class Ice (

  @SerializedName("Value"    ) var Value    : Int?    = null,
  @SerializedName("Unit"     ) var Unit     : String? = null,
  @SerializedName("UnitType" ) var UnitType : Int?    = null

)