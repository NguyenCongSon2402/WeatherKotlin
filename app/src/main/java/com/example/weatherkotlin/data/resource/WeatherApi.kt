package com.example.weatherkotlin.data.resource

import com.example.weatherkotlin.data.model1.CitySearch.CurrentConditions
import retrofit2.Response
import retrofit2.http.GET

interface WeatherApi {
    // api   https://api.openweathermap.org/data/2.5/weather?q=H%C3%A0%20N%E1%BB%99i&appid=96ebc3869650044c64bcabadf7b3dc03
    //http://dataservice.accuweather.com/currentconditions/v1/355085?apikey=XfZd5b8QoXAXCkH6o5NQ2Fu4jrM0zosQ&language=vi

    // Geoposition Search
    //https://dataservice.accuweather.com/locations/v1/cities/geoposition/search?apikey=XfZd5b8QoXAXCkH6o5NQ2Fu4jrM0zosQ&q=20.429,106.175&language=vi&details=false&toplevel=true
    @GET("353412?apikey=XfZd5b8QoXAXCkH6o5NQ2Fu4jrM0zosQ&language=vi")
    suspend fun getCurrentWeathers(): Response<List<CurrentConditions>?>

}