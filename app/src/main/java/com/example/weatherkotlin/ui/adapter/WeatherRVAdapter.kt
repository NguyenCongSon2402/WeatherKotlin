package com.example.weatherkotlin.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherkotlin.R

class WeatherRVAdapter : RecyclerView.Adapter<WeatherRVAdapter.WeatherViewHolder>() {
    private var time = arrayOf("10h", "12h", "14h", "16h", "18h")
    private var temperature = arrayOf("31°C", "27°C", "28°C", "29°C", "30°C")
    private var WindSpeed = arrayOf("30 km/h", "30 km/h", "30 km/h", "30 km/h", "30 km/h")
    private var image = arrayOf(
        R.drawable.img_rain,
        R.drawable.img_rain,
        R.drawable.img_rain,
        R.drawable.img_rain,
        R.drawable.img_rain
    )


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val adapterLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.weather_rv_item, parent, false);
        return WeatherViewHolder(adapterLayout);
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.tv_time.text = time[position]
        holder.tv_temperature.text = temperature[position]
        holder.tv_WindSpeed.text = WindSpeed[position]
        holder.Img_Condition.setImageResource(image[position])

    }

    override fun getItemCount(): Int {
        return time.size
    }

    inner class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_time: TextView = itemView.findViewById(R.id.tv_time);
        var tv_temperature: TextView = itemView.findViewById(R.id.tv_temperature);
        var tv_WindSpeed: TextView = itemView.findViewById(R.id.tv_WindSpeed);
        var Img_Condition: ImageView = itemView.findViewById(R.id.Img_Condition);
    }
}