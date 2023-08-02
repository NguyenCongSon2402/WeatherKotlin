package com.example.weatherkotlin.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherkotlin.data.model1.CurrentConditions.CurrentConditions
import com.example.weatherkotlin.data.model1.FiveDayForecast.FiveDayForecast
import com.example.weatherkotlin.data.model1.GeopositionSearch.CityResponse
import com.example.weatherkotlin.data.model1.GeopositionSearch.LocationRequestBody
import com.example.weatherkotlin.data.model1.HourlyForecasts.HourlyForecasts
import com.example.weatherkotlin.data.repository.WeatherRepository
import com.example.weatherkotlin.data.repository.WeatherRepositoryImp
import com.example.weatherkotlin.data.resource.Result
import com.google.gson.Gson
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.lang.Exception

class MainActivityViewModel(
    private val repository: WeatherRepository = WeatherRepositoryImp()
) : ViewModel() {
    private val _currentWeatherLiveData = MutableLiveData<List<CurrentConditions>?>()
    val currentWeatherLiveData: LiveData<List<CurrentConditions>?> = _currentWeatherLiveData
    private val _error = MutableLiveData<Exception?>()
    val error: LiveData<Exception?> = _error

    private val _errorTry = MutableLiveData<String?>()
    val errorTry: LiveData<String?> = _errorTry
    private val _isLoad = MutableLiveData<Boolean>()
    val isLoad: LiveData<Boolean> = _isLoad
    private val _cityInfo = MutableLiveData<CityResponse?>()
    val cityInfo: LiveData<CityResponse?> = _cityInfo


    private val _FiveDayForecastLiveData = MutableLiveData<FiveDayForecast?>()
    val FiveDayForecastLiveData: LiveData<FiveDayForecast?> = _FiveDayForecastLiveData

    private val _error1 = MutableLiveData<Exception?>()
    val error1: LiveData<Exception?> = _error1


    private val _HourlyForecastLiveData = MutableLiveData<List<HourlyForecasts>?>()
    val HourlyForecast: LiveData<List<HourlyForecasts>?> = _HourlyForecastLiveData

    private val _error2 = MutableLiveData<Exception?>()
    val error2: LiveData<Exception?> = _error2
    fun loadCurrentWeather(locationRequestBody: LocationRequestBody?) {
        viewModelScope.launch {
            try {
                Log.e(
                    "Location1",
                    locationRequestBody.toString()
                )

                val resultCity1 = repository.getCityFromLocation(locationRequestBody)
                // Kiểm tra kết quả từ hàm repository.getCityFromLocation
                if (resultCity1 is Result.Success) {
                    // Lấy kết quả thành công từ hàm repository.getCityFromLocation
                    // Lưu trữ thông tin cityInfo trong LiveData _cityInfo
                    _cityInfo.postValue(resultCity1.data)
                    val key = resultCity1.data?.Key.toString()
                    val nameCity=resultCity1.data?.LocalizedName

                    Log.d("SUSSEC", resultCity1.data?.Key.toString())

                    // Tiếp tục lấy dữ liệu thời tiết với đầu vào từ hàm repository.getCityFromLocation
                    // Sử dụng locationRequestBody để lấy thông tin vị trí của thành phố
                    if (key != null) {
                        Log.d("SUSSEC1", key)
                        val result3 =
                            coroutineScope { async { repository.getFiveDayForecast(key) } }

                        // Gọi 3 API cùng lúc
                        val result1 = coroutineScope { async { repository.getCurrentWeather(key) } }

                        val result2 = coroutineScope { async { repository.getHourlyForecast(key) } }

                        // Đợi 3 kết quả trả về
                        val resultCurrentWeather = result1.await()
                        val resultHourlyForecasts = result2.await()
                        val resultFiveDayForecasts = result3.await()

                        if (resultCurrentWeather is Result.Success
                            && resultHourlyForecasts is Result.Success
                            && resultFiveDayForecasts is Result.Success
                        ) {
                            Log.d(
                                "SUCCES2",
                                resultCurrentWeather.data?.get(0)?.Temperature.toString()
                            )
                            Log.d(
                                "SUCCES2",
                                resultHourlyForecasts.data?.get(0)?.DateTime.toString()
                            )
                            Log.d(
                                "SUCCES2",
                                resultFiveDayForecasts.data?.Headline?.Text.toString()
                            )
                            _currentWeatherLiveData.postValue(resultCurrentWeather.data)
                            _error.postValue(null)

                            _HourlyForecastLiveData.postValue(resultHourlyForecasts.data)
                            _error1.postValue(null)

                            _FiveDayForecastLiveData.postValue(resultFiveDayForecasts.data)
                            _error2.postValue(null)

                            _isLoad.postValue(true)
                        } else if (resultCurrentWeather is Result.Error
                            && resultHourlyForecasts is Result.Error
                            && resultFiveDayForecasts is Result.Error
                        ) {
                            _currentWeatherLiveData.postValue(null)
                            _error.postValue(resultCurrentWeather.exception)

                            _HourlyForecastLiveData.postValue(null)
                            _error1.postValue(resultHourlyForecasts.exception)

                            _FiveDayForecastLiveData.postValue(null)
                            _error2.postValue(resultFiveDayForecasts.exception)
                            _isLoad.postValue(false)
                            Log.d("ERROR2", resultHourlyForecasts.exception.toString())
                        }
                    }

                } else if (resultCity1 is Result.Error) {
                    _cityInfo.postValue(null)
                    Log.d("ERROR", "LOI")
                    _errorTry.postValue(resultCity1.exception.toString())
                }
            } catch (e: Exception) {
                _errorTry.postValue("Lỗi khi lấy vị trí")
            }
        }
    }
}