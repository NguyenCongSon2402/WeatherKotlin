package com.example.weatherkotlin.data.model1.FiveDayForecast

import com.google.gson.annotations.SerializedName


data class DailyForecasts (

  @SerializedName("Date"                     ) var Date                     : String?                   = null,
  @SerializedName("EpochDate"                ) var EpochDate                : Int?                      = null,
  @SerializedName("Temperature"              ) var Temperature              : Temperature?              = Temperature(),
  @SerializedName("RealFeelTemperature"      ) var RealFeelTemperature      : RealFeelTemperature?      = RealFeelTemperature(),
  @SerializedName("Day"                      ) var Day                      : Day?                      = Day(),
  @SerializedName("Night"                    ) var Night                    : Night?                    = Night(),
  @SerializedName("Sources"                  ) var Sources                  : ArrayList<String>         = arrayListOf(),
  @SerializedName("MobileLink"               ) var MobileLink               : String?                   = null,
  @SerializedName("Link"                     ) var Link                     : String?                   = null

)