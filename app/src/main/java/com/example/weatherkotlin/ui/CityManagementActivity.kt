package com.example.weatherkotlin.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherkotlin.R
import com.example.weatherkotlin.data.model1.CurrentConditions.CurrentConditions
import com.example.weatherkotlin.data.model1.FiveDayForecast.FiveDayForecast
import com.example.weatherkotlin.data.model1.HourlyForecasts.HourlyForecasts
import com.example.weatherkotlin.data.model1.WeatherCityData.WeatherCityData
import com.example.weatherkotlin.ui.adapter.CityManagementAdapter
import com.example.weatherkotlin.ui.interfaces.RecyclerViewItemClickListener
import com.example.weatherkotlin.ui.viewmodel.CityViewModel
import com.example.weatherkotlin.ui.viewmodel.MainActivityViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.Exception
import java.util.ArrayList

class CityManagementActivity : AppCompatActivity(), RecyclerViewItemClickListener {

    private var isEditMode = false
    private lateinit var txt_SelectedItem: TextView
    private lateinit var txt_management: TextView
    private lateinit var editTextSearch: AutoCompleteTextView
    private lateinit var btn_Back: ImageView
    private lateinit var btn_Cancel: ImageView
    private lateinit var btn_selectAll: ImageView
    private lateinit var btn_Delete: ImageView
    private lateinit var layout_delete: LinearLayout
    private lateinit var adapter: CityManagementAdapter
    private lateinit var autoCompleteAdapter: ArrayAdapter<String>
    private lateinit var cityViewModel: CityViewModel
    private val citySuggestions = mutableListOf<String>()
    private val COUNTRIES = arrayOf(
        "Belgium", "France", "Italy", "Germany", "Spain", "Hanoi",
        "Ho Chi Minh City",
        "Tokyo",
        "New York",
        "London",
        "Paris",
        "Sydney",
        "Berlin",
        "Moscow"
    )

    private lateinit var recyclerView: RecyclerView
    private val selectedItems = mutableSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.city_management_layout)

        txt_SelectedItem = findViewById(R.id.txt_SelectedItem)
        txt_management = findViewById(R.id.txt_management)
        editTextSearch = findViewById(R.id.editTextSearch)
        recyclerView = findViewById(R.id.RVCity)
        btn_Back = findViewById(R.id.btn_Back)
        btn_Cancel = findViewById(R.id.btn_Cancel)
        btn_selectAll = findViewById(R.id.btn_selectAll)
        btn_Delete = findViewById(R.id.btn_Delete)
        layout_delete = findViewById(R.id.layout_delete)
        val itemLongClickListener: RecyclerViewItemClickListener = this
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        val data = readDataFromSharedPreferences()
        val CitiesList = getCitiesListFromSharedPreferences()

        adapter = CityManagementAdapter(itemLongClickListener, txt_SelectedItem, data, CitiesList)
        recyclerView.adapter = adapter
        btn_Back.setOnClickListener {
            onBackPressed()
        }
