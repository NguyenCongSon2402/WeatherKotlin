package com.example.weatherkotlin.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherkotlin.data.model1.CitySearch.CurrentConditions
import com.example.weatherkotlin.data.repository.WeatherRepository
import com.example.weatherkotlin.data.repository.WeatherRepositoryImp
import com.example.weatherkotlin.data.resource.Result
import kotlinx.coroutines.launch
import java.lang.Exception

class MainActivityViewModel(
    private val repository: WeatherRepository = WeatherRepositoryImp()
) : ViewModel() {
    private val _currentWeatherLiveData = MutableLiveData<List<CurrentConditions>?>()
    val currentWeatherLiveData: LiveData<List<CurrentConditions>?> = _currentWeatherLiveData

    private val _error = MutableLiveData<Exception?>()
    val error: LiveData<Exception?> = _error
    fun loadCurrentWeather() {
        viewModelScope.launch {
            val result = repository.loadCurrentWeather()
            if (result is Result.Success) {
                _currentWeatherLiveData.postValue(result.data)
                _error.postValue(null)
            } else if (result is Result.Error) {
                _currentWeatherLiveData.postValue(null)
                _error.postValue(result.exception)
            }
        }
    }
}