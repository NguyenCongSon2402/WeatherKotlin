package com.example.weatherkotlin.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.weatherkotlin.R
import com.example.weatherkotlin.data.model1.WeatherCityData.WeatherCityData
import com.example.weatherkotlin.ui.WeatherForecastActivity
import com.example.weatherkotlin.ui.adapter.WeatherRVAdapter
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Fragment1 : Fragment() {
    private lateinit var textViewData: TextView
    private lateinit var txt_temperature: TextView
    private lateinit var txt_today: TextView
    private lateinit var txt_temperature_today: TextView
    private lateinit var txt_Tomorrow: TextView
    private lateinit var txt_temperatureTomorrow: TextView
    private lateinit var txt_afterTomorrow: TextView
    private lateinit var txt_temperatureAfterTomorrow: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var btn_next: Button
    private lateinit var scrollView: ScrollView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_layout, container, false)
        textViewData = rootView.findViewById(R.id.text_fragment)
        txt_temperature = rootView.findViewById(R.id.txt_temperature)
        recyclerView = rootView.findViewById(R.id.RvWeather)
        btn_next = rootView.findViewById(R.id.btn_next)
        txt_today = rootView.findViewById(R.id.txt_today)
        txt_temperature_today = rootView.findViewById(R.id.txt_temperature_today)
        txt_Tomorrow = rootView.findViewById(R.id.txt_Tomorrow)
        txt_temperatureTomorrow = rootView.findViewById(R.id.txt_temperatureTomorrow)
        txt_afterTomorrow = rootView.findViewById(R.id.txt_afterTomorrow)
        txt_temperatureAfterTomorrow = rootView.findViewById(R.id.txt_temperatureAfterTomorrow)

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        val weatherRVAdapter = WeatherRVAdapter()
        recyclerView.adapter = weatherRVAdapter


        // Lấy dữ liệu từ Bundle và hiển thị lên TextView
        val bundle = arguments
        if (bundle != null) {
            val jsonData = bundle.getString("data")
            if (jsonData != null) {
                val gson = Gson()
                val data = gson.fromJson(jsonData, WeatherCityData::class.java)

                val dateList: List<String> =
                    data.fiveDayForecast?.DailyForecasts?.mapNotNull { it?.Date } ?: emptyList()

                val dayOfWeekList: List<String> = dateList.map { getDayOfWeekFromDateTime(it) }




                textViewData.setText(data.currentWeather?.get(0)?.WeatherText.toString())
                txt_temperature.setText(data.currentWeather?.get(0)?.Temperature?.Metric?.Value.toString())
                txt_today.setText(
                    "Hôm nay" + " " + data.fiveDayForecast?.DailyForecasts?.get(
                        0
                    )?.Day?.IconPhrase.toString()
                )
                txt_temperature_today.setText(
                    fahrenheitToCelsius((data.fiveDayForecast?.DailyForecasts?.get(0)?.Temperature?.Minimum?.Value)).toString() + "/"
                            + fahrenheitToCelsius((data.fiveDayForecast?.DailyForecasts?.get(0)?.Temperature?.Maximum?.Value)).toString()
                )

                txt_Tomorrow.setText("Ngày mai" + " " + data.fiveDayForecast?.DailyForecasts?.get(1)?.Day?.IconPhrase.toString())
                txt_temperatureTomorrow.setText(
                    fahrenheitToCelsius((data.fiveDayForecast?.DailyForecasts?.get(0)?.Temperature?.Minimum?.Value)).toString() + "/"
                            + fahrenheitToCelsius((data.fiveDayForecast?.DailyForecasts?.get(1)?.Temperature?.Maximum?.Value)).toString()
                )

                txt_afterTomorrow.setText(
                    dayOfWeekList[2] + " " + data.fiveDayForecast?.DailyForecasts?.get(2)?.Day?.IconPhrase.toString()
                )
                txt_temperatureAfterTomorrow.setText(
                    fahrenheitToCelsius((data.fiveDayForecast?.DailyForecasts?.get(0)?.Temperature?.Minimum?.Value)).toString() + "/"
                            + fahrenheitToCelsius((data.fiveDayForecast?.DailyForecasts?.get(2)?.Temperature?.Maximum?.Value)).toString()
                )
            }

        }
        btn_next.setOnClickListener {
            navigateToNewActivity()
        }

        return rootView
    }

    private fun navigateToNewActivity() {
        val intent = Intent(requireContext(), WeatherForecastActivity::class.java)
        startActivity(intent)
    }

    companion object {
        fun newInstance(data: WeatherCityData): Fragment1 {
            val fragment = Fragment1()
            val bundle = Bundle()
            val gson = Gson()
            val jsonData = gson.toJson(data)

            // Đưa chuỗi JSON vào Bundle
            bundle.putString("data", jsonData)
            fragment.arguments = bundle
            return fragment
        }
    }

    // Hàm chuyển đổi ngày tháng thành thứ
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

    fun celsiusToFahrenheit(celsius: Double): Double {
        return (celsius * 9 / 5) + 32
    }

    fun fahrenheitToCelsius(fahrenheit: Int?): Int? {
        if (fahrenheit != null) {
            return (fahrenheit - 32) * 5 / 9
        }
        return null
    }


}