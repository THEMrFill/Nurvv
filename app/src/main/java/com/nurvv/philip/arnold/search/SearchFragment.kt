package com.nurvv.philip.arnold.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.nurvv.philip.arnold.R
import com.nurvv.philip.arnold.main.MainActivityInterface
import com.nurvv.philip.arnold.model.CityLookup
import com.nurvv.philip.arnold.model.Weather
import com.nurvv.philip.arnold.retrofit.RetrofitFactory
import com.nurvv.philip.arnold.retrofit.RetrofitLocationService
import com.nurvv.philip.arnold.retrofit.RetrofitWeatherService
import com.nurvv.philip.arnold.retrofit.citydata.GeoLookup
import com.nurvv.philip.arnold.retrofit.weatherdata.WeatherLookup
import kotlinx.coroutines.*
import retrofit2.Response
import java.util.*

class SearchFragment(val mainActivityInterface: MainActivityInterface): Fragment() {
    companion object {
        fun newInstance(mainActivityInterface: MainActivityInterface): SearchFragment {
            return SearchFragment(mainActivityInterface)
        }
    }

    lateinit var cityName: TextInputEditText
    lateinit var recycler: RecyclerView
    lateinit var progress_circular: ContentLoadingProgressBar
    val service = RetrofitFactory.cityRetrofit.create(RetrofitLocationService::class.java)
    val coroutine = CoroutineScope(Dispatchers.IO)
    var call: Response<GeoLookup>? = null
    var adapter: SearchAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_location_entry, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.search).setOnClickListener({ _ ->
            SearchForCities()
        })
        cityName = view.findViewById(R.id.cityEntry)
        cityName.addTextChangedListener(object: TextWatcher {
            var timer = Timer()
            val DELAY: Long = 1000

            override fun afterTextChanged(s: Editable) {
                timer.cancel()
                timer = Timer()
                timer.schedule(
                    object : TimerTask() {
                        override fun run() {
                            SearchForCities()
                        }
                    },
                    DELAY
                )
            }

            override fun beforeTextChanged(s: CharSequence,
                                           start: Int,
                                           count: Int,
                                           after: Int) {

            }

            override fun onTextChanged(s: CharSequence,
                                       start: Int,
                                       before: Int,
                                       count: Int) {
            }
        })
        recycler = view.findViewById(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(context)
        progress_circular = view.findViewById(R.id.progress_circular)

        mainActivityInterface.SetToolbarTitle(getString(R.string.add_a_city))
    }

    fun SearchForCities() {
        adapter = SearchAdapter(ArrayList(), mainActivityInterface)

        val name = String.format("%s*", cityName.text.toString())
        if (name.length >= 3) {
            if (call != null) {
                coroutine.coroutineContext.cancel()
            }
            progress_circular.show()
            coroutine.launch {
                call = service.getCities(name)
                withContext(Dispatchers.Main) {
                    if (call!!.isSuccessful) {
                        val tempCall = call
                        call = null
                        HandleCityLookup(tempCall!!)
                    } else {
                        progress_circular.hide()
                        call = null
                    }
                }
            }
        }
    }

    private fun HandleCityLookup(call: Response<GeoLookup>) {
        val weatherService = RetrofitFactory.weatherRetrofit.create(RetrofitWeatherService::class.java)
        val cities = ArrayList<CityLookup>()
        for (result in call.body()!!.results) {
            val output = result.formatted_address
            val parts = output.split(",")
            val country = parts.last()
            val city = parts[parts.size - 2]
            if (city.length > 0 && country.length > 0) {
                val newCity = CityLookup(
                        city,
                        country,
                        result.geometry.location.lat.toFloat(),
                        result.geometry.location.lng.toFloat()
                    )

                cities.add(newCity)
                coroutine.launch {
                    val callWeather =
                        weatherService.getWeather(
                            result.geometry.location.lat.toFloat(),
                            result.geometry.location.lng.toFloat()
                        )
                    withContext(Dispatchers.Main) {
                        progress_circular.hide()
                        CalculateWeatherParts(callWeather)
                    }
                }
            }
        }
        adapter = SearchAdapter(cities, mainActivityInterface)
        recycler.adapter = adapter
    }

    private fun CalculateWeatherParts(call: Response<WeatherLookup>) {
        if (call.isSuccessful) {
            val weather = ArrayList<Weather>()
            var datePart = ""
            var timePart = ""
            val timesToCheck = arrayOf("12:00:00", "13:00:00", "14:00:00", "15:00:00")
            val thisCity = call.body()!!.city
            val city = CityLookup(
                            thisCity.name,
                            thisCity.country,
                            thisCity.coord.lat.toFloat(),
                            thisCity.coord.lon.toFloat()
                        )

            for (result in call.body()!!.list) {
                val parts = result.dt_txt.split(" ")
                if (datePart.length == 0) {
                    datePart = parts[0]
                }
                timePart = parts[1]
                datePart = parts[0]
                val thisWeather = Weather(
                    result.dt_txt,
                    result.weather[0].main,
                    result.weather[0].description,
                    result.weather[0].icon,
                    result.main.temp,
                    result.main.temp_min,
                    result.main.temp_max,
                    result.wind.speed,
                    result.wind.deg
                )
                weather.add(thisWeather)
                break
            }
            if (weather.size > 0) {
                ShowWeather(city, weather.get(0))
            }
        }
    }

    fun ShowWeather(city: CityLookup, weather: Weather) {
        adapter!!.updateWeather(city, weather)
    }
}