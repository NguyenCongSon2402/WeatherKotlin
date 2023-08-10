package com.example.weatherkotlin.ui.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherkotlin.R
import com.example.weatherkotlin.data.model1.WeatherCityData.WeatherCityData
import com.example.weatherkotlin.ui.CityManagementActivity
import com.example.weatherkotlin.ui.MainActivity
import com.example.weatherkotlin.ui.interfaces.RecyclerViewItemClickListener
import com.google.gson.Gson
import java.lang.Exception
import java.util.ArrayList

class CityManagementAdapter(
    private val context: Context,
    private val itemLongClickListener: RecyclerViewItemClickListener,
    private val txt_SelectedItem: TextView,
    private val data: ArrayList<WeatherCityData>?,
    private val CitiesList: ArrayList<String>
) :
    RecyclerView.Adapter<CityManagementAdapter.CityHolder>() {
    private var isEditMode: Boolean = false
    private val selectedItems = mutableListOf<Int>()


    inner class CityHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt_location: TextView = itemView.findViewById(R.id.txt_location)
        var txt_temperature1: TextView = itemView.findViewById(R.id.txt_temperature1)
        var txt_temperature: TextView = itemView.findViewById(R.id.txt_temperature)
        var checkBoxDelete: CheckBox = itemView.findViewById(R.id.checkBoxDelete)

        init {
            // Xử lý sự kiện nhấn giữ lâu trên item
            itemView.setOnLongClickListener {
                itemLongClickListener.onItemLongClick(adapterPosition)
                true // Trả về 'true' để xác định rằng sự kiện đã được xử lý
            }
            itemView.setOnClickListener {
                val selectedPosition = adapterPosition
                Toast.makeText(context,selectedPosition.toString(),Toast.LENGTH_SHORT).show()
                val resultIntent = Intent()
                resultIntent.putExtra("selected_position", selectedPosition)
                Log.e("CheckSelected",resultIntent.data.toString())
                (context as? CityManagementActivity)?.setResult(Activity.RESULT_OK, resultIntent)
                // Kết thúc activity hiện tại
                (context as? CityManagementActivity)?.finish()
                true
            }

            checkBoxDelete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if (checkBoxDelete.isChecked) {
                        // Nếu checkbox được chọn, thêm vị trí vào danh sách selectedItems
                        if (!selectedItems.contains(position)) {
                            selectedItems.add(position)
                        }
                    } else {
                        // Nếu checkbox được bỏ chọn, loại bỏ vị trí khỏi danh sách selectedItems
                        selectedItems.remove(position)
                    }

                    // Hiển thị danh sách các thành phố đã chọn trực tiếp trên giao diện
                    val selectedText = "Các mục đã chọn:\n" + selectedItems.joinToString(", ")
                    txt_SelectedItem.text = selectedText
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return CityHolder(view)
    }

    override fun getItemCount(): Int {
        if (CitiesList != null)
            return CitiesList.size
        return 0
    }

    override fun onBindViewHolder(holder: CityHolder, position: Int) {
        //Log.d("city",data.toString())
        Log.d("city", CitiesList.toString())

// Gán giá trị citiesList vào holder.txt_location.text
        if (position < CitiesList.size) {
            // Tách tên thành phố từ chuỗi "name-key"
            val cityNameWithKey = CitiesList[position]
            val cityName = cityNameWithKey.substringBefore("-")
            Log.e("Check", cityName)
            holder.txt_location.text = cityName
        } else {
            // Nếu không tồn tại phần tử tại vị trí position, đặt thành chuỗi rỗng hoặc một giá trị mặc định khác
            holder.txt_location.text = ""
        }

        holder.checkBoxDelete.isChecked = selectedItems.contains(position)
        if (isEditMode) {
            holder.checkBoxDelete.visibility = View.VISIBLE
        } else {
            holder.checkBoxDelete.visibility = View.GONE
        }


    }

    fun setEditMode(isEditMode: Boolean) {
        this.isEditMode = isEditMode
        notifyDataSetChanged()
    }

    // Function to update selected item positions
    fun setSelectedItems(selectedItems: List<Int>) {
        this.selectedItems.clear()
        this.selectedItems.addAll(selectedItems)
        val selectedText = "Các mục đã chọn:\n" + selectedItems.joinToString(", ")
        txt_SelectedItem.text = selectedText
        notifyDataSetChanged()
    }

    fun clearSelectedItems() {
        selectedItems.clear()
        val selectedText = "Các mục đã chọn:\n" + selectedItems.joinToString(", ")
        txt_SelectedItem.text = selectedText
        notifyDataSetChanged()
    }

    // Function to get the list of selected item positions
    fun getSelectedItems(): List<Int> {
        return selectedItems
    }

    fun deleteItemSelected() {
        try {
            if (!selectedItems.isNullOrEmpty()) {
                //CitiesList.removeAt(position)
                selectedItems.sortedDescending()
                removeWeaherDataAtIndex(selectedItems)
                //removeCityDataAtIndex(position)
            }
            selectedItems.clear() // Xóa tất cả các phần tử đã chọn
        } catch (e: Exception) {
            e.printStackTrace()
        }
        isEditMode = false
        if (!data.isNullOrEmpty() && !CitiesList.isNullOrEmpty()) {
            saveDataToSharedPreferences(data)
            saveCitiesListToSharedPreferences(CitiesList)
        }

        // Cập nhật lại RecyclerView sau khi xóa các item đã chọn
        notifyDataSetChanged()
    }

    fun updateCitiesAndData(
        newCitiesList: ArrayList<String>,
        newData: ArrayList<WeatherCityData>?
    ) {
        CitiesList.clear()
        CitiesList.addAll(newCitiesList)
        data?.clear()
        if (newData != null) {
            data?.addAll(newData)
        }
        notifyDataSetChanged()
    }

    private fun removeWeaherDataAtIndex(indices: List<Int>) {

        if (data != null && CitiesList != null) {
            for (index in indices) {
                if (index >= 0 && index < data.size && index < CitiesList.size) {
                    data.removeAt(index)
                    CitiesList.removeAt(index)
                }
            }
            Log.e("Weather", data.size.toString())
            Log.e("City", CitiesList.size.toString())
        }

    }

    fun saveDataToSharedPreferences(weatherCityData: ArrayList<WeatherCityData>) {
        val gson = Gson()
        val dataListJson = gson.toJson(weatherCityData)
        val sharedPreferences = context.getSharedPreferences("WeatherData", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("DataListJson", dataListJson).apply()
    }

    // Hàm xoá thành phố trong danh sách và cập nhật vào SharedPreferences
    private fun removeCityDataAtIndex(position: Int) {
        if (CitiesList != null && position >= 0 && position < CitiesList.size) {
            CitiesList.removeAt(position)
            //Log.e("XOA Weather", CitiesList.removeAt(position))
            Log.e("Weather", CitiesList.size.toString())
            //saveCitiesListToSharedPreferences()
        }
    }

    // Hàm lưu danh sách thành phố vào SharedPreferences
    private fun saveCitiesListToSharedPreferences(CitiesList: ArrayList<String>) {
        val gson = Gson()
        val citiesListJson = gson.toJson(CitiesList)
        // Lưu giá trị nameCity vào SharedPreferences
        context.getSharedPreferences("City", Context.MODE_PRIVATE)
            .edit()
            .putString("citiesList", citiesListJson)
            .apply() // Lưu lại danh sách sau khi xoá phần tử
    }


}