// Xử lý sự kiện khi nhấn nút Cancel (chuyển về chế độ xem thông thường)
        btn_Cancel.setOnClickListener {
            // Ẩn các view đi
            btn_Back.visibility = View.VISIBLE
            txt_management.visibility = View.VISIBLE
            btn_Cancel.visibility = View.GONE
            btn_selectAll.visibility = View.GONE
            layout_delete.visibility = View.GONE
            layout_delete.visibility = View.GONE
            txt_SelectedItem.visibility = View.GONE

            isEditMode = false
            adapter.clearSelectedItems()
            // Cập nhật trạng thái của adapter
            adapter.setEditMode(false)
        }

        // Xử lý sự kiện khi nhấn nút Select All (chọn tất cả checkbox)
        btn_selectAll.setOnClickListener {
            if (adapter.getSelectedItems().size == adapter.itemCount) {
                // Đã chọn tất cả, bỏ chọn tất cả checkbox
                adapter.clearSelectedItems()
            } else {
                // Chưa chọn tất cả, chọn tất cả checkbox
                val allItems = mutableListOf<Int>()
                for (i in 0 until adapter.itemCount) {
                    allItems.add(i)
                }
                adapter.setSelectedItems(allItems)
            }
        }
        // Xử lý sự kiện khi nhấn nút Delete (xóa các item đã chọn)
        btn_Delete.setOnClickListener {
            adapter.deleteItemSelected()
            btn_Cancel.visibility = View.GONE
            btn_selectAll.visibility = View.GONE
            layout_delete.visibility = View.GONE
            txt_SelectedItem.visibility = View.GONE
            btn_Back.visibility = View.VISIBLE
            txt_management.visibility = View.VISIBLE
            isEditMode = false
        }
        // Khởi tạo Adapter và gán cho AutoCompleteTextView
        //val suggestions = mutableListOf<String>() // Danh sách gợi ý tìm kiếm
        autoCompleteAdapter =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, COUNTRIES)
        editTextSearch.setAdapter(autoCompleteAdapter)

        editTextSearch.setOnEditorActionListener { _, acTionId, _ ->
            if (acTionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchQuery = editTextSearch.text.trim().toString()
                cityViewModel = ViewModelProvider(this).get(CityViewModel::class.java)
                cityViewModel.loadCityWeather(searchQuery)
                setupObserver()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun readDataFromSharedPreferences(): ArrayList<WeatherCityData>? {
        // lấy dữ liệu JSON string từ SharedPreferences
        val sharedPreferences = getSharedPreferences("WeatherData", Context.MODE_PRIVATE)
        val dataListJson = sharedPreferences.getString("DataListJson", "")
        if (dataListJson != null && dataListJson.isNotEmpty()) {
            val gson = Gson()
            val dataListType = object : TypeToken<ArrayList<WeatherCityData>>() {}.type
            val dataList: ArrayList<WeatherCityData> = gson.fromJson(dataListJson, dataListType)
            return dataList
        }
        return null
    }

    private fun setupObserver() = try {
        var currentWeather: List<CurrentConditions>? = null
        var hourlyForecast: List<HourlyForecasts>? = null
        var fiveDayForecast: FiveDayForecast? = null

        cityViewModel.cityInfo.observe(this) {
            if (it == null) {
                Toast.makeText(this, cityViewModel.errorTry.value?.toString()!!, Toast.LENGTH_LONG)
                    .show()
            } else {

                val citiesList = ArrayList<String>()
                citiesList.add(it.get(0)?.LocalizedName.toString())
                updateCitiesList(it[0]?.LocalizedName.toString())
                citySuggestions.add(it.get(0)?.LocalizedName.toString())


            }
        }
        cityViewModel.currentWeatherLiveData.observe(this) {
            if (it == null) {
                Toast.makeText(this, cityViewModel.error.value?.message!!, Toast.LENGTH_LONG)
                    .show()

            } else {
                currentWeather = it
                Log.e("Đã cập nhập xong1", it[0].Temperature?.Metric?.Value.toString())
            }

        }


        cityViewModel.HourlyForecast.observe(this) {
            if (it == null) {
                Toast.makeText(this, cityViewModel.error.value?.message!!, Toast.LENGTH_LONG)
                    .show()
            } else {
                hourlyForecast = it
                Log.e("Đã cập nhập xong2", it[0].Temperature?.Value.toString())
                checkAndRenderData(currentWeather, hourlyForecast, fiveDayForecast)
            }

        }
        cityViewModel.FiveDayForecastLiveData.observe(this) {
            if (it == null) {
                Toast.makeText(this, cityViewModel.error.value?.message!!, Toast.LENGTH_LONG)
                    .show()
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
            val weatherCityData = WeatherCityData(currentWeather, hourlyForecast, fiveDayForecast)
//            dataList.add(weatherCityData)
//            saveDataToSharedPreferences(dataList)
//            renderList(dataList)
        }
    }

    private fun getCitiesListFromSharedPreferences(): ArrayList<String> {
        val sharedPreferences = getSharedPreferences("City", Context.MODE_PRIVATE)
        val citiesSet = sharedPreferences.getStringSet("citiesList", emptySet())
        return ArrayList(citiesSet)
    }

    fun updateCitiesList(newCityName: String) {
        // Lấy danh sách thành phố từ SharedPreferences
        val sharedPreferences = getSharedPreferences("City", Context.MODE_PRIVATE)
        val citiesList =
            sharedPreferences.getStringSet("citiesList", mutableSetOf())?.toMutableSet()

        // Thêm giá trị mới vào danh sách
        citiesList?.add(newCityName)

        // Lưu lại danh sách mới vào SharedPreferences
        sharedPreferences.edit()
            .putStringSet("citiesList", citiesList)
            .apply()
    }


    private fun performAutocompleteSearch(searchQuery: String) {
        citySuggestions.clear()
        Log.e("searchQuery", searchQuery)

        // Fake dữ liệu tên thành phố - đây chỉ là ví dụ, bạn có thể sử dụng dữ liệu thực từ API
        val fakeCities = listOf(
            "Hanoi",
            "Ho Chi Minh City",
            "Tokyo",
            "New York",
            "London",
            "Paris",
            "Sydney",
            "Berlin",
            "Moscow"
        )

        // Tìm kiếm các thành phố khớp với query và thêm vào danh sách gợi ý tìm kiếm
        for (city in fakeCities) {
            if (city.startsWith(searchQuery, ignoreCase = true)) {
                citySuggestions.add(city)
                Log.e("citySuggestions", citySuggestions.toString())
            }
        }

    }


    override fun onItemLongClick(position: Int) {
        // Xử lý hiển thị những thứ bị ẩn khi nhấn giữ lâu trên item ở vị trí position
        btn_Back.visibility = View.GONE
        txt_management.visibility = View.GONE
        btn_Cancel.visibility = View.VISIBLE
        btn_selectAll.visibility = View.VISIBLE
        layout_delete.visibility = View.VISIBLE
        txt_SelectedItem.visibility = View.VISIBLE

        // Đánh dấu adapter trong chế độ chỉnh sửa để hiển thị checkbox
        adapter.setEditMode(true)
    }

    fun showItemSelected(s: String) {
        txt_SelectedItem.text = s

    }
}