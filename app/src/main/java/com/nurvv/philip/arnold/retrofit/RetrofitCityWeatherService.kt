package com.nurvv.philip.arnold.retrofit

import com.nurvv.philip.arnold.BuildConfig
import com.nurvv.philip.arnold.retrofit.weatherdata.WeatherLookup
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitCityWeatherService {
    //creating a Network Interceptor to add api_key in all the request as authInterceptor
    private val interceptor = Interceptor { chain ->
        val url = chain.request().url().newBuilder().addQueryParameter("apiKey", BuildConfig.WEATHER_KEY).build()
        val request = chain.request()
            .newBuilder()
            .url(url)
            .build()
        chain.proceed(request)
    }
    // we are creating a networking client using OkHttp and add our authInterceptor.
    private val apiClient = OkHttpClient().newBuilder().addInterceptor(interceptor).build()

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder().client(apiClient)
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val weatherApi: RetrofitWeatherService = getRetrofit().create(RetrofitWeatherService::class.java)
}