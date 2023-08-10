package com.example.weatherkotlin.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherkotlin.data.model1.CitySearch.CitySearch
import com.example.weatherkotlin.data.model1.CurrentConditions.CurrentConditions
import com.example.weatherkotlin.data.model1.FiveDayForecast.FiveDayForecast
import com.example.weatherkotlin.data.model1.HourlyForecasts.HourlyForecasts
import com.example.weatherkotlin.data.repository.WeatherRepository
import com.example.weatherkotlin.data.repository.WeatherRepositoryImp
import com.example.weatherkotlin.data.resource.Result
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.lang.Exception

class CityViewModel(
    private val repository: WeatherRepository = WeatherRepositoryImp()
) : ViewModel() {
    private val _currentWeatherLiveData2 = MutableLiveData<List<CurrentConditions>?>()
    val currentWeatherLiveData2: LiveData<List<CurrentConditions>?> = _currentWeatherLiveData2
    private val _errorcurrentWeather = MutableLiveData<Exception?>()
    val errorcurrentWeather: LiveData<Exception?> = _errorcurrentWeather

    private val _errorcityInfo = MutableLiveData<Exception?>()
    val errorcityInfo: LiveData<Exception?> = _errorcityInfo
    private val _errorTry2 = MutableLiveData<String?>()
    val errorTry2: LiveData<String?> = _errorTry2
    private val _cityInfo2 = MutableLiveData<List<CitySearch>?>()
    val cityInfo2: LiveData<List<CitySearch>?> = _cityInfo2


    private val _FiveDayForecastLiveData2 = MutableLiveData<FiveDayForecast?>()
    val FiveDayForecastLiveData2: LiveData<FiveDayForecast?> = _FiveDayForecastLiveData2

    private val _errorFiveDayForecast = MutableLiveData<Exception?>()
    val errorFiveDayForecast: LiveData<Exception?> = _errorFiveDayForecast


    private val _HourlyForecastLiveData2 = MutableLiveData<List<HourlyForecasts>?>()
    val HourlyForecast2: LiveData<List<HourlyForecasts>?> = _HourlyForecastLiveData2

    private val _errorHourlyForecast = MutableLiveData<Exception?>()
    val errorHourlyForecast: LiveData<Exception?> = _errorHourlyForecast
    fun loadCityWeather(q: String?) {
        viewModelScope.launch {
            try {
                if (q != null) {

                    val resultCity = repository.getCity(q)
                    // Kiểm tra kết quả từ hàm repository.getCityFromLocation
                    if (resultCity is Result.Success) {
                        _cityInfo2.postValue(resultCity.data)
                        val key = resultCity.data?.get(0)?.Key.toString()
                        Log.d("SUSSEC", key)

                        // Tiếp tục lấy dữ liệu thời tiết với đầu vào từ hàm repository.getCityFromLocation
                        // Sử dụng locationRequestBody để lấy thông tin vị trí của thành phố
                        if (!key.isNullOrEmpty()) {
                            Log.d("SUSSEC1", key)
                            val result3 =
                                coroutineScope { async { repository.getFiveDayForecast(key) } }

                            // Gọi 3 API cùng lúc
                            val result1 =
                                coroutineScope { async { repository.getCurrentWeather(key) } }

                            val result2 =
                                coroutineScope { async { repository.getHourlyForecast(key) } }

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
                                _currentWeatherLiveData2.postValue(resultCurrentWeather.data)
                                _errorcurrentWeather.postValue(null)

                                _HourlyForecastLiveData2.postValue(resultHourlyForecasts.data)
                                _errorHourlyForecast.postValue(null)

                                _FiveDayForecastLiveData2.postValue(resultFiveDayForecasts.data)
                                _errorFiveDayForecast.postValue(null)

                            } else if (resultCurrentWeather is Result.Error
                                && resultHourlyForecasts is Result.Error
                                && resultFiveDayForecasts is Result.Error
                            ) {
                                _currentWeatherLiveData2.postValue(null)
                                _errorcurrentWeather.postValue(resultCurrentWeather.exception)

                                _HourlyForecastLiveData2.postValue(null)
                                _errorHourlyForecast.postValue(resultHourlyForecasts.exception)

                                _FiveDayForecastLiveData2.postValue(null)
                                _errorFiveDayForecast.postValue(resultFiveDayForecasts.exception)
                                Log.d("ERROR2", resultHourlyForecasts.exception.toString())
                            }
                        }

                    } else if (resultCity is Result.Error) {
                        _cityInfo2.postValue(null)
                        _errorcityInfo.postValue(resultCity.exception)
                    }
                }
            } catch (e: Exception) {
                _errorTry2.postValue("Lỗi khi tìm kiếm thành phố")
            }
        }
    }
}