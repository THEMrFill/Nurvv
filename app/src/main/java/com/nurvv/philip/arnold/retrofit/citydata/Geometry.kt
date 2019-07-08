package com.nurvv.philip.arnold.retrofit.citydata

import com.nurvv.philip.arnold.retrofit.citydata.Viewport

data class Geometry(
    val location: Location,
    val location_type: String,
    val viewport: Viewport
)