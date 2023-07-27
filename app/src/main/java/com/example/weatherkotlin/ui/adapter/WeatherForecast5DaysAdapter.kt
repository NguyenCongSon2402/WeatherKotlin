package com.example.weatherkotlin.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherkotlin.R


class WeatherForecast5DaysAdapter :
    RecyclerView.Adapter<WeatherForecast5DaysAdapter.ViewHolder1>() {
    private var txt_thu1 = arrayOf("Hôm nay", "Ngày mai", "Th 6", "Th 7", "CN")
    private var ngay = arrayOf("19/7", "20/7", "21/7", "22/7", "23/7")
    private var txt_TempC1 = arrayOf("31°/25°", "31°/25°", "31°/25°", "31°/25°", "31°/25°")
    private var txt_speed1 = arrayOf("30 km/h", "30 km/h", "30 km/h", "30 km/h", "30 km/h")
    private var img_weather1 = arrayOf(
        R.drawable.rain_black,
        R.drawable.rain_black,
        R.drawable.rain_black,
        R.drawable.rain_black,
        R.drawable.rain_black
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder1 {
        val adapterLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false);
        return ViewHolder1(adapterLayout);
    }

    override fun onBindViewHolder(holder: ViewHolder1, position: Int) {

        holder.txt_thu.text = txt_thu1[position]
        holder.txt_ngay.text = ngay[position]
        holder.txt_TempC.text = txt_TempC1[position]
        holder.txt_speed.text = txt_speed1[position]
        holder.img_weather.setImageResource(img_weather1[position])
    }

    override fun getItemCount(): Int {
        if (txt_thu1 != null)
            return txt_thu1.size
        return 0
    }

    inner class ViewHolder1(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt_thu: TextView = itemView.findViewById(R.id.txt_thu)
        var txt_ngay: TextView = itemView.findViewById(R.id.txt_ngay)
        var txt_TempC: TextView = itemView.findViewById(R.id.txt_TempC)
        var txt_speed: TextView = itemView.findViewById(R.id.txt_speed)
        var img_weather: ImageView = itemView.findViewById(R.id.img_weather)
    }
}