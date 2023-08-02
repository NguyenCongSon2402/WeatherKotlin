package com.example.weatherkotlin.data.repository

import com.example.weatherkotlin.data.model1.CitySearch.CitySearch
import com.example.weatherkotlin.data.model1.CurrentConditions.CurrentConditions
import com.example.weatherkotlin.data.model1.FiveDayForecast.FiveDayForecast
import com.example.weatherkotlin.data.model1.GeopositionSearch.CityResponse
import com.example.weatherkotlin.data.model1.GeopositionSearch.LocationRequestBody
import com.example.weatherkotlin.data.model1.HourlyForecasts.HourlyForecasts
import com.example.weatherkotlin.data.resource.Result

interface WeatherRepository {
    suspend fun getCurrentWeather(key: String?): Result<List<CurrentConditions>?>
    suspend fun getCityFromLocation(locationRequestBody: LocationRequestBody?): Result<CityResponse?>
    suspend fun getCity(q: String?): Result<List<CitySearch>?>
    suspend fun getHourlyForecast(key: String?): Result<List<HourlyForecasts>?>
    suspend fun getFiveDayForecast(key: String?): Result<FiveDayForecast?>


}