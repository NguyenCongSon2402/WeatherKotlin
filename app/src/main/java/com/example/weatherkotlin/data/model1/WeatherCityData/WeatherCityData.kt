package com.example.weatherkotlin.data.model1.WeatherCityData

import com.example.weatherkotlin.data.model1.CurrentConditions.CurrentConditions
import com.example.weatherkotlin.data.model1.FiveDayForecast.FiveDayForecast
import com.example.weatherkotlin.data.model1.HourlyForecasts.HourlyForecasts

data class WeatherCityData(
    val currentWeather: List<CurrentConditions>?=null,
    val hourlyForecast: List<HourlyForecasts>??=null,
    val fiveDayForecast: FiveDayForecast?=null
)