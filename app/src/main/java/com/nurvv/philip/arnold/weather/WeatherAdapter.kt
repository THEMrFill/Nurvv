package com.nurvv.philip.arnold.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nurvv.philip.arnold.R
import com.nurvv.philip.arnold.model.Weather
import com.nurvv.philip.arnold.retrofit.weatherdata.WeatherLookup
import com.nurvv.philip.arnold.utils.Formatter
import kotlinx.android.synthetic.main.recycler_weather.view.*

class WeatherAdapter(var weather: ArrayList<Weather>): RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        return WeatherViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_weather, parent, false))
    }

    override fun getItemCount() = weather.size

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(weather[position])
    }

    class WeatherViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var time: TextView
        var temp: TextView
        var wind: TextView
        var windIcon: ImageView
        var weather: TextView

        init {
            time = itemView.findViewById(R.id.time)
            temp = itemView.findViewById(R.id.temp)
            wind = itemView.findViewById(R.id.wind)
            windIcon = itemView.findViewById(R.id.windIcon)
            weather = itemView.findViewById(R.id.weather)
        }

        fun bind(thisWeather: Weather) = with(itemView) {
            time.text = String.format("%s %s", Formatter.DayFormatter(thisWeather.timestamp), Formatter.TimeFormatter(thisWeather.timestamp))
            temp.text = Formatter.TempFormatter(thisWeather.temp, true)
            wind.text = Formatter.WindFormatter(thisWeather.wind_speed, thisWeather.wind_direction)
            weather.text = thisWeather.description

//            Picasso
//                .get()
//                .load(String.format(baseImageUrl, item.icon))
//                .into(windIcon)
        }

    }
}
