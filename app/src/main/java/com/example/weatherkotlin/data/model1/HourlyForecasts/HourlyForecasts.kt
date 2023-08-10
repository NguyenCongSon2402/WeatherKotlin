package com.example.weatherkotlin.data.model1.HourlyForecasts

import com.google.gson.annotations.SerializedName


data class HourlyForecasts (

  @SerializedName("DateTime"                 ) var DateTime                 : String?                   = null,
  @SerializedName("EpochDateTime"            ) var EpochDateTime            : Int?                      = null,
  @SerializedName("WeatherIcon"              ) var WeatherIcon              : Int?                      = null,
  @SerializedName("IconPhrase"               ) var IconPhrase               : String?                   = null,
  @SerializedName("Temperature"              ) var Temperature              : Temperature?              = Temperature(),
  @SerializedName("RealFeelTemperature"      ) var RealFeelTemperature      : RealFeelTemperature?      = RealFeelTemperature(),
  @SerializedName("Wind"                     ) var Wind                     : Wind?                     = Wind(),
  @SerializedName("RelativeHumidity"         ) var RelativeHumidity         : Int?                      = null,
  @SerializedName("UVIndex"                  ) var UVIndex                  : Int?                      = null,
  @SerializedName("UVIndexText"              ) var UVIndexText              : String?                   = null,
  @SerializedName("RainProbability"          ) var RainProbability          : Int?                      = null,
  @SerializedName("MobileLink"               ) var MobileLink               : String?                   = null,
  @SerializedName("Link"                     ) var Link                     : String?                   = null

)