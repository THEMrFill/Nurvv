package com.nurvv.philip.arnold.retrofit.citydata

import com.nurvv.philip.arnold.retrofit.citydata.AddressComponent
import com.nurvv.philip.arnold.retrofit.citydata.Geometry

data class Result(
    val address_components: ArrayList<AddressComponent>,
    val formatted_address: String,
    val geometry: Geometry,
    val place_id: String,
    val types: ArrayList<String>
)