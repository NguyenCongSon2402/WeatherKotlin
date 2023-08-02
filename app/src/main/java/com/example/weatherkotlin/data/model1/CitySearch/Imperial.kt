package com.example.weatherkotlin.data.model1.CitySearch

import com.google.gson.annotations.SerializedName


data class Imperial (

  @SerializedName("Value"    ) var Value    : Int?    = null,
  @SerializedName("Unit"     ) var Unit     : String? = null,
  @SerializedName("UnitType" ) var UnitType : Int?    = null

)