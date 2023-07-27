package com.example.weatherkotlin.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.weatherkotlin.data.resource.Result
import com.example.weatherkotlin.R
import com.example.weatherkotlin.data.model.CurrentWeather
import com.example.weatherkotlin.ui.adapter.ViewPagerAdapter
import com.example.weatherkotlin.ui.viewmodel.MainActivityViewModel
import java.util.ArrayList
import me.relex.circleindicator.CircleIndicator3

class MainActivity : AppCompatActivity() {
    private lateinit var viewPager2: ViewPager2
    private lateinit var circleIndicator3: CircleIndicator3
    private lateinit var imageOverflowMenu: ImageView
    private lateinit var image_add: ImageView
    private lateinit var weatherViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager2 = findViewById(R.id.view_paper2)
        circleIndicator3 = findViewById(R.id.indicator)
        image_add = findViewById(R.id.image_add)
        imageOverflowMenu = findViewById(R.id.image_overflowMenu)
        imageOverflowMenu.setOnClickListener { view ->
            showPopupMenu(view)
        }
        image_add.setOnClickListener {
            val intent = Intent(this, CityManagementActivity::class.java)
            startActivity(intent)
        }
        val dataList = ArrayList<String>()
        // Khởi tạo WeatherViewModel
        weatherViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        weatherViewModel.loadCurrentWeather()
        // Lắng nghe dữ liệu thời tiết trả về
        try {
            weatherViewModel.currentWeatherLiveData.observe(this) {
                if (it == null) {
                    Toast.makeText(this, weatherViewModel.error.value?.message!!, Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(this, it[0].PrecipitationType.toString(), Toast.LENGTH_LONG)
                        .show()
                    Log.e("CurrentWeaher", it[0].Temperature?.Metric?.Value.toString())
                    dataList.add(it[0].WeatherText.toString())
                    dataList.add(it[0].WeatherText.toString())
                    dataList.add(it[0].WeatherText.toString())
                    val adapter = ViewPagerAdapter(this, dataList)
                    viewPager2.adapter = adapter
                    circleIndicator3.setViewPager(viewPager2)

                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        val inflater: MenuInflater = popupMenu.menuInflater
        inflater.inflate(R.menu.menu_overflow, popupMenu.menu)

        // Xử lý sự kiện khi chọn một mục trong menu
        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            val itemId = menuItem.itemId

            if (itemId == R.id.item_share) {
                // Xử lý khi chọn Menu Item 1
                true
            } else if (itemId == R.id.item_setting) {
                // Xử lý khi chọn Menu Item 2
                true
            } else {
                // Xử lý các mục menu khác nếu cần
                false
            }
        }

        // Hiển thị PopupMenu
        popupMenu.show()
    }
}
