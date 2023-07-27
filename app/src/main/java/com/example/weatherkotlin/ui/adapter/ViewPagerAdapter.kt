package com.example.weatherkotlin.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.weatherkotlin.ui.fragment.Fragment1

class ViewPagerAdapter(
    private val fragmentActivity: FragmentActivity,
    private val dataList: List<String>
) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return if (position >= 0 && position < dataList.size) {
            Fragment1.newInstance(dataList[position])
        } else {
            Fragment()
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}