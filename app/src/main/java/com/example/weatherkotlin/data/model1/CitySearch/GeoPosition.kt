package com.example.weatherkotlin.data.model1.CitySearch

import com.google.gson.annotations.SerializedName


data class GeoPosition (

  @SerializedName("Latitude"  ) var Latitude  : Double?    = null,
  @SerializedName("Longitude" ) var Longitude : Double?    = null,
  @SerializedName("Elevation" ) var Elevation : Elevation? = Elevation()

)