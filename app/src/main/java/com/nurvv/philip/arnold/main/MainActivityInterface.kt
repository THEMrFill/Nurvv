package com.nurvv.philip.arnold.main

import androidx.fragment.app.Fragment
import com.nurvv.philip.arnold.model.CityLookup

interface MainActivityInterface {
    fun PushToFragment(fragment: Fragment, addToStack: Boolean = false)
    fun SetToolbarTitle(title: String)
    fun AddLocation(city: CityLookup)
    fun WeatherFormatter(): String
    fun WeatherFormatter2(): String
}