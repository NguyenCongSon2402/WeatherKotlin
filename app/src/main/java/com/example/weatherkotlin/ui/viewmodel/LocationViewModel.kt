package com.example.weatherkotlin.ui.viewmodel

import android.Manifest
import android.content.Context
import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherkotlin.data.resource.LocationUtils
import kotlinx.coroutines.*

class LocationViewModel : ViewModel() {

    val locationLiveData = MutableLiveData<Location>()

    private val locationJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + locationJob)

    fun getLastKnownLocation(context: Context) {
        coroutineScope.launch {
            // Sử dụng coroutine để lấy vị trí hiện tại của người dùng
            val location = withContext(Dispatchers.IO) {
                LocationUtils.getLastKnownLocation(context)
            }
            locationLiveData.postValue(location)
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Hủy coroutine khi ViewModel bị hủy
        locationJob.cancel()
    }
}
