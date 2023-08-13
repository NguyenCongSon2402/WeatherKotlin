package com.example.weatherkotlin.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherkotlin.R
import com.example.weatherkotlin.data.model1.CitySearch.CitySearch
import com.example.weatherkotlin.data.model1.CurrentConditions.CurrentConditions
import com.example.weatherkotlin.data.model1.FiveDayForecast.FiveDayForecast
import com.example.weatherkotlin.data.model1.HourlyForecasts.HourlyForecasts
import com.example.weatherkotlin.data.model1.WeatherCityData.WeatherCityData
import com.example.weatherkotlin.ui.adapter.CityAdapter
import com.example.weatherkotlin.ui.viewmodel.CityViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.Exception
import java.util.ArrayList

class SearchActivity : AppCompatActivity(){
    private lateinit var searchView: SearchView
    private lateinit var txtSearch: TextView
    private lateinit var result: TextView
    private lateinit var RVCity1: RecyclerView
    private lateinit var progressBar:ProgressBar

    private lateinit var adapter: CityAdapter
    private var searchResults: List<CitySearch> = emptyList()
    private var weatherCityData: WeatherCityData? = null

    private lateinit var  cityViewModel: CityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchView = findViewById(R.id.searchView)
        txtSearch = findViewById(R.id.txtSearch)
        result = findViewById(R.id.result)
        RVCity1 = findViewById(R.id.RVCity1)
        progressBar  = findViewById(R.id.progressBar)

        RVCity1.layoutManager = LinearLayoutManager(this)
        // Khởi tạo adapter
        cityViewModel = ViewModelProvider(this).get(CityViewModel::class.java)
        setupObserver()
        try {
            // Đăng ký sự kiện nhấn vào mục trong RecyclerView
            RVCity1.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    // Lấy view con nhận được sự kiện chạm
                    val childView = RVCity1.findChildViewUnder(e.x, e.y)
                    if (childView != null) {
                        // Lấy vị trí của mục trong RecyclerView
                        val position = rv.getChildAdapterPosition(childView)
                        if (position != RecyclerView.NO_POSITION) {
                            Log.d("Clicked Position", position.toString())
                            updateCitiesList(
                                searchResults[0]?.LocalizedName.toString()+"-"+searchResults[0]?.Key,
                                weatherCityData!!
                            )
                            onBackPressed()
                        }
                    }
                    return false
                }

                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
            })
        } catch (e: Exception) {
            e.message
        }


