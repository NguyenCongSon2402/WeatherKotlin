package com.example.weatherkotlin.data.model1.GeopositionSearch

import com.google.gson.annotations.SerializedName


data class Region (

  @SerializedName("ID"            ) var ID            : String? = null,
  @SerializedName("LocalizedName" ) var LocalizedName : String? = null,
  @SerializedName("EnglishName"   ) var EnglishName   : String? = null

)