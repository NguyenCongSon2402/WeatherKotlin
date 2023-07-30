package com.example.weatherkotlin.data.repository

import android.util.Log
import com.example.weatherkotlin.data.model1.CitySearch.CurrentConditions
import com.example.weatherkotlin.data.model1.FiveDayForecast.FiveDayForecast
import com.example.weatherkotlin.data.model1.GeopositionSearch.CityResponse
import com.example.weatherkotlin.data.model1.GeopositionSearch.LocationRequestBody
import com.example.weatherkotlin.data.model1.HourlyForecasts.HourlyForecasts
import com.example.weatherkotlin.data.resource.Result
import com.example.weatherkotlin.data.resource.RetrofitHelper
import com.example.weatherkotlin.data.resource.WeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class WeatherRepositoryImp : WeatherRepository {
    val apiKey = "eAqGlkcrfWwMVlKJi6IuFnh5CqtA7GAZ"
    override suspend fun getCurrentWeather(key: String?): Result<List<CurrentConditions>?> {
        return withContext(Dispatchers.IO) {
            // Tạo một đối tượng API để gọi dự báo thời tiết
            val weatherAPI: WeatherApi =
                RetrofitHelper.getInstance().create(WeatherApi::class.java)
            Log.d("ERROR1","getCurrentWeather" )
            // Gọi API để lấy thông tin thời tiết hiện tại
            val language = "vi"
            val ikey = "353412"
            val response = weatherAPI.getCurrentWeathers(key, apiKey, language)
            if (response.isSuccessful) {
                Log.d("ERROR1","SUCCESgetCurrentWeather" )
                // Nếu thành công, trả về kết quả Result.Success với dữ liệu thời tiết đã deserialize từ JSON
                Result.Success(response.body()!!)
            }
            else {
                Log.d("ERROR1","ERRORgetCurrentWeather" )
                // Nếu không thành công, trả về kết quả Result.Error với thông báo lỗi từ response
                Result.Error(Exception(response.message()))
            }
        }
    }

    override suspend fun getCityFromLocation(locationRequestBody: LocationRequestBody?): Result<CityResponse?> {
        return withContext(Dispatchers.IO) {
            Log.e("Location3", locationRequestBody.toString())
            // Tạo một đối tượng API để gọi dự báo thời tiết
            val weatherAPI: WeatherApi = RetrofitHelper.getInstance().create(WeatherApi::class.java)
            val qParam =
                locationRequestBody?.latitude.toString() + "," + locationRequestBody?.longitude.toString()
            Log.e("Q", qParam)
            val key = "20.999,105.7" // Ví dụ cho vĩ độ 20.429 và kinh độ 106.175
            val language = "vi"
            val details = false
            val toplevel = true
            val response =
                weatherAPI.getCityFromLocations(apiKey, qParam, language, details, toplevel)
            if (response.isSuccessful) {
                // Nếu thành công trả về kết quả
                Log.e("SUCCES11", response.body().toString())
                Result.Success(response.body())
            } else {
                // Nếu không thành công, trả về lỗi
                Log.e("ERROR11", response.message())
                Result.Error(Exception(response.message()))
            }
        }
    }

    override suspend fun getHourlyForecast(key: String?): Result<List<HourlyForecasts>?> {
        return withContext(Dispatchers.IO) {
            // Tạo một đối tượng API để gọi dự báo thời tiết
            Log.d("ERROR2","getHourlyForecast" )
            val weatherAPI: WeatherApi =
                RetrofitHelper.getInstance().create(WeatherApi::class.java)
            // Gọi API để lấy thông tin thời tiết hiện tại
            val language = "vi"
            val response = weatherAPI.getHourlyForecasts(key, apiKey, language)
            if (response.isSuccessful) {
                Log.d("ERROR2","SUCCESgetHourlyForecast" )
                // Nếu thành công, trả về kết quả Result.Success với dữ liệu thời tiết đã deserialize từ JSON
                Result.Success(response.body()!!)
            }
            else {
                Log.d("ERROR2","ERRORgetHourlyForecast" )
                // Nếu không thành công, trả về kết quả Result.Error với thông báo lỗi từ response
                Result.Error(Exception(response.message()))
            }
        }
    }

    override suspend fun getFiveDayForecast(key: String?): Result<FiveDayForecast?> {
        return withContext(Dispatchers.IO) {
            // Tạo một đối tượng API để gọi dự báo thời tiết
            Log.d("ERROR2","getFiveDayForecast" )
            val weatherAPI: WeatherApi =
                RetrofitHelper.getInstance().create(WeatherApi::class.java)
            // Gọi API để lấy thông tin thời tiết hiện tại

            val language = "vi"
            val detail = false

            val response = weatherAPI.getFiveDayForecasts(key, apiKey, language, detail)
            if (response.isSuccessful){
                Log.d("ERROR2","SUCCESgetFiveDayForecast" )
            // Nếu thành công, trả về kết quả Result.Success với dữ liệu thời tiết đã deserialize từ JSON
                Result.Success(response.body()!!)
            }
            else {
                Log.d("ERROR2","ERRORgetFiveDayForecast" )
                // Nếu không thành công, trả về kết quả Result.Error với thông báo lỗi từ response
                Result.Error(Exception(response.message()))
            }
        }
    }


}