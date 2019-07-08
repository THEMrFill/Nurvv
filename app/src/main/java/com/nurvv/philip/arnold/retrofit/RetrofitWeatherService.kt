package com.nurvv.philip.arnold.retrofit

import com.nurvv.philip.arnold.BuildConfig
import com.nurvv.philip.arnold.retrofit.weatherdata.WeatherLookup
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitWeatherService {
    @GET("forecast?")
    suspend fun getWeather(@Query("lat") lat: Float,
                           @Query("lon") lon: Float,
                           @Query("units") units: String = WeatherDefaults.UnitsMetric,
                           @Query("APPID") key: String = BuildConfig.WEATHER_KEY
                    ): Response<WeatherLookup>
}