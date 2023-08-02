package com.example.weatherkotlin.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2

import com.example.weatherkotlin.R
import com.example.weatherkotlin.data.model1.CurrentConditions.CurrentConditions
import com.example.weatherkotlin.data.model1.FiveDayForecast.FiveDayForecast
import com.example.weatherkotlin.data.model1.GeopositionSearch.LocationRequestBody
import com.example.weatherkotlin.data.model1.HourlyForecasts.HourlyForecasts
import com.example.weatherkotlin.data.model1.WeatherCityData.WeatherCityData

import com.example.weatherkotlin.ui.adapter.ViewPagerAdapter
import com.example.weatherkotlin.ui.viewmodel.MainActivityViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.ArrayList
import me.relex.circleindicator.CircleIndicator3
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var viewPager2: ViewPager2
    private lateinit var circleIndicator3: CircleIndicator3
    private lateinit var imageOverflowMenu: ImageView
    private lateinit var image_add: ImageView
    private lateinit var img_wifiOff: ImageView
    private lateinit var title_update: TextView
    private lateinit var title_fragment: TextView
    private lateinit var title_updateSucces: TextView
    private lateinit var weatherViewModel: MainActivityViewModel
    private val LOCATION_PERMISSION_CODE = 1001
    private val dataList = ArrayList<WeatherCityData>()

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var locationRequestBody: LocationRequestBody? = LocationRequestBody()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        // Kiểm tra xem có dữ liệu trong SharedPreferences hay không
        val data = readDataFromSharedPreferences()
        Log.d("Hiển thị dữ liệu đã lưu", data.toString())
        if (data != null && data.isNotEmpty()) {
            Log.d("Hiển thị dữ liệu đã lưu", data.toString())
            Toast.makeText(this,"Hiển thị dữ liệu đã lưu",Toast.LENGTH_LONG).show()
            // Hiển thị dữ liệu từ SharedPreferences lên UI
            //showDataOnUI(data)
            checkLocationPermission()
        } else {
            Log.d("LOIII", "KO CO DATA")
            // Kiểm tra kết nối internet
            if (isOnline()) {
                // Gọi API để lấy dữ liệu
                Log.d("Call API", "KO CO DATA1")
                Toast.makeText(this,"Call API",Toast.LENGTH_LONG).show()
                checkLocationPermission()
            } else {
                // Hiển thị thông báo cho người dùng khi không có dữ liệu và không có kết nối internet
                Toast.makeText(
                    this,
                    "Không có dữ liệu hiển thị và không có kết nối internet",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        // Khởi tạo WeatherViewModel

    }

    private fun showDataOnUI(data: ArrayList<WeatherCityData>?) {
        if (data != null && data.isNotEmpty()) {
            // Hiển thị dữ liệu từ SharedPreferences lên UI
            renderList(data)
        } else {
            // Hiển thị thông báo cho người dùng khi không có dữ liệu
            Toast.makeText(this, "Không có dữ liệu hiển thị", Toast.LENGTH_SHORT).show()
        }
    }

    private fun readDataFromSharedPreferences(): ArrayList<WeatherCityData>? {
        // lấy dữ liệu JSON string từ SharedPreferences
        val sharedPreferences = getSharedPreferences("WeatherData", Context.MODE_PRIVATE)
        val dataListJson = sharedPreferences.getString("DataListJson", "")
        if (dataListJson != null &&dataListJson.isNotEmpty()) {
            val gson = Gson()
            val dataListType = object : TypeToken<ArrayList<WeatherCityData>>() {}.type
            val dataList: ArrayList<WeatherCityData> = gson.fromJson(dataListJson, dataListType)
            return dataList
        }
        return null
    }

    private fun removeDataAtIndex(index: Int) {
        val dataList = readDataFromSharedPreferences()
        if (dataList != null) {
            dataList.removeAt(index)
            saveDataToSharedPreferences(dataList) // Lưu lại danh sách sau khi xoá phần tử
        }
    }

    private fun replaceDataAtIndex(index: Int, weatherCityData: WeatherCityData) {
        val dataList = readDataFromSharedPreferences()
        if (dataList != null) {
            dataList[index] = weatherCityData
            saveDataToSharedPreferences(dataList) // Lưu lại danh sách sau khi thay đổi
        }
    }


    fun isOnline(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
        if (capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || capabilities.hasTransport(
                NetworkCapabilities.TRANSPORT_WIFI
            ) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
        ) {
            return true
        }
        return false
    }

    fun refresh() {
        img_wifiOff.visibility = View.GONE
        if (isOnline()) {
            title_update.visibility = View.VISIBLE
            img_wifiOff.visibility = View.GONE
            setUpViewModel()
        } else {
            img_wifiOff.visibility = View.VISIBLE
            Toast.makeText(this, "Không có kết nối internet", Toast.LENGTH_LONG).show()

        }
    }

    fun saveDataToSharedPreferences(weatherCityData: ArrayList<WeatherCityData>) {
        val gson = Gson()
        val dataListJson = gson.toJson(weatherCityData)
        val sharedPreferences = getSharedPreferences("WeatherData", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("DataListJson", dataListJson).apply()
    }

//    override fun onRefresh() {
//        refresh()
//        swipeRefreshLayout.isRefreshing = false
//    }

    private fun setupObserver() = try {
        var currentWeather: List<CurrentConditions>? = null
        var hourlyForecast: List<HourlyForecasts>? = null
        var fiveDayForecast: FiveDayForecast? = null

        weatherViewModel.cityInfo.observe(this){
            if (it == null){
                Toast.makeText(this, weatherViewModel.errorTry.value?.toString()!!, Toast.LENGTH_LONG)
                    .show()
            }
            else{

                val citiesList = ArrayList<String>()
                citiesList.add(it.LocalizedName.toString())
                // Lưu giá trị nameCity vào SharedPreferences
                getSharedPreferences("City", Context.MODE_PRIVATE)
                    .edit()
                    .putStringSet("citiesList", citiesList.toSet())
                    .apply()
            }
        }
        weatherViewModel.currentWeatherLiveData.observe(this) {
            title_update.visibility = View.VISIBLE
            if (it == null) {
                Toast.makeText(this, weatherViewModel.error.value?.message!!, Toast.LENGTH_LONG)
                    .show()
                Toast.makeText(
                    this, weatherViewModel.errorTry.value?.toString(), Toast.LENGTH_LONG
                ).show()
                title_update.visibility = View.GONE
            } else {
                title_update.visibility = View.GONE
                title_updateSucces.visibility = View.VISIBLE
                currentWeather = it
                Log.e("Đã cập nhập xong1", it[0].Temperature?.Metric?.Value.toString())
            }

        }


        weatherViewModel.HourlyForecast.observe(this) {
            if (it == null) {
                Toast.makeText(this, weatherViewModel.error.value?.message!!, Toast.LENGTH_LONG)
                    .show()
                Toast.makeText(
                    this, weatherViewModel.errorTry.value?.toString(), Toast.LENGTH_LONG
                ).show()
            } else {
                hourlyForecast = it
                Log.e("Đã cập nhập xong2", it[0].Temperature?.Value.toString())
                checkAndRenderData(currentWeather, hourlyForecast, fiveDayForecast)
            }

        }
        weatherViewModel.FiveDayForecastLiveData.observe(this) {
            if (it == null) {
                Toast.makeText(this, weatherViewModel.error.value?.message!!, Toast.LENGTH_LONG)
                    .show()
                Toast.makeText(
                    this, weatherViewModel.errorTry.value?.toString(), Toast.LENGTH_LONG
                ).show()
                checkAndRenderData(currentWeather, hourlyForecast, fiveDayForecast)
            } else {
                fiveDayForecast = it
                Log.e("Đã cập nhập xong3", it.Headline?.Text.toString())
                checkAndRenderData(currentWeather, hourlyForecast, fiveDayForecast)
            }

        }

    } catch (e: Exception) {
        Toast.makeText(this, "Lỗi trong quá trình lấy data API", Toast.LENGTH_SHORT).show()
    }
    private fun checkAndRenderData(
        currentWeather: List<CurrentConditions>?,
        hourlyForecast: List<HourlyForecasts>?,
        fiveDayForecast: FiveDayForecast?
    ) {
        if (currentWeather != null && hourlyForecast != null && fiveDayForecast != null) {
            val weatherCityData = WeatherCityData(currentWeather, hourlyForecast, fiveDayForecast)
            dataList.add(weatherCityData)
            saveDataToSharedPreferences(dataList)
            renderList(dataList)
        }
    }


    private fun renderList(weatherCityData: ArrayList<WeatherCityData>) {
        if (weatherCityData != null) {
            val adapter = ViewPagerAdapter(this, weatherCityData)
            viewPager2.adapter = adapter
            circleIndicator3.setViewPager(viewPager2)
            title_updateSucces.visibility = View.GONE
        }
    }

    private fun initView() {
        viewPager2 = findViewById(R.id.view_paper2)
        circleIndicator3 = findViewById(R.id.indicator)
        image_add = findViewById(R.id.image_add)
        imageOverflowMenu = findViewById(R.id.image_overflowMenu)
        img_wifiOff = findViewById(R.id.img_wifiOff)
        title_update = findViewById(R.id.title_update)
        title_fragment = findViewById(R.id.title_fragment)
        title_updateSucces = findViewById(R.id.title_updateSucces)
        imageOverflowMenu.setOnClickListener { view ->
            showPopupMenu(view)
        }
        image_add.setOnClickListener {
            val intent = Intent(this, CityManagementActivity::class.java)
            startActivity(intent)
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
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

    //tach check permission vs lat lon rieng. Lay xong thi moi goi den lay du lieu
    private fun checkLocationPermission() {
        Log.d("LOIII", "KO CO DATA2")
        // kiểm tra xem quyền cấp vị trí chính xác đã được cấp hay chưa
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Quyền đã được cấp
            Log.d("LOIII", "KO CO DATA3")
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                Log.d("LOIII", "KO CO DATA3.1")
                if (location != null) {
                    Log.d("LOIII", "KO CO DATA4")
                    // đã lấy được vị trí
                    val latitude = location.latitude
                    val longitude = location.longitude
                    locationRequestBody = LocationRequestBody(latitude, longitude)

                    Log.e("Location", locationRequestBody.toString())
                    Toast.makeText(this, locationRequestBody.toString(), Toast.LENGTH_SHORT).show()
                    setUpViewModel() // tach ra ham khac

                }
            }.addOnFailureListener { exception ->
                Log.e("Location", "Có lỗi xảy ra khi lấy vị trí", exception)
                val intent = Intent(this, CityManagementActivity::class.java)
                startActivity(intent)
            }
        } else {
            // Quyền truy cập vị trí chính xác chưa được cấp, yêu cầu cấp
            requestLocationPermisson()
        }

    }


    private fun setUpViewModel() {
        Log.d("LOIII", "KO CO DATA5")
        if (locationRequestBody != null) {
            weatherViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
            weatherViewModel.loadCurrentWeather(locationRequestBody)
            // Lắng nghe dữ liệu thời tiết trả về
            Log.d("ERROR1", "HAHAHAHAH")
            setupObserver()
        }
    }

    private fun requestLocationPermisson() {
        // yêu cầu quyền truy cập vị trí chính xác
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // xử lí kết quả phản hồi quyền truy cập vị trí chính xác
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền truy cập vị trí chính xác đã được cấp
               //goi ham logic o day
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                    Log.d("LOIII", "KO CO DATA3.1")
                    if (location != null) {
                        Log.d("LOIII", "KO CO DATA4")
                        // đã lấy được vị trí
                        val latitude = location.latitude
                        val longitude = location.longitude
                        locationRequestBody = LocationRequestBody(latitude, longitude)

                        Log.e("Location", locationRequestBody.toString())
                        Toast.makeText(this, locationRequestBody.toString(), Toast.LENGTH_SHORT).show()
                        setUpViewModel() // tach ra ham khac

                    }
                }.addOnFailureListener { exception ->
                    Log.e("Location", "Có lỗi xảy ra khi lấy vị trí", exception)
                    val intent = Intent(this, CityManagementActivity::class.java)
                    startActivity(intent)
                }

            } else {
                // Người dùng từ chối cấp quyền vị trí chính xác
                showLocationPermissonDeniedMessage()
            }
        }
//        else if (requestCode == BACKGROUND_LOCATION_PERMISSION_CODE) if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            // người dùng đồng ý cấp quyền truy cập vị trí nền
//        } else {
//            showwBackgroundLocationPermisson()
//        }
    }


    private fun showLocationPermissonDeniedMessage() {
        AlertDialog.Builder(this).setTitle("Cần quyền")
            .setMessage("Ứng dụng không thể lấy vị trí nếu không được cấp quyền")
            .setPositiveButton("Đồng ý") { dialogInterface, _ ->
                // Thực hiện xử lý tùy ý khi người dùng đồng ý hiểu thông báo
                dialogInterface.dismiss()
                val intent = Intent(this, CityManagementActivity::class.java)
                startActivity(intent)
            }.create().show()
    }


}
