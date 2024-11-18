package com.example.weatherreport.Activity

import kotlinx.serialization.Serializable

@Serializable
class Data (
    val forecasts: ArrayList<cForecast>
)