package com.example.weatherkotlin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherkotlin.R
import com.example.weatherkotlin.ui.adapter.WeatherForecast5DaysAdapter

class WeatherForecastActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var btn_back: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_forecast)
        recyclerView = findViewById(R.id.RvWeatherForecast)
        btn_back = findViewById(R.id.btn_Back)
        val apdapter = WeatherForecast5DaysAdapter()
        recyclerView.adapter = apdapter
        btn_back.setOnClickListener {
            onBackPressed()
        }
    }
}