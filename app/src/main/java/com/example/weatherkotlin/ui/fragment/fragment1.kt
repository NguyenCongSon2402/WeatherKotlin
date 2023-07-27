package com.example.weatherkotlin.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherkotlin.R
import com.example.weatherkotlin.ui.WeatherForecastActivity
import com.example.weatherkotlin.ui.adapter.WeatherRVAdapter

class Fragment1 : Fragment() {
    private lateinit var textViewData: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var btn_next: Button
    private var isRecyclerViewAtEnd = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_layout, container, false)
        textViewData = rootView.findViewById(R.id.text_fragment)
        recyclerView = rootView.findViewById(R.id.RvWeather)
        btn_next = rootView.findViewById(R.id.btn_next)
        btn_next = rootView.findViewById(R.id.btn_next)
        // Tạo LayoutManager cho RecyclerView (LinearLayoutManager để hiển thị theo chiều dọc)
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        val weatherRVAdapter = WeatherRVAdapter()
        recyclerView.adapter = weatherRVAdapter


        // Lấy dữ liệu từ Bundle và hiển thị lên TextView
        val bundle = arguments
        if (bundle != null) {
            val data = bundle.getString("data")
            textViewData.text = data
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
        fun newInstance(data: String): Fragment1 {
            val fragment = Fragment1()
            val bundle = Bundle()
            bundle.putString("data", data)
            fragment.arguments = bundle
            return fragment
        }
    }
}