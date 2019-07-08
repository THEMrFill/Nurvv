package com.nurvv.philip.arnold.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nurvv.philip.arnold.R
import com.nurvv.philip.arnold.main.MainActivityInterface
import com.nurvv.philip.arnold.model.CityLookup
import com.nurvv.philip.arnold.model.CityWeatherLookup
import com.nurvv.philip.arnold.model.Weather
import com.nurvv.philip.arnold.utils.Formatter
import java.util.*
import kotlin.collections.ArrayList

class SearchAdapter (
    val cityList: ArrayList<CityLookup>,
    val mainActivityInterface: MainActivityInterface
): RecyclerView.Adapter<SearchAdapter.LocationEntryViewHolder>() {
    val cities = ArrayList<CityWeatherLookup>()
    init {
        for (city in cityList) {
            cities.add(CityWeatherLookup(city))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationEntryViewHolder {
        return LocationEntryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_location_entry, parent, false), mainActivityInterface)
    }

    override fun getItemCount() = cities.size

    override fun onBindViewHolder(holder: LocationEntryViewHolder, position: Int) {
        holder.bind(cities[position])
    }

    fun updateWeather(city: CityLookup, weather: Weather) {
        for (i in 0..cities.size - 1) {
            val thisCity = cities.get(i)
            // separated out the variables as during the execution it was failing:
            // Weather service shifts the location to where it was taken from, not where requested
            val cityLat1 = ShortenDecimals(city.Latitude)
            val cityLat2 = ShortenDecimals(thisCity.cityLookup!!.Latitude)
            val cityLon1 = ShortenDecimals(city.Longitude)
            val cityLon2 = ShortenDecimals(thisCity.cityLookup!!.Longitude)
            if (cityLat1.equals(cityLat2) &&
                cityLon1.equals(cityLon2)) {
                    thisCity.weather = weather
                    cities.set(i, thisCity)
                    notifyItemChanged(i)
                    break
            }
        }
    }

    fun ShortenDecimals(double: Float?): String {
        return String.format("%.${2}f", double!!)
    }

    class LocationEntryViewHolder(itemView: View, val mainActivityInterface: MainActivityInterface) : RecyclerView.ViewHolder(itemView) {
        var cityName: TextView? = null
        var weather: TextView? = null
        var weather2: TextView? = null
        var weather3: TextView? = null
        init {
            cityName = itemView.findViewById(R.id.cityName)
            weather = itemView.findViewById(R.id.weather)
            weather2 = itemView.findViewById(R.id.weather2)
            weather3 = itemView.findViewById(R.id.weather3)
        }

        fun bind(item: CityWeatherLookup) = with(itemView) {
            cityName!!.text = String.format("%s, %s", item.cityLookup!!.Name, item.cityLookup!!.Country)
            if (item.weather != null) {
                weather!!.text = FormatWeather(item.weather!!)
                weather2!!.text = FormatWeather2(item.weather!!)
                weather3!!.text = item.weather!!.description
            }
            setOnClickListener { _ -> LinkTo(item.cityLookup!!) }
        }

        fun LinkTo(item: CityLookup) {
            mainActivityInterface.AddLocation(item)
        }
        fun FormatWeather(weather: Weather): String {
            val value = String.format(Locale.ENGLISH,
                mainActivityInterface.WeatherFormatter(),
                Formatter.TimeFormatter(weather.timestamp),
                Formatter.TempFormatter(weather.temp, true))
            return value
        }
        fun FormatWeather2(weather: Weather): String {
            val value = String.format(Locale.ENGLISH,
                mainActivityInterface.WeatherFormatter2(),
                Formatter.WindFormatter(weather.wind_speed, weather.wind_direction))
            return value
        }
    }
}