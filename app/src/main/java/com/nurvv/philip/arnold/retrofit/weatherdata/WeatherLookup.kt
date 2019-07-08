package com.nurvv.philip.arnold.retrofit.weatherdata

data class WeatherLookup(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<X>,
    val message: Double
)