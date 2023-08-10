package com.example.weatherkotlin.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.weatherkotlin.R
import com.example.weatherkotlin.data.model1.FiveDayForecast.FiveDayForecast
import com.example.weatherkotlin.data.model1.WeatherCityData.WeatherCityData
import com.example.weatherkotlin.ui.WeatherForecastActivity
import com.example.weatherkotlin.ui.adapter.WeatherRVAdapter
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class Fragment1 : Fragment() {
    private lateinit var textViewData: TextView
    private lateinit var txt_temperature: TextView
    private lateinit var txt_today: TextView
    private lateinit var txt_temperature_today: TextView
    private lateinit var txt_Tomorrow: TextView
    private lateinit var txt_temperatureTomorrow: TextView
    private lateinit var txt_afterTomorrow: TextView
    private lateinit var txt_temperatureAfterTomorrow: TextView
    private lateinit var Humidity: TextView
    private lateinit var RealFeelTemperature: TextView
    private lateinit var UVIndex: TextView
    private lateinit var ATM: TextView
    private lateinit var rain: TextView
    private lateinit var txt_direction: TextView
    private lateinit var speeds: TextView


    private lateinit var icon1: ImageView
    private lateinit var icon2: ImageView
    private lateinit var icon3: ImageView


    private lateinit var recyclerView: RecyclerView
    private lateinit var btn_next: Button
    private lateinit var scrollView: NestedScrollView
    private lateinit var data: WeatherCityData
    private lateinit var view_paper2: ViewPager2


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_layout, container, false)

        scrollView = rootView.findViewById(R.id.scrollView)
        textViewData = rootView.findViewById(R.id.text_fragment)
        txt_temperature = rootView.findViewById(R.id.txt_temperature)
        recyclerView = rootView.findViewById(R.id.RvWeather)
        btn_next = rootView.findViewById(R.id.btn_next)
        txt_today = rootView.findViewById(R.id.txt_today)
        txt_temperature_today = rootView.findViewById(R.id.txt_temperature_today)
        txt_Tomorrow = rootView.findViewById(R.id.txt_Tomorrow)
        txt_temperatureTomorrow = rootView.findViewById(R.id.txt_temperatureTomorrow)
        txt_afterTomorrow = rootView.findViewById(R.id.txt_afterTomorrow)
        txt_temperatureAfterTomorrow = rootView.findViewById(R.id.txt_temperatureAfterTomorrow)
        Humidity = rootView.findViewById(R.id.doAm)
        RealFeelTemperature = rootView.findViewById(R.id.tempC)
        UVIndex = rootView.findViewById(R.id.UV)
        ATM = rootView.findViewById(R.id.ATM)
        rain = rootView.findViewById(R.id.rain)
        txt_direction = rootView.findViewById(R.id.txt_direction)
        speeds = rootView.findViewById(R.id.speeds)
        view_paper2 = requireActivity().findViewById(R.id.view_paper2)

        icon1 = rootView.findViewById(R.id.icon1)
        icon2 = rootView.findViewById(R.id.icon2)
        icon3 = rootView.findViewById(R.id.icon3)


        //swipeRefreshLayout = requireActivity().findViewById(R.id.swipeRefreshLayout)

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager


        // tạo một OnItemTouchListener để  xử lí sự kiện chạm trên các item trong recycview
        recyclerView.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            // khởi tạo biến startX để lưu trữ giá trị toạ độ X của điểm chạm lần đầu tiên màn hình
            private var startX = 0f

            // phương thức onInterceptTouchEvent được gọi khi sự kiện chạm diễn ra trong recycview
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                // xử lí các sự kiện ACTION_DOWN, ACTION_MOVE , ACTION_UP của MotionEvent
                when (e.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // Lưu giá trị toạ độ X của điểm chạm đầu tiên
                        startX = e.x
                    }

                    MotionEvent.ACTION_MOVE -> {
                        // xác định xem recycview có thể cuộn sang phải hay sang trái không
                        val isScrollingRight = e.x < startX
                        val isScrollingToLeft = isScrollingRight && recyclerView.canScrollRight
                        val isScrollingToRight = !isScrollingRight && recyclerView.canScrollLeft

                        val isScrollingToDown = !isScrollingRight && recyclerView.canScrollDown
                        val isScrollingToUp = !isScrollingRight && recyclerView.canScrollUp
                        // Nếu có thể cuộn sang phải hoặc sang trái, yêu cầu cha của RecyclerView không can thiệp vào sự kiện chạm
                        recyclerView.parent.requestDisallowInterceptTouchEvent(isScrollingToRight || isScrollingToLeft || isScrollingToDown || isScrollingToUp)
                    }

                    MotionEvent.ACTION_UP -> {
                        // đặt lại giá trị của startX để về 0 khi sự kiện chạm kết thúc
                        startX = 0f
                    }
                    // nếu không phải sự kiện này thì ko lm j cả
                    else -> Unit
                }
                // trả về false để tiếp tục xử l các sự kiện khác nếu có
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
            }

        })


        // Lấy dữ liệu từ Bundle và hiển thị lên TextView
        val bundle = arguments
        if (bundle != null) {
            val jsonData = bundle.getString("data")
            if (jsonData != null) {
                val gson = Gson()
                data = gson.fromJson(jsonData, WeatherCityData::class.java)


                val dateList: List<String> =
                    data.fiveDayForecast?.DailyForecasts?.mapNotNull { it?.Date } ?: emptyList()

                val dayOfWeekList: List<String> = dateList.map { getDayOfWeekFromDateTime(it) }




                textViewData.setText(data.currentWeather?.get(0)?.WeatherText.toString() +" "+fahrenheitToCelsius((data.fiveDayForecast?.DailyForecasts?.get(0)?.Temperature?.Minimum?.Value)).toString() + "°/"
                        + fahrenheitToCelsius((data.fiveDayForecast?.DailyForecasts?.get(0)?.Temperature?.Maximum?.Value)).toString() + "°")
                txt_temperature.setText(
                    data.currentWeather?.get(0)?.Temperature?.Metric?.Value.toString() + "°" + data.currentWeather?.get(
                        0
                    )?.Temperature?.Metric?.Unit.toString()
                )
                txt_today.setText(
                    "Hôm nay" + " " + data.fiveDayForecast?.DailyForecasts?.get(
                        0
                    )?.Day?.IconPhrase.toString()
                )
                txt_temperature_today.setText(
                    fahrenheitToCelsius((data.fiveDayForecast?.DailyForecasts?.get(0)?.Temperature?.Minimum?.Value)).toString() + "°/"
                            + fahrenheitToCelsius((data.fiveDayForecast?.DailyForecasts?.get(0)?.Temperature?.Maximum?.Value)).toString() + "°"
                )

                txt_Tomorrow.setText("Ngày mai" + " " + data.fiveDayForecast?.DailyForecasts?.get(1)?.Day?.IconPhrase.toString())
                txt_temperatureTomorrow.setText(
                    fahrenheitToCelsius((data.fiveDayForecast?.DailyForecasts?.get(0)?.Temperature?.Minimum?.Value)).toString() + "°/"
                            + fahrenheitToCelsius((data.fiveDayForecast?.DailyForecasts?.get(1)?.Temperature?.Maximum?.Value)).toString() + "°"
                )

                txt_afterTomorrow.setText(
                    dayOfWeekList[2] + " " + data.fiveDayForecast?.DailyForecasts?.get(2)?.Day?.IconPhrase.toString()
                )
                txt_temperatureAfterTomorrow.setText(
                    fahrenheitToCelsius((data.fiveDayForecast?.DailyForecasts?.get(0)?.Temperature?.Minimum?.Value)).toString() + "°/" + fahrenheitToCelsius(
                        (data.fiveDayForecast?.DailyForecasts?.get(2)?.Temperature?.Maximum?.Value)
                    ).toString() + "°"
                )

                Humidity.setText(data.currentWeather?.get(0)?.RelativeHumidity.toString() + "%")
                RealFeelTemperature.setText(data.currentWeather?.get(0)?.RealFeelTemperature?.Metric?.Value.toString() + "°")
                UVIndex.setText(data.currentWeather?.get(0)?.UVIndex.toString())
                ATM.setText(
                    data.currentWeather?.get(0)?.Pressure?.Metric?.Value.toString() + data.currentWeather?.get(
                        0
                    )?.Pressure?.Metric?.Unit.toString()
                )
                txt_direction.setText(data.currentWeather?.get(0)?.Wind?.Direction?.Localized)
                speeds.setText(
                    data.currentWeather?.get(0)?.Wind?.Speed?.Metric?.Value.toString() + data.currentWeather?.get(
                        0
                    )?.Wind?.Speed?.Metric?.Unit.toString()
                )

                rain.setText(data.hourlyForecast?.get(0)?.RainProbability.toString()+"%")



                val ic1=data.fiveDayForecast?.DailyForecasts?.get(0)?.Day?.Icon
                val ic2=data.fiveDayForecast?.DailyForecasts?.get(1)?.Day?.Icon
                val ic3=data.fiveDayForecast?.DailyForecasts?.get(2)?.Day?.Icon

                val iconName1 = "s_$ic1" // iconName sẽ là "s_2"
                val iconName2 = "s_$ic2" // iconName sẽ là "s_2"
                val iconName3 = "s_$ic3" // iconName sẽ là "s_2"


                val resourceId1 = resources.getIdentifier(iconName1, "drawable",requireContext().getPackageName() )
                val resourceId2 = resources.getIdentifier(iconName2, "drawable",requireContext().getPackageName() )
                val resourceId3 = resources.getIdentifier(iconName3, "drawable",requireContext().getPackageName() )

                if (resourceId1 != 0) {
                    icon1.setImageResource(resourceId1)

                } else {
                    icon1.setImageResource(R.drawable.s_1)
                }
                if (resourceId2 != 0) {
                    icon2.setImageResource(resourceId2)

                } else {
                    icon2.setImageResource(R.drawable.s_1)
                }
                if (resourceId3 != 0) {
                    icon3.setImageResource(resourceId3)

                } else {
                    icon3.setImageResource(R.drawable.s_1)
                }


            }

        }
        btn_next.setOnClickListener {
            navigateToNewActivity(data.fiveDayForecast)
        }
        val context = requireContext() // hoặc val context = context

        val weatherRVAdapter = WeatherRVAdapter(data.hourlyForecast,context)
        recyclerView.adapter = weatherRVAdapter

        return rootView
    }

    private val RecyclerView.canScrollRight: Boolean
        get() = canScrollHorizontally(SCROLL_DIRECTION_RIGHT)

    private val RecyclerView.canScrollLeft: Boolean
        get() = canScrollHorizontally(SCROLL_DIRECTION_LEFT)

    private val RecyclerView.canScrollDown: Boolean
        get() = canScrollVertically(SCROLL_DIRECTION_DOWN)
    private val RecyclerView.canScrollUp: Boolean
        get() = canScrollVertically(SCROLL_DIRECTION_UP)


    private fun navigateToNewActivity(fiveDayForecast: FiveDayForecast?) {
        if (fiveDayForecast != null) {
            val gson = Gson()
            val jsonData = gson.toJson(fiveDayForecast)

            val intent = Intent(requireContext(), WeatherForecastActivity::class.java)
            intent.putExtra("data", jsonData)
            startActivity(intent)
        }
    }


    companion object {
        fun newInstance(data: WeatherCityData): Fragment1 {
            val fragment = Fragment1()
            val bundle = Bundle()
            val gson = Gson()
            val jsonData = gson.toJson(data)

            // Đưa chuỗi JSON vào Bundle
            bundle.putString("data", jsonData)
            fragment.arguments = bundle
            return fragment
        }

        private const val SCROLL_DIRECTION_RIGHT = 1
        private const val SCROLL_DIRECTION_LEFT = -1
        private const val SCROLL_DIRECTION_DOWN = 1
        private const val SCROLL_DIRECTION_UP = -1

    }

    // Hàm chuyển đổi ngày tháng thành thứ
    fun getDayOfWeekFromDateTime(dateTimeString: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
        val date = dateFormat.parse(dateTimeString)
        val calendar = Calendar.getInstance().apply {
            time = date
        }
        val dayOfWeek =
            calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
        return dayOfWeek
    }

    fun celsiusToFahrenheit(celsius: Double): Double {
        return (celsius * 9 / 5) + 32
    }

    fun fahrenheitToCelsius(fahrenheit: Int?): Int? {
        if (fahrenheit != null) {
            return (fahrenheit - 32) * 5 / 9
        }
        return null
    }

//    override fun onResume() {
//        super.onResume()
//        if (isAdded) {
//            scrollView.viewTreeObserver.addOnScrollChangedListener {
//                val isAtTop = scrollView.scrollY == 0
//                val parentActivity = activity as? MainActivity
//                Toast.makeText(requireContext(), isAtTop.toString(), Toast.LENGTH_SHORT).show()
//
//                if (parentActivity != null) {
//                    parentActivity.setSwipeRefreshLayoutEnabled(isAtTop)
//                }
//            }
//        }
//    }


}