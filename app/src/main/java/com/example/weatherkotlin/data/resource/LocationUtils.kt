package com.example.weatherkotlin.data.resource

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*

class LocationUtils(private val context: Context) {

    // Mã yêu cầu quyền truy cập vị trí
    private val locationPermissionRequestCode = 1001

    // Khởi tạo FusedLocationProviderClient để lấy vị trí
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    // LiveData chứa thông tin vị trí hiện tại
    private val _locationLiveData = MutableLiveData<Location>()
    val locationLiveData: LiveData<Location> get() = _locationLiveData

    // Hàm này được gọi để lấy vị trí hiện tại
    fun getLastKnownLocation(activity: AppCompatActivity) {
        // Kiểm tra quyền truy cập vị trí
        if (hasLocationPermission()) {
            // Nếu quyền đã được cấp, tiến hành lấy vị trí
            getLocation(activity)
        } else {
            // Nếu quyền chưa được cấp, yêu cầu quyền từ người dùng
            requestLocationPermission(activity)
        }
    }

    // Kiểm tra quyền truy cập vị trí
    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Yêu cầu quyền truy cập vị trí từ người dùng
    private fun requestLocationPermission(activity: AppCompatActivity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            locationPermissionRequestCode
        )
    }

    // Lấy vị trí hiện tại từ FusedLocationProviderClient
    @SuppressLint("MissingPermission")
    private fun getLocation(activity: AppCompatActivity) {
        // Kiểm tra lại quyền truy cập vị trí trước khi lấy vị trí
        if (hasLocationPermission()) {
            // Tạo LocationRequest để yêu cầu vị trí với độ chính xác cao
            val locationRequest = LocationRequest.create().apply {
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                interval = 1000 // Thời gian giữa các lần lấy vị trí
            }

            // Callback để nhận kết quả lấy vị trí
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    super.onLocationResult(p0)
                    // Lấy danh sách các vị trí trong locationResult
                    val locations: List<Location> = p0.locations
                    // Kiểm tra danh sách vị trí không rỗng và lấy thông tin vị trí đầu tiên
                    if (locations.isNotEmpty()) {
                        _locationLiveData.postValue(locations[0])
                        fusedLocationClient.removeLocationUpdates(this)
                        val location = locations[0]
                        val latitude = location.latitude
                        val longitude = location.longitude

                        // Hiển thị thông tin vị trí qua Logcat
                        Log.d("Location", "Latitude: $latitude, Longitude: $longitude")

                        // TODO: Tiến hành sử dụng thông tin vị trí trong các công việc cần thiết
                    } else {
                        Log.e("Location", "Không có thông tin vị trí")
                    }

                }
//                override fun onLocationResult(p0: LocationResult?) {
//                    // Lấy vị trí thành công
//                    locationResult?.let {
//                        val location = it.lastLocation
//                        // Đưa thông tin vị trí vào LiveData để cập nhật UI
//                        _locationLiveData.postValue(location)
//                        // Ngừng lắng nghe để không tiếp tục lấy vị trí sau khi đã có dữ liệu
//                        fusedLocationClient.removeLocationUpdates(this)
//                    }
//                }
            }

            // Yêu cầu FusedLocationProviderClient lắng nghe vị trí và thông báo kết quả qua callback
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } else {
            // Xử lý trường hợp quyền truy cập vị trí bị từ chối
        }
    }

}

