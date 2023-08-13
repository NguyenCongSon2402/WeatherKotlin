package com.example.weatherkotlin.ui.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherkotlin.R
import com.example.weatherkotlin.data.model1.FiveDayForecast.FiveDayForecast
import com.example.weatherkotlin.ui.WeatherForecastActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class WeatherForecast5DaysAdapter(
    private val data: FiveDayForecast,
    private val context: WeatherForecastActivity
) :
    RecyclerView.Adapter<WeatherForecast5DaysAdapter.ViewHolder1>() {
    private var img_weather1 = arrayOf(
        R.drawable.rain_black,
        R.drawable.rain_black,
        R.drawable.rain_black,
        R.drawable.rain_black,
        R.drawable.rain_black
    )

    // Khai báo biến toàn cục cho dayOfWeekList và dateMonthList
    private var dayOfWeekList: List<String> = emptyList()
    private var dateMonthList: List<String> = emptyList()
    private var iconList: List<Int> = emptyList()

    init {
        updateDataLists()
    }

    private fun updateDataLists() {
        // Cập nhật giá trị cho dayOfWeekList và dateMonthList từ dataList
        val dateList: List<String>? = data.DailyForecasts?.mapNotNull { it.Date }
        val icList: List<String>? = data.DailyForecasts?.mapNotNull { it.Day?.Icon.toString() }

        if (icList != null) {
            iconList = icList.map { getIcon(it, context) }
        }
        if (dateList != null) {
            dayOfWeekList = dateList.map { getDayOfWeekFromDateTime(it) }
            dateMonthList = dateList.map { convertDateFormat(it) }
        }
    }

    private fun getIcon(it: String, context: WeatherForecastActivity): Int {
        val iconName1 = "s_$it"
        val resourceId1 =
            context.resources.getIdentifier(iconName1, "drawable", context.getPackageName())
        if (resourceId1 != 0) {
            return resourceId1
        }
        return R.drawable.s_1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder1 {
        val adapterLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false);
        return ViewHolder1(adapterLayout);
    }

    override fun onBindViewHolder(holder: ViewHolder1, position: Int) {


        holder.txt_thu.text = dayOfWeekList[position]
        holder.txt_ngay.text = dateMonthList[position]
        holder.txt_TempC.text = getTemperatureInCelsius(position)
        holder.img_weather.setImageResource(iconList[position])
        holder.icon.setOnClickListener {
            val url = data.DailyForecasts[position].MobileLink.toString()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        if (data != null)
            return data.DailyForecasts.size
        return 0
    }

    inner class ViewHolder1(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt_thu: TextView = itemView.findViewById(R.id.txt_thu)
        var txt_ngay: TextView = itemView.findViewById(R.id.txt_ngay)
        var txt_TempC: TextView = itemView.findViewById(R.id.txt_TempC)
        var icon: ImageView = itemView.findViewById(R.id.icon)
        var img_weather: ImageView = itemView.findViewById(R.id.img_weather)


    }

    private fun getSetting(key: String): String {
        val sharedPreferences =
            context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, "") ?: ""
    }


    fun getDayOfWeekFromDateTime(dateTimeString: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
        val date = dateFormat.parse(dateTimeString)
        val calendar = Calendar.getInstance().apply {
            time = date
        }
        val dayOfWeek =
            calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
        return dayOfWeek
    }

    fun convertDateFormat(inputDateString: String): String {
        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
        val date = inputDateFormat.parse(inputDateString)
        return outputDateFormat.format(date)
    }

    fun fahrenheitToCelsius(fahrenheit: Int?): Int? {
        if (fahrenheit != null) {
            return (fahrenheit - 32) * 5 / 9
        }
        return null
    }

    fun getTemperatureInCelsius(position: Int): String {
        val selectedTemp = getSetting("selectedTemp")
        val selectedSpeed = getSetting("selectedSpeed")
        if (selectedTemp.equals("°C")) {
            val maxTempF = data.DailyForecasts[position]?.Temperature?.Maximum?.Value
            val minTempF = data.DailyForecasts[position]?.Temperature?.Minimum?.Value
            val maxTempC = fahrenheitToCelsius(maxTempF)
            val minTempC = fahrenheitToCelsius(minTempF)
            return "$minTempC°/$maxTempC°"
        }
        val maxTempF = data.DailyForecasts[position]?.Temperature?.Maximum?.Value
        val minTempF = data.DailyForecasts[position]?.Temperature?.Minimum?.Value

        return "$minTempF°/$maxTempF°"
    }


}