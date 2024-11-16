package com.example.myweather.data

import com.example.myweather.common.Result
import retrofit2.HttpException
import java.io.IOException

class WeatherRepository(private val apiService: WeatherApiService) {

    suspend fun getForecast(cityName: String, apiKey: String): Result<ForecastResponse> {
        return try {
            val response = apiService.getForecast(cityName, apiKey).execute()
            if (response.isSuccessful) {
                val forecast = response.body()
                if (forecast != null) {
                    Result.Success(forecast)
                } else {
                    Result.Error("No forecast data found")
                }
            } else {
                Result.Error("Error: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("An error occurred: ${e.message}")
        }
    }

}
