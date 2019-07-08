package com.nurvv.philip.arnold.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.nurvv.philip.arnold.R
import com.nurvv.philip.arnold.model.CityLookup
import com.nurvv.philip.arnold.search.SearchFragment
import com.nurvv.philip.arnold.weather.WeatherFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainActivityInterface {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PushToEntry()
    }

    override fun PushToFragment(fragment: Fragment, addToStack: Boolean) {
        ClearBackStack()
        val transaction = supportFragmentManager.beginTransaction()
        if (addToStack) {
            transaction.addToBackStack(null)
        }
        transaction.replace(R.id.fragment, fragment)
        transaction.commit()
    }

    override fun SetToolbarTitle(title: String) {
        val actionBar = getSupportActionBar()
        actionBar!!.title = title
    }

    override fun AddLocation(city: CityLookup) {
        PushToWeather(city)
    }

    override fun WeatherFormatter(): String {
        return getString(R.string.city_list_weather_format)
    }
    override fun WeatherFormatter2(): String {
        return getString(R.string.city_list_weather_format2)
    }

    fun ClearBackStack() {
        val fm = getSupportFragmentManager()
        for (i in 0 until fm.getBackStackEntryCount()) {
            fm.popBackStack()
        }
    }

    private fun PushToWeather(city: CityLookup) {
        val fragment = WeatherFragment.newInstance(this, city)
        PushToFragment(fragment, true)
    }

    fun PushToEntry() {
        val fragment = SearchFragment.newInstance(this)
        PushToFragment(fragment)
    }
}
