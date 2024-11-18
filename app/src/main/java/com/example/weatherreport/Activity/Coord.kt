package com.example.weatherreport.Activity

import kotlinx.serialization.Serializable

@Serializable
data class Coord(
    val lat: Int,
    val lon: Int
)