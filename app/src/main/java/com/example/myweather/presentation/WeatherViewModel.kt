package com.example.myweather.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweather.data.ForecastItem
import com.example.myweather.data.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.myweather.common.Result

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    // A stateful list of forecast items
    val forecastList = mutableStateListOf<ForecastItem>()

    // A state to store the city name (optional)
    var cityName: String = ""

    fun fetchForecast(city: String, apiKey: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { repository.getForecast(city, apiKey) }

            // Update the stateful list
            when (result) {
                is Result.Success -> {
                    forecastList.clear() // Clear the previous list
                    forecastList.addAll(result.data.list) // Add the new forecast items
                    cityName = result.data.city.name // Update the city name
                }
                is Result.Error -> {
                    // Handle error state (optional)
                    forecastList.clear()
                    cityName = "Error: ${result.message}"
                }
            }
        }
    }
}
