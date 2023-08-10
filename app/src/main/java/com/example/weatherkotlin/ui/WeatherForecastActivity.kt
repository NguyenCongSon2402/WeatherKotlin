package com.example.weatherkotlin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherkotlin.R
import com.example.weatherkotlin.data.model1.FiveDayForecast.FiveDayForecast
import com.example.weatherkotlin.ui.adapter.WeatherForecast5DaysAdapter
import com.google.gson.Gson

class WeatherForecastActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var btn_back: ImageButton
    private lateinit var fiveDayForecast: FiveDayForecast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_forecast)
        recyclerView = findViewById(R.id.RvWeatherForecast)
        btn_back = findViewById(R.id.btn_Back)


        // Lấy dữ liệu từ Intent
        val jsonData = intent.getStringExtra("data")
        val gson = Gson()
        fiveDayForecast = gson.fromJson(jsonData, FiveDayForecast::class.java)
        val context = this // hoặc ActivityName.this


        val apdapter = WeatherForecast5DaysAdapter(fiveDayForecast,context)
        recyclerView.adapter = apdapter
        btn_back.setOnClickListener {
            onBackPressed()
        }
    }
}