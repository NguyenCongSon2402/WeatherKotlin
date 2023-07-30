package com.example.weatherkotlin.data.model1.FiveDayForecast

import com.google.gson.annotations.SerializedName


data class DailyForecasts (

  @SerializedName("Date"                     ) var Date                     : String?                   = null,
  @SerializedName("EpochDate"                ) var EpochDate                : Int?                      = null,
  @SerializedName("Sun"                      ) var Sun                      : Sun?                      = Sun(),
  @SerializedName("Moon"                     ) var Moon                     : Moon?                     = Moon(),
  @SerializedName("Temperature"              ) var Temperature              : Temperature?              = Temperature(),
  @SerializedName("RealFeelTemperature"      ) var RealFeelTemperature      : RealFeelTemperature?      = RealFeelTemperature(),
  @SerializedName("RealFeelTemperatureShade" ) var RealFeelTemperatureShade : RealFeelTemperatureShade? = RealFeelTemperatureShade(),
  @SerializedName("HoursOfSun"               ) var HoursOfSun               : Double?                   = null,
  @SerializedName("DegreeDaySummary"         ) var DegreeDaySummary         : DegreeDaySummary?         = DegreeDaySummary(),
  @SerializedName("AirAndPollen"             ) var AirAndPollen             : ArrayList<AirAndPollen>   = arrayListOf(),
  @SerializedName("Day"                      ) var Day                      : Day?                      = Day(),
  @SerializedName("Night"                    ) var Night                    : Night?                    = Night(),
  @SerializedName("Sources"                  ) var Sources                  : ArrayList<String>         = arrayListOf(),
  @SerializedName("MobileLink"               ) var MobileLink               : String?                   = null,
  @SerializedName("Link"                     ) var Link                     : String?                   = null

)