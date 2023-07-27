package com.example.weatherkotlin.data.repository

import com.example.weatherkotlin.data.model1.CitySearch.CurrentConditions
import com.example.weatherkotlin.data.resource.Result
import com.example.weatherkotlin.data.resource.RetrofitHelper
import com.example.weatherkotlin.data.resource.WeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class WeatherRepositoryImp : WeatherRepository {
    override suspend fun loadCurrentWeather(): Result<List<CurrentConditions>?> {
        return withContext(Dispatchers.IO) {
            // Tạo một đối tượng API để gọi dự báo thời tiết
            val weatherAPI: WeatherApi =
                RetrofitHelper.getInstance().create(WeatherApi::class.java)
            // Gọi API để lấy thông tin thời tiết hiện tại
            val response = weatherAPI.getCurrentWeathers()
            if (response.isSuccessful)
            // Nếu thành công, trả về kết quả Result.Success với dữ liệu thời tiết đã deserialize từ JSON
                Result.Success(response.body()!!)
            else {
                // Nếu không thành công, trả về kết quả Result.Error với thông báo lỗi từ response
                Result.Error(Exception(response.message()))
            }
        }
    }
}