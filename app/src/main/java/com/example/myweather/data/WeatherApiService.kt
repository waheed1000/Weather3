package com.example.myweather.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("forecast")
    fun getForecast(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String = "cf6855a9d3a0672a2ba1ebcc7d5b9d2a",
        @Query("units") units: String = "metric",
        @Query("cnt") count: Int? = null
    ): Call<ForecastResponse>
}
