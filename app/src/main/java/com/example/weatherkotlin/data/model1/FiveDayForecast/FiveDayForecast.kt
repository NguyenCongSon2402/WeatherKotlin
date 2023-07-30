package com.example.weatherkotlin.data.model1.FiveDayForecast

import com.google.gson.annotations.SerializedName


data class FiveDayForecast (

  @SerializedName("Headline"       ) var Headline       : Headline?                 = Headline(),
  @SerializedName("DailyForecasts" ) var DailyForecasts : ArrayList<DailyForecasts> = arrayListOf()

)