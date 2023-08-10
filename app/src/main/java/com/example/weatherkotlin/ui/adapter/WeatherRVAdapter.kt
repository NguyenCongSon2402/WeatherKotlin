package com.example.weatherkotlin.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherkotlin.R
import com.example.weatherkotlin.data.model1.HourlyForecasts.HourlyForecasts

class WeatherRVAdapter(
    private val hourlyForecast: List<HourlyForecasts>?,
    private val context: Context
) :
    RecyclerView.Adapter<WeatherRVAdapter.WeatherViewHolder>() {
    private var image = arrayOf(
        R.drawable.s_1,
        R.drawable.img_rain,
        R.drawable.img_rain,
        R.drawable.img_rain,
        R.drawable.img_rain
    )

    private var timeList: List<String> = emptyList()
    private var tempCList: List<Int> = emptyList()
    private var iconList: List<Int> = emptyList()

    init {
        updateDataLists()
    }

    private fun updateDataLists() {
        val dateList: List<String>? = hourlyForecast?.mapNotNull { it.DateTime }
        val tempList: List<Int>? = hourlyForecast?.mapNotNull { it.Temperature?.Value }
        val icList: List<String>? = hourlyForecast?.mapNotNull { it.WeatherIcon.toString() }
        if (dateList != null && tempList != null) {
            timeList = dateList.map { getTimeFromDateTime(it) }
            tempCList = tempList.map { fahrenheitToCelsius(it) }
        }
        if (icList != null) {
            iconList = icList.map { getIcon(it, context) }
        }
    }

    private fun getIcon(it: String, context: Context): Int {
        val iconName1 = "s_$it"
        val resourceId1 =
            context.resources.getIdentifier(iconName1, "drawable", context.getPackageName())
        if (resourceId1 != 0) {
            return resourceId1
        }
        return R.drawable.s_1
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val adapterLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.weather_rv_item, parent, false);
        return WeatherViewHolder(adapterLayout);
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.tv_time.text = timeList[position]
        holder.tv_temperature.text = tempCList[position].toString() + "°C"
        val milesPerHour = hourlyForecast?.get(position)?.Wind?.Speed?.Value ?: 0.0
        val kilometersPerHour = String.format("%.1f", milesPerHour * 1.609344)
        holder.tv_WindSpeed.text = kilometersPerHour.toString() + "Km/h"
        holder.Img_Condition.setImageResource(iconList[position])

    }

    override fun getItemCount(): Int {
        return timeList.size
    }

    inner class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_time: TextView = itemView.findViewById(R.id.tv_time);
        var tv_temperature: TextView = itemView.findViewById(R.id.tv_temperature);
        var tv_WindSpeed: TextView = itemView.findViewById(R.id.tv_WindSpeed);
        var Img_Condition: ImageView = itemView.findViewById(R.id.Img_Condition);
    }

    // Hàm chuyển đổi một chuỗi thời gian sang giờ phút
    private fun getTimeFromDateTime(dateTimeString: String?): String {
        val timeParts = dateTimeString?.split("T")?.getOrNull(1)?.split(":")
        val hour = timeParts?.getOrNull(0)
        val minute = timeParts?.getOrNull(1)

        return if (hour != null && minute != null) {
            "$hour:$minute"
        } else {
            ""
        }
    }

    private fun fahrenheitToCelsius(fahrenheit: Int?): Int {
        return fahrenheit?.let {
            val celsius = (it - 32) * 5 / 9
            celsius
        } ?: 0
    }


}