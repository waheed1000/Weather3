package com.example.myweather

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.example.myweather.data.WeatherApiClient
import com.example.myweather.data.WeatherApiService
import com.example.myweather.data.WeatherRepository
import com.example.myweather.presentation.WeatherViewModel
import com.example.myweather.presentation.WeatherViewModelFactory
import com.example.myweather.ui.ForecastScreen

class MainActivity : ComponentActivity() {

    private val weatherViewModel: WeatherViewModel by viewModels {
        val apiService = WeatherApiClient.instance.create(WeatherApiService::class.java)
        val repository = WeatherRepository(apiService)
        WeatherViewModelFactory(repository)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ForecastScreen(
                forecastList = weatherViewModel.forecastList,
                isLoading = weatherViewModel.isLoading.value,
                cityName = weatherViewModel.cityName.value,
                fetchForecast = { city ->
                    weatherViewModel.fetchForecast(city)
                }
            )
        }
    }
}
