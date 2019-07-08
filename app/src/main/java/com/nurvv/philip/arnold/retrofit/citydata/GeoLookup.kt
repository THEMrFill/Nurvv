package com.nurvv.philip.arnold.retrofit.citydata

data class GeoLookup(
    val results: List<Result>,
    val status: String
)