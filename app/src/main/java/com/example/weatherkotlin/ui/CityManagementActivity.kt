package com.example.weatherkotlin.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherkotlin.R
import com.example.weatherkotlin.data.model1.WeatherCityData.WeatherCityData
import com.example.weatherkotlin.ui.adapter.CityManagementAdapter
import com.example.weatherkotlin.ui.interfaces.RecyclerViewItemClickListener
import com.google.android.material.floatingactionbutton.FloatingActionButton

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.ArrayList

class CityManagementActivity : AppCompatActivity(), RecyclerViewItemClickListener {


    private var isEditMode = false
    private lateinit var txt_SelectedItem: TextView
    private lateinit var txt_management: TextView
    private lateinit var editTextSearch: AutoCompleteTextView
    private lateinit var floatButton: FloatingActionButton
    private lateinit var btn_Back: ImageView
    private lateinit var btn_Cancel: ImageView
    private lateinit var btn_selectAll: ImageView
    private lateinit var btn_Delete: ImageView
    private lateinit var layout_delete: LinearLayout
    private lateinit var adapter: CityManagementAdapter
    private lateinit var autoCompleteAdapter: ArrayAdapter<String>
    private val citySuggestions = mutableListOf<String>()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.city_management_layout)

        txt_SelectedItem = findViewById(R.id.txt_SelectedItem)
        txt_management = findViewById(R.id.txt_management)
        recyclerView = findViewById(R.id.RVCity)
        btn_Back = findViewById(R.id.btn_Back)
        btn_Cancel = findViewById(R.id.btn_Cancel)
        btn_selectAll = findViewById(R.id.btn_selectAll)
        btn_Delete = findViewById(R.id.btn_Delete)
        floatButton = findViewById(R.id.floatButton)
        layout_delete = findViewById(R.id.layout_delete)
        val itemLongClickListener: RecyclerViewItemClickListener = this
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        val data = readDataFromSharedPreferences()
        val CitiesList = getCitiesListFromSharedPreferences()

        adapter = CityManagementAdapter(this,itemLongClickListener, txt_SelectedItem, data, CitiesList)
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
        floatButton.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
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


    private fun getCitiesListFromSharedPreferences(): ArrayList<String> {
        val sharedPreferences = getSharedPreferences("City", Context.MODE_PRIVATE)
        val citiesListJson = sharedPreferences.getString("citiesList", "")

        // Nếu chuỗi JSON không rỗng, chuyển đổi thành danh sách ArrayList<String> sử dụng Gson
        if (!citiesListJson.isNullOrEmpty()) {
            val gson = Gson()
            val citiesListType = object : TypeToken<ArrayList<String>>() {}.type
            return gson.fromJson(citiesListJson, citiesListType)
        }

        // Trả về danh sách trống nếu không có dữ liệu hoặc xảy ra lỗi
        return ArrayList()
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

    override fun onRestart() {
        super.onRestart()
        val newData = readDataFromSharedPreferences()
        val newCitiesList = getCitiesListFromSharedPreferences()
        adapter.updateCitiesAndData(newCitiesList, newData)
    }



}