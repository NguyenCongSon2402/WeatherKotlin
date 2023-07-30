package com.example.weatherkotlin.data.model1.HourlyForecasts

import com.google.gson.annotations.SerializedName


data class HourlyForecasts (

  @SerializedName("DateTime"                 ) var DateTime                 : String?      = null,
  @SerializedName("EpochDateTime"            ) var EpochDateTime            : Int?         = null,
  @SerializedName("WeatherIcon"              ) var WeatherIcon              : Int?         = null,
  @SerializedName("IconPhrase"               ) var IconPhrase               : String?      = null,
  @SerializedName("HasPrecipitation"         ) var HasPrecipitation         : Boolean?     = null,
  @SerializedName("PrecipitationType"        ) var PrecipitationType        : String?      = null,
  @SerializedName("PrecipitationIntensity"   ) var PrecipitationIntensity   : String?      = null,
  @SerializedName("IsDaylight"               ) var IsDaylight               : Boolean?     = null,
  @SerializedName("Temperature"              ) var Temperature              : Temperature? = Temperature(),
  @SerializedName("PrecipitationProbability" ) var PrecipitationProbability : Int?         = null,
  @SerializedName("MobileLink"               ) var MobileLink               : String?      = null,
  @SerializedName("Link"                     ) var Link                     : String?      = null

)