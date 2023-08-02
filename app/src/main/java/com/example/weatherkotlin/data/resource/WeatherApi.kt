package com.example.weatherkotlin.data.resource

import com.example.weatherkotlin.data.model1.CitySearch.CitySearch
import com.example.weatherkotlin.data.model1.CurrentConditions.CurrentConditions
import com.example.weatherkotlin.data.model1.FiveDayForecast.FiveDayForecast
import com.example.weatherkotlin.data.model1.GeopositionSearch.CityResponse
import com.example.weatherkotlin.data.model1.HourlyForecasts.HourlyForecasts
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApi {
    //http://dataservice.accuweather.com/currentconditions/v1/355085?apikey=XfZd5b8QoXAXCkH6o5NQ2Fu4jrM0zosQ&language=vi

    // Geoposition Search
    //https://dataservice.accuweather.com/locations/v1/cities/geoposition/search?apikey=XfZd5b8QoXAXCkH6o5NQ2Fu4jrM0zosQ&q=20.429,106.175&language=vi&details=false&toplevel=true
//    @GET("currentconditions/v1/{key}?apikey=XfZd5b8QoXAXCkH6o5NQ2Fu4jrM0zosQ&language=vi")
//    suspend fun getCurrentWeathers(@Path("key") key: String?): Response<List<CurrentConditions>?>

    //http://dataservice.accuweather.com/currentconditions/v1/355085?apikey=XfZd5b8QoXAXCkH6o5NQ2Fu4jrM0zosQ&language=vi
    @GET("currentconditions/v1/{key}")
    suspend fun getCurrentWeathers(
        @Path("key") key: String?,
        @Query("apikey") apiKey: String,
        @Query("language") language: String
    ): Response<List<CurrentConditions>?>

    //http://dataservice.accuweather.com/forecasts/v1/hourly/12hour/355085?apikey=G4WMi1n0ABQjRvy4M6Gkh11z2Ln2O7GQ&language=vi
    @GET("forecasts/v1/hourly/12hour/{key}")
    suspend fun getHourlyForecasts(
        @Path("key") key: String?,
        @Query("apikey") apiKey: String,
        @Query("language") language: String
    ): Response<List<HourlyForecasts>?>


    // Geoposition Search POST request
    //http://dataservice.accuweather.com/locations/v1/cities/geoposition/search?apikey=XfZd5b8QoXAXCkH6o5NQ2Fu4jrM0zosQ&q=20.9889773%2C105.8652891&language=vi&details=false&toplevel=true

    //    @GET("locations/v1/cities/geoposition/search?apikey=XfZd5b8QoXAXCkH6o5NQ2Fu4jrM0zosQ&q={key}&language=vi&details=false&toplevel=true")
//    suspend fun getCityFromLocations(@Path("key") key: String?): Response<CityResponse?>
    @GET("locations/v1/cities/geoposition/search")
    suspend fun getCityFromLocations(
        @Query("apikey") apiKey: String,
        @Query("q") key: String?,
        @Query("language") language: String,
        @Query("details") details: Boolean,
        @Query("toplevel") toplevel: Boolean
    ): Response<CityResponse?>

    //http://dataservice.accuweather.com/locations/v1/cities/search?apikey=eAqGlkcrfWwMVlKJi6IuFnh5CqtA7GAZ&q=H%C3%A0%20n%E1%BB%99i&language=vi
    @GET("locations/v1/cities/search")
    suspend fun getCity(
        @Query("apikey") apiKey: String,
        @Query("q") key: String?,
        @Query("language") language: String
    ): Response<List<CitySearch>?>

    //http://dataservice.accuweather.com/forecasts/v1/daily/5day/355085?apikey=GOgW2GTASsOXbhRWHGixmVA5fYFBohAp&language=vi&details=true
    @GET("forecasts/v1/daily/5day/{key}")
    suspend fun getFiveDayForecasts(
        @Path("key") key: String?,
        @Query("apikey") apiKey: String,
        @Query("language") language: String,
        @Query("details") details: Boolean

    ): Response<FiveDayForecast?>

}