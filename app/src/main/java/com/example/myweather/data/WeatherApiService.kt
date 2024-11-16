package com.example.myweather.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("forecast")
    fun getForecast(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("cnt") count: Int? = null
    ): Call<ForecastResponse>
}
