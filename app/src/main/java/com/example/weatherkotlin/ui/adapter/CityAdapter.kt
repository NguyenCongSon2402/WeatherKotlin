package com.example.weatherkotlin.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherkotlin.R

import com.example.weatherkotlin.data.model1.CitySearch.CitySearch
import com.example.weatherkotlin.ui.MainActivity

class CityAdapter(private var cityList: List<CitySearch>) :
    RecyclerView.Adapter<CityAdapter.CityViewHolder>() {
    inner class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameCity: TextView = itemView.findViewById(R.id.nameCity)
        var nameCountry: TextView = itemView.findViewById(R.id.nameCountry)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_city, parent, false)
        return CityViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (cityList != null)
            return cityList.size
        return 0
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.nameCity.text = cityList[position]?.LocalizedName
        holder.nameCountry.text = cityList[position]?.Country?.LocalizedName
     }

    // Phương thức để cập nhật dữ liệu và cập nhật lại RecyclerView
    fun setData(newCities: List<CitySearch>) {
        cityList = newCities
        notifyDataSetChanged()
    }


}