//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//
//                if (searchView.query.isNullOrEmpty())
//                    performSearch(searchView.query.toString())
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                TODO("Not yet implemented")
//            }
//
//        })
// Gán OnClickListener cho TextView Tìm kiếm
        txtSearch.setOnClickListener {
            searchResults= emptyList()
            adapter = CityAdapter(searchResults)
            RVCity1.adapter = adapter
            RVCity1.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE
            performSearch(searchView.query.toString())
        }

    }

    private fun performSearch(keyword: String) {
        cityViewModel.loadCityWeather(keyword)
    }

    override fun onStart() {
        super.onStart()
    }


    private fun setupObserver() = try {
        var currentWeather: List<CurrentConditions>? = null
        var hourlyForecast: List<HourlyForecasts>? = null
        var fiveDayForecast: FiveDayForecast? = null

        cityViewModel.cityInfo2.observe(this) {
            if (it == null) {
                cityViewModel.errorcityInfo.observe(this) { errorValue ->
                    errorValue?.let {
                        Toast.makeText(this, "loi $it", Toast.LENGTH_LONG).show()
                    }
                }
                searchView.isSubmitButtonEnabled = false
                progressBar.visibility = View.GONE
                RVCity1.visibility = View.GONE
            } else {

                if (it.isEmpty()) {
                    Toast.makeText(this, "Không tìm thấy thành phố", Toast.LENGTH_LONG).show()
                    searchView.isSubmitButtonEnabled = true
                    progressBar.visibility = View.GONE
                    RVCity1.visibility = View.GONE
                }
                else
                //updateCitiesList(it[0]?.LocalizedName.toString())
                    searchResults = it

            }
        }
        cityViewModel.currentWeatherLiveData2.observe(this) {
            if (it == null) {
                cityViewModel.errorcurrentWeather.observe(this) { errorValue ->
                    errorValue?.let {
                        Toast.makeText(this, "loi $it", Toast.LENGTH_LONG).show()
                    }
                }
                searchView.isSubmitButtonEnabled = false
                progressBar.visibility = View.GONE
                RVCity1.visibility = View.GONE

            } else {
                currentWeather = it
                Log.e("Đã cập nhập xong1", it[0].Temperature?.Metric?.Value.toString())
            }

        }


        cityViewModel.HourlyForecast2.observe(this) {
            if (it == null) {
                cityViewModel.errorHourlyForecast.observe(this) { errorValue ->
                    errorValue?.let {
                        Toast.makeText(this, "loi $it", Toast.LENGTH_LONG).show()
                    }
                }
                searchView.isSubmitButtonEnabled = false
                progressBar.visibility = View.GONE
                RVCity1.visibility = View.GONE
            } else {
                hourlyForecast = it
                Log.e("Đã cập nhập xong2", it[0].Temperature?.Value.toString())
                checkAndRenderData(currentWeather, hourlyForecast, fiveDayForecast)
            }

        }
        cityViewModel.FiveDayForecastLiveData2.observe(this) {
            if (it == null) {
                cityViewModel.errorFiveDayForecast.observe(this) { errorValue ->
                    errorValue?.let {
                        Toast.makeText(this, "loi $it", Toast.LENGTH_LONG).show()
                    }
                }
                searchView.isSubmitButtonEnabled = false
                progressBar.visibility = View.GONE
                RVCity1.visibility = View.GONE
            } else {
                fiveDayForecast = it
                Log.e("Đã cập nhập xong3", it.Headline?.Text.toString())
                checkAndRenderData(currentWeather, hourlyForecast, fiveDayForecast)
            }

        }

    } catch (e: Exception) {
        Toast.makeText(this, "Lỗi trong quá trình lấy data API", Toast.LENGTH_SHORT).show()
    }

    private fun checkAndRenderData(
        currentWeather: List<CurrentConditions>?,
        hourlyForecast: List<HourlyForecasts>?,
        fiveDayForecast: FiveDayForecast?
    ) {
        if (currentWeather != null && hourlyForecast != null && fiveDayForecast != null) {
            weatherCityData = WeatherCityData(currentWeather, hourlyForecast, fiveDayForecast)
            adapter.setData(searchResults)
            progressBar.visibility = View.GONE
            searchView.isSubmitButtonEnabled = true
        }
    }


    private fun updateCitiesList(newCityName: String, weatherCityData: WeatherCityData) {
        // Lấy danh sách thành phố từ SharedPreferences
        val sharedPreferences = getSharedPreferences("City", Context.MODE_PRIVATE)
        val citiesListJson = sharedPreferences.getString("citiesList", "")
        val gson = Gson()

        // Chuyển đổi chuỗi JSON thành danh sách các tên thành phố
        val citiesListType = object : TypeToken<ArrayList<String>>() {}.type
        val citiesList =
            gson.fromJson<ArrayList<String>>(citiesListJson, citiesListType) ?: ArrayList()

        if (!citiesList.contains(newCityName)) {
            // Thêm giá trị mới vào danh sách
            citiesList.add(newCityName)

            // Chuyển danh sách thành phố thành chuỗi JSON
            val updatedCitiesListJson = gson.toJson(citiesList)

            // Lưu lại danh sách mới vào SharedPreferences
            sharedPreferences.edit()
                .putString("citiesList", updatedCitiesListJson)
                .apply()

            saveDataToSharedPreferences(weatherCityData)
        }
    }


    fun saveDataToSharedPreferences(weatherCityData: WeatherCityData) {
        // Đọc dữ liệu hiện có từ SharedPreferences
        val sharedPreferences = getSharedPreferences("WeatherData", Context.MODE_PRIVATE)
        val dataListJson = sharedPreferences.getString("DataListJson", "")

        // Khởi tạo danh sách mới hoặc đọc danh sách từ dữ liệu đã có
        val existingDataList: ArrayList<WeatherCityData> = if (dataListJson.isNullOrEmpty()) {
            ArrayList()
        } else {
            val gson = Gson()
            val dataListType = object : TypeToken<ArrayList<WeatherCityData>>() {}.type
            gson.fromJson(dataListJson, dataListType)
        }

        // Thêm dữ liệu mới vào danh sách đã đọc
        existingDataList.add(weatherCityData)

        // Lưu danh sách dữ liệu mới (bao gồm cả dữ liệu cũ và mới) vào SharedPreferences
        val gson = Gson()
        val newDataListJson = gson.toJson(existingDataList)
        sharedPreferences.edit().putString("DataListJson", newDataListJson).apply()
    }



}