package com.nurvv.philip.arnold.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nurvv.philip.arnold.R
import com.nurvv.philip.arnold.main.MainActivityInterface
import com.nurvv.philip.arnold.model.CityLookup
import com.nurvv.philip.arnold.model.Weather
import com.nurvv.philip.arnold.retrofit.RetrofitFactory
import com.nurvv.philip.arnold.retrofit.RetrofitHourlyWeatherService
import com.nurvv.philip.arnold.retrofit.WeatherDefaults
import com.nurvv.philip.arnold.retrofit.weatherdata.WeatherLookup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class WeatherFragment(val mainActivityInterface: MainActivityInterface, val city: CityLookup): Fragment() {
    companion object {
        fun newInstance(mainActivityInterface: MainActivityInterface, city: CityLookup): WeatherFragment {
            return WeatherFragment(mainActivityInterface, city)
        }
    }

    lateinit var recycler: RecyclerView
    lateinit var adapter: WeatherAdapter
    lateinit var progress_circular: ContentLoadingProgressBar
    val weather = ArrayList<Weather>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_weather, container, false)

        recycler = view.findViewById(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(context)
        progress_circular = view.findViewById(R.id.progress_circular)

        mainActivityInterface.SetToolbarTitle(String.format("%s, %s", city.Name, city.Country))

        LookupWeather()

        return view
    }

    fun LookupWeather() {
        progress_circular.show()
        val service = RetrofitFactory.weatherRetrofit.create(RetrofitHourlyWeatherService::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val call = service.getWeather(city.Latitude!!, city.Longitude!!, WeatherDefaults.UnitsMetric)
            withContext(Dispatchers.Main) {
                progress_circular.hide()
                CalculateWeatherParts(call)
            }
        }
    }

    private fun CalculateWeatherParts(call: Response<WeatherLookup>) {
        if (call.isSuccessful) {
            weather.clear()
            var datePart = ""
            var timePart = ""
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
            }
            ShowWeather()
        }
    }

    fun ShowWeather() {
        adapter = WeatherAdapter(weather)
        recycler.adapter = adapter
    }
}