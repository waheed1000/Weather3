package com.example.myweather.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweather.data.ForecastItem
import com.example.myweather.data.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.myweather.common.Result

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    val forecastList = mutableStateListOf<ForecastItem>()

    var cityName = mutableStateOf("")
        private set

    val isLoading = mutableStateOf(false)

    fun fetchForecast(city: String) {
        viewModelScope.launch {
            isLoading.value = true
            val result = withContext(Dispatchers.IO) { repository.getForecast(city) }

            // Update the stateful list
            when (result) {
                is Result.Success -> {
                    forecastList.clear()
                    forecastList.addAll(result.data.list)
                    cityName.value = result.data.city.name
                }
                is Result.Error -> {
                    forecastList.clear()
                    cityName.value = "Error: ${result.message}"
                }
            }
            isLoading.value = false
        }
    }
}
