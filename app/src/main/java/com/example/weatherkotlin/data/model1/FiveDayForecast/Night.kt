package com.example.weatherkotlin.data.model1.FiveDayForecast

import com.google.gson.annotations.SerializedName


data class Night (

  @SerializedName("Icon"                     ) var Icon                     : Int?                = null,
  @SerializedName("IconPhrase"               ) var IconPhrase               : String?             = null,
  @SerializedName("ShortPhrase"              ) var ShortPhrase              : String?             = null,
  @SerializedName("LongPhrase"               ) var LongPhrase               : String?             = null,
  @SerializedName("RainProbability"          ) var RainProbability          : Int?                = null,
  @SerializedName("Wind"                     ) var Wind                     : Wind?               = Wind()

)