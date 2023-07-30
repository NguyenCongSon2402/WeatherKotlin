package com.example.weatherkotlin.data.model1.FiveDayForecast

import com.google.gson.annotations.SerializedName


data class AirAndPollen (

  @SerializedName("Name"          ) var Name          : String? = null,
  @SerializedName("Value"         ) var Value         : Int?    = null,
  @SerializedName("Category"      ) var Category      : String? = null,
  @SerializedName("CategoryValue" ) var CategoryValue : Int?    = null,
  @SerializedName("Type"          ) var Type          : String? = null

)