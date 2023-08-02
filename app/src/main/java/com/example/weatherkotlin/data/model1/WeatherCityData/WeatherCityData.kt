package com.example.weatherkotlin.data.model1.WeatherCityData

import com.example.weatherkotlin.data.model1.CurrentConditions.CurrentConditions
import com.example.weatherkotlin.data.model1.FiveDayForecast.FiveDayForecast
import com.example.weatherkotlin.data.model1.HourlyForecasts.HourlyForecasts

data class WeatherCityData(
    val currentWeather: List<CurrentConditions>?,
    val hourlyForecast: List<HourlyForecasts>?,
    val fiveDayForecast: FiveDayForecast?
)