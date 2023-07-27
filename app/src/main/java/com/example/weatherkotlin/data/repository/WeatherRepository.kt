package com.example.weatherkotlin.data.repository

import com.example.weatherkotlin.data.model1.CitySearch.CurrentConditions
import com.example.weatherkotlin.data.resource.Result

interface WeatherRepository {
    suspend fun loadCurrentWeather(): Result<List<CurrentConditions>?>
}