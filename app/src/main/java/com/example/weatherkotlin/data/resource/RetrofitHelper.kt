package com.example.weatherkotlin.data.resource

import android.content.Context

import com.example.weatherkotlin.data.repository.LoggingInterceptor
import com.intuit.sdp.BuildConfig
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitHelper {
    // Phương thức getInstance() trả về một Retrofit client đã được cấu hình với baseUrl và GsonConverterFactory.
    //https://dataservice.accuweather.com/currentconditions/v1/355085?apikey=XfZd5b8QoXAXCkH6o5NQ2Fu4jrM0zosQ&language=vi
    private const val BASE_URL1 = "https://dataservice.accuweather.com/"
    private val loggingInterceptor = LoggingInterceptor()

    val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // Thêm Interceptor vào OkHttpClient
        .connectTimeout(30, TimeUnit.SECONDS) // Thời gian kết nối tối đa là 30 giây
        .readTimeout(30, TimeUnit.SECONDS) // Thời gian đọc dữ liệu tối đa là 30 giây
        .writeTimeout(30, TimeUnit.SECONDS) // Thời gian ghi dữ liệu tối đa là 30 giây
        .build()

    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL1)
            .client(okHttpClient) // Sử dụng OkHttpClient đã cấu hình
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }



}