package com.example.weatherkotlin.data.model1.CurrentConditions

import com.google.gson.annotations.SerializedName


data class Metric(

    @SerializedName("Value") var Value: Double? = null,
    @SerializedName("Unit") var Unit: String? = null,
    @SerializedName("UnitType") var UnitType: Int? = null

)