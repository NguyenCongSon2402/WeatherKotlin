package com.example.weatherkotlin.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.weatherkotlin.data.model1.WeatherCityData.WeatherCityData
import com.example.weatherkotlin.ui.fragment.Fragment1

class ViewPagerAdapter(
    private val fragmentActivity: FragmentActivity,
    private var dataList: ArrayList<WeatherCityData>
) : FragmentStateAdapter(fragmentActivity) {


    override fun createFragment(position: Int): Fragment {
        return if (position >= 0 && position < dataList.size) {
            val fragment = Fragment1.newInstance(dataList[position])
            fragment
        } else {
            Fragment()
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    // Phương thức để cập nhật dữ liệu và thông báo cho ViewPager2 biết để cập nhật lại
    fun updateDataList(newDataList: ArrayList<WeatherCityData>) {
        dataList = newDataList
        notifyDataSetChanged()
    }




}