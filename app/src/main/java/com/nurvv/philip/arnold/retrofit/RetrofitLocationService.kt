package com.nurvv.philip.arnold.retrofit

import com.nurvv.philip.arnold.BuildConfig
import com.nurvv.philip.arnold.retrofit.citydata.GeoLookup
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitLocationService {
    @GET("json?")
    suspend fun getCities(
                    @Query("address") city: String,
                    @Query("key") api_key: String = BuildConfig.MAP_KEY
                ): Response<GeoLookup>
}