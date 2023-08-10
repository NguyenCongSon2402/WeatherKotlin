package com.example.weatherkotlin.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2

import com.example.weatherkotlin.R
import com.example.weatherkotlin.data.model1.CurrentConditions.CurrentConditions
import com.example.weatherkotlin.data.model1.FiveDayForecast.FiveDayForecast
import com.example.weatherkotlin.data.model1.GeopositionSearch.LocationRequestBody
import com.example.weatherkotlin.data.model1.HourlyForecasts.HourlyForecasts
import com.example.weatherkotlin.data.model1.WeatherCityData.WeatherCityData

import com.example.weatherkotlin.ui.adapter.ViewPagerAdapter
import com.example.weatherkotlin.ui.fragment.ConfirmDialog
import com.example.weatherkotlin.ui.viewmodel.MainActivityViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import me.relex.circleindicator.CircleIndicator3
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var scrollView: ScrollView

    private lateinit var viewPager2: ViewPager2
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var circleIndicator3: CircleIndicator3
    private lateinit var imageOverflowMenu: ImageView
    private lateinit var image_add: ImageView
    private lateinit var img_wifiOff: ImageView
    private lateinit var title_update: TextView
    private lateinit var title_fragment: TextView
    private lateinit var title_updateSucces: TextView
    private lateinit var weatherViewModel: MainActivityViewModel
    private val LOCATION_PERMISSION_CODE = 1001
    private var dataList = ArrayList<WeatherCityData>()
    private var citiesList = ArrayList<String>()
    private var selectedPosition = 0

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var locationRequestBody: LocationRequestBody? = LocationRequestBody()
    private var key: String? = null
    private var currentPosition: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        // Khởi tạo WeatherViewModel
        // Kiểm tra xem có dữ liệu trong SharedPreferences hay không
        dataList = readDataFromSharedPreferences()
        citiesList = getCitiesListFromSharedPreferences()

        if (!dataList.isNullOrEmpty()) {
            Log.d("Hiển thị dữ liệu đã lưu", citiesList.toString())
            Log.d("Hiển thị dữ liệu đã lưu", dataList!!.size.toString())
            Toast.makeText(this, "Hiển thị dữ liệu đã lưu", Toast.LENGTH_LONG).show()
            // Hiển thị dữ liệu từ SharedPreferences lên UI
            showDataOnUI(dataList, citiesList)
            //checkLocationPermission()
        } else {
            Log.d("LOIII", "KO CO DATA")
            // Kiểm tra kết nối internet
            if (isOnline()) {
                // Gọi API để lấy dữ liệu
                Log.d("Call API", "KO CO DATA1")
                Toast.makeText(this, "Call API", Toast.LENGTH_LONG).show()
                checkLocationPermission()
            } else {
                // Hiển thị thông báo cho người dùng khi không có dữ liệu và không có kết nối internet

                val dialogFragment = ConfirmDialog()
                dialogFragment.show(supportFragmentManager, "ConfirmDialog")
            }
        }

    }

    private val ViewPager2.canScrollRight: Boolean
        get() = canScrollHorizontally(SCROLL_DIRECTION_RIGHT)

    private val ViewPager2.canScrollLeft: Boolean
        get() = canScrollHorizontally(SCROLL_DIRECTION_LEFT)


    private fun showDataOnUI(data: ArrayList<WeatherCityData>?, dataCity: ArrayList<String>) {
        if (data != null) {
            renderList(data, dataCity)
            setUpViewModel1()
        }
    }

    private fun readDataFromSharedPreferences(): ArrayList<WeatherCityData> {
        // lấy dữ liệu JSON string từ SharedPreferences
        val sharedPreferences = getSharedPreferences("WeatherData", Context.MODE_PRIVATE)
        val dataListJson = sharedPreferences.getString("DataListJson", "")
        if (dataListJson != null && dataListJson.isNotEmpty()) {
            val gson = Gson()
            val dataListType = object : TypeToken<ArrayList<WeatherCityData>>() {}.type
            val dataList: ArrayList<WeatherCityData> = gson.fromJson(dataListJson, dataListType)
            return dataList
        }
        return dataList
    }

    private fun getCitiesListFromSharedPreferences(): ArrayList<String> {
        val sharedPreferences = getSharedPreferences("City", Context.MODE_PRIVATE)
        val citiesListJson = sharedPreferences.getString("citiesList", "")

        // Nếu chuỗi JSON không rỗng, chuyển đổi thành danh sách ArrayList<String> sử dụng Gson
        if (!citiesListJson.isNullOrEmpty()) {
            val gson = Gson()
            val citiesListType = object : TypeToken<ArrayList<String>>() {}.type
            return gson.fromJson(citiesListJson, citiesListType)
        }

        // Trả về danh sách trống nếu không có dữ liệu hoặc xảy ra lỗi
        return ArrayList()
    }


    private fun replaceDataAtIndex(
        index: Int,
        weatherCityData: WeatherCityData
    ): ArrayList<WeatherCityData>? {
        val dataList = readDataFromSharedPreferences()
        if (dataList != null) {
            dataList[index] = weatherCityData
            saveDataToSharedPreferences(dataList) // Lưu lại danh sách sau khi thay đổi
            return dataList
        }
        return null
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
            setUpViewModel1()
        } else {
            img_wifiOff.visibility = View.VISIBLE
            val dialogFragment = ConfirmDialog()
            dialogFragment.show(supportFragmentManager, "ConfirmDialog")
            swipeRefreshLayout.isRefreshing = false

        }
    }

    fun saveDataToSharedPreferences(weatherCityData: ArrayList<WeatherCityData>) {
        val gson = Gson()
        val dataListJson = gson.toJson(weatherCityData)
        val sharedPreferences = getSharedPreferences("WeatherData", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("DataListJson", dataListJson).apply()
    }


    private fun setupObserver(currenPosition: Int?) = try {
        var currentWeather: List<CurrentConditions>? = null
        var hourlyForecast: List<HourlyForecasts>? = null
        var fiveDayForecast: FiveDayForecast? = null

        weatherViewModel.cityInfo.observe(this) {
            if (it == null) {
                Toast.makeText(
                    this,
                    "1" + weatherViewModel.errorTry.value.toString()!!,
                    Toast.LENGTH_LONG
                ).show()
            } else {

                //val citiesList = ArrayList<String>()
                Log.e("TEST key", it?.Key.toString() + it?.LocalizedName.toString())
                citiesList.add(it.LocalizedName.toString() + "-" + it?.Key)
                // Chuyển danh sách thành chuỗi JSON sử dụng Gson
                val gson = Gson()
                val citiesListJson = gson.toJson(citiesList)
                // Lưu giá trị nameCity vào SharedPreferences
                getSharedPreferences("City", Context.MODE_PRIVATE)
                    .edit()
                    .putString("citiesList", citiesListJson)
                    .apply()
            }
        }
        weatherViewModel.currentWeatherLiveData.observe(this) {
            title_update.visibility = View.VISIBLE
            if (it == null) {
                weatherViewModel.error2.observe(this) { errorValue ->
                    errorValue?.let {
                        Toast.makeText(this, "loi $it", Toast.LENGTH_LONG).show()
                    }
                }
                swipeRefreshLayout.isRefreshing = false
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
                weatherViewModel.error2.observe(this) { errorValue ->
                    errorValue?.let {
                        Toast.makeText(this, "loi $it", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                hourlyForecast = it
                Log.e("Đã cập nhập xong2", it[0].Temperature?.Value.toString())
                checkAndRenderData(currentWeather, hourlyForecast, fiveDayForecast, currenPosition)
            }

        }
        weatherViewModel.FiveDayForecastLiveData?.observe(this) {
            if (it == null) {
                weatherViewModel.error2.observe(this) { errorValue ->
                    errorValue?.let {
                        Toast.makeText(this, "loi $it", Toast.LENGTH_LONG).show()
                    }
                }

            } else {
                fiveDayForecast = it
                Log.e("Đã cập nhập xong3", it.Headline?.Text.toString())
                checkAndRenderData(currentWeather, hourlyForecast, fiveDayForecast, currenPosition)
            }

        }
//        weatherViewModel.error2.observeForever { errorValue ->
//            if (errorValue != null) {
//                Toast.makeText(this, errorValue, Toast.LENGTH_LONG)
//                    .show()
//            }
//        }


    } catch (e: Exception) {
        Toast.makeText(this, "Lỗi trong quá trình lấy data API", Toast.LENGTH_SHORT).show()
    }

    private fun checkAndRenderData(
        currentWeather: List<CurrentConditions>?,
        hourlyForecast: List<HourlyForecasts>?,
        fiveDayForecast: FiveDayForecast?,
        currenPosition: Int?
    ) {
        if (currentWeather != null && hourlyForecast != null && fiveDayForecast != null) {
            Log.e("CheckNULLLLLL", currenPosition.toString())
            if (currenPosition == null) {
                val weatherCityData =
                    WeatherCityData(currentWeather, hourlyForecast, fiveDayForecast)
                dataList.add(weatherCityData)
                dataList?.let { saveDataToSharedPreferences(it) }
                dataList?.let { renderList(it, citiesList) }
            } else {
                val weatherCityData =
                    WeatherCityData(currentWeather, hourlyForecast, fiveDayForecast)
                val data = replaceDataAtIndex(currenPosition, weatherCityData)
                if (data.isNullOrEmpty()) {
                    Toast.makeText(this, "Cập nhập thất bại", Toast.LENGTH_LONG).show()
                } else {
                    title_update.visibility = View.GONE
                    title_updateSucces.visibility = View.VISIBLE
                    swipeRefreshLayout.isRefreshing = false
                    Toast.makeText(this, "Cập nhập xong", Toast.LENGTH_LONG).show()
                    data?.let { renderList(it, citiesList) }
                }
            }
        }
    }


    private fun renderList(
        weatherCityData: ArrayList<WeatherCityData>,
        citiesList: ArrayList<String>
    ) {
        Log.e("CheckNULLLLL", "kiểm tra")
        if (weatherCityData != null) {
            adapter = ViewPagerAdapter(this, weatherCityData)
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
            val requestCode = 123 // Ví dụ
            val intent = Intent(this, CityManagementActivity::class.java)
            startActivityForResult(intent, requestCode)
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener(this)
        adapter = ViewPagerAdapter(this, dataList)
        viewPager2.adapter = adapter
        circleIndicator3.setViewPager(viewPager2)

//        val fragment = supportFragmentManager.findFragmentById(R.id.yourFragmentId) as f?
//        val scrollView = fragment?.view?.findViewById<ScrollView>(R.id.scrollView)

    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        val inflater: MenuInflater = popupMenu.menuInflater
        inflater.inflate(R.menu.menu_overflow, popupMenu.menu)

        // Xử lý sự kiện khi chọn một mục trong menu
        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            val itemId = menuItem.itemId

            if (itemId == R.id.item_share) {
                val activityView = window.decorView.rootView // Tham chiếu đến view của activity

                val capturedBitmap = captureScreen(activityView)
// Lưu ảnh vào bộ nhớ và lấy Uri
                val imageUri = saveBitmapAndGetUri(capturedBitmap)
// Tạo Intent để chia sẻ ảnh
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "image/*"
                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
                startActivity(Intent.createChooser(shareIntent, "Chia sẻ qua"))
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

    fun saveBitmapAndGetUri(bitmap: Bitmap): Uri {
        val imagesFolder = File(getExternalFilesDir(null), "images")
        imagesFolder.mkdirs()
        val file = File(imagesFolder, "shared_image.png")
        val stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
        stream.flush()
        stream.close()
        return FileProvider.getUriForFile(this, "com.example.weatherkotlin.share", file)
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
            setupObserver(currentPosition)
        }
    }

    private fun setUpViewModel1() {
        Log.d("LOIII", "KO CO DATA5")
        val currentPosition = viewPager2.currentItem
        if (!citiesList.isNullOrEmpty()) {
            val cityNameWithKey = citiesList[currentPosition]
            key = cityNameWithKey.substringAfter("-")

            if (!key.isNullOrEmpty()) {
                weatherViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
                weatherViewModel.loadWeather(key!!)
                // Lắng nghe dữ liệu thời tiết trả về
                Log.d("AAAAAAAAAAAAAAAAAAA", currentPosition.toString())
                setupObserver(currentPosition)
            }
        }
        else {
            title_update.visibility = View.GONE
            swipeRefreshLayout.isRefreshing = false
            Toast.makeText(this,"Đã xảy ra lỗi trong quá trình cập nhập!",Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(this, locationRequestBody.toString(), Toast.LENGTH_SHORT)
                            .show()
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

    override fun onResume() {
        super.onResume()

        if (selectedPosition != null) {
            viewPager2.setCurrentItem(selectedPosition, false)
        }
        // Cài đặt callback cho ViewPager2 khi Fragment thay đổi
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)


                // Tách tên thành phố từ chuỗi "name-key"
                val cityNameWithKey = citiesList[position]
                val cityName = cityNameWithKey.substringBefore("-")

                title_fragment.text = cityName
                Log.e("Check", cityName)
            }
        })


    }

    override fun onRestart() {
        super.onRestart()
        val data = readDataFromSharedPreferences()
        citiesList = getCitiesListFromSharedPreferences()
// Khi dataList thay đổi, gọi phương thức updateDataList() để cập nhật dữ liệu và viewpager
        if (data != null) {

            adapter.updateDataList(data)
            viewPager2.adapter = adapter
            circleIndicator3.setViewPager(viewPager2)
        }


    }

    override fun onRefresh() {
        refresh()
    }

    fun setSwipeRefreshLayoutEnabled(enabled: Boolean) {
        swipeRefreshLayout.isEnabled = enabled
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 123 && resultCode == Activity.RESULT_OK) {
            selectedPosition = data?.getIntExtra("selected_position", RecyclerView.NO_POSITION)!!

        }
    }

    companion object {
        private const val SCROLL_DIRECTION_RIGHT = 1
        private const val SCROLL_DIRECTION_LEFT = -1
    }

    fun captureScrollView(scrollView: ScrollView): Bitmap {
        val totalHeight = scrollView.getChildAt(0).height
        val bitmap = Bitmap.createBitmap(scrollView.width, totalHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        scrollView.draw(canvas)

        return bitmap
    }

    fun captureScreen(view: View): Bitmap {
        val screenshot = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(screenshot)
        view.draw(canvas)
        return screenshot
    }


}
