package com.example.weatherkotlin.data.model1.CurrentConditions

import com.google.gson.annotations.SerializedName


data class CurrentConditions (
  @SerializedName("LocalObservationDateTime"       ) var LocalObservationDateTime       : String?                         = null,
  @SerializedName("WeatherText"                    ) var WeatherText                    : String?                         = null,
  @SerializedName("WeatherIcon"                    ) var WeatherIcon                    : Int?                            = null,
  @SerializedName("Temperature"                    ) var Temperature                    : Temperature?                    = Temperature(),
  @SerializedName("RealFeelTemperature"            ) var RealFeelTemperature            : RealFeelTemperature?            = RealFeelTemperature(),
  @SerializedName("RelativeHumidity"               ) var RelativeHumidity               : Int?                            = null,
  @SerializedName("Pressure"                       ) var Pressure                       : Pressure?                       = Pressure(),
  @SerializedName("Wind"                           ) var Wind                           : Wind?                           = Wind(),
  @SerializedName("UVIndex"                        ) var UVIndex                        : Int?                            = null,
  @SerializedName("MobileLink"                     ) var MobileLink                     : String?                         = null

)