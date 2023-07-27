package com.example.weatherkotlin.data.resource

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    // Phương thức getInstance() trả về một Retrofit client đã được cấu hình với baseUrl và GsonConverterFactory.
    //http://dataservice.accuweather.com/currentconditions/v1/355085?apikey=XfZd5b8QoXAXCkH6o5NQ2Fu4jrM0zosQ&language=vi
    private const val BASE_URL = "https://api.openweathermap.org/"
    private const val BASE_URL1 = "https://dataservice.accuweather.com/currentconditions/v1/"
    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL1)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}