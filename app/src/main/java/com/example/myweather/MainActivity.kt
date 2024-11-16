package com.example.myweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myweather.data.ForecastItem
import com.example.myweather.data.WeatherApiClient
import com.example.myweather.data.WeatherApiService
import com.example.myweather.data.WeatherRepository
import com.example.myweather.presentation.WeatherViewModelFactory
import java.util.*
import kotlin.collections.ArrayList
import kotlin.text.capitalize
import androidx.compose.runtime.Composable

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myweather.presentation.WeatherViewModel


class MainActivity : ComponentActivity() {

    private val weatherViewModel: WeatherViewModel by viewModels {
        val apiService = WeatherApiClient.instance.create(WeatherApiService::class.java)
        val repository = WeatherRepository(apiService)
        WeatherViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                WeatherApp(weatherViewModel = weatherViewModel)
            }
        }
    }

    @Composable
    fun WeatherApp(weatherViewModel: WeatherViewModel = viewModel()) {
        var cityName by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // TextField for user input
            var inputCity by remember { mutableStateOf("") }
            OutlinedTextField(
                value = inputCity,
                onValueChange = { inputCity = it },
                label = { Text("Enter City") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Fetch Forecast Button
            Button(
                onClick = {
                    cityName = inputCity
                    weatherViewModel.fetchForecast(cityName, "YOUR_API_KEY")
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Get Weather Forecast")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Display Forecast
            if (weatherViewModel.forecastList.isNotEmpty()) {
                Text(
                    text = "5-Day Forecast for ${weatherViewModel.cityName}",
//                    style = MaterialTheme.typography.h6
                )
                ForecastList(weatherViewModel.forecastList)
            } else if (cityName.isNotEmpty()) {
                Text(text = "Loading forecast for $cityName...")
            }
        }
    }

    @Composable
    fun ForecastList(forecastList: List<ForecastItem>) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(forecastList) { forecastItem ->
                ForecastListItem(forecastItem)
            }
        }
    }

    @Composable
    fun ForecastListItem(forecastItem: ForecastItem) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = forecastItem.dtTxt, modifier = Modifier.weight(1f))
            Text(text = "${forecastItem.main.temp}°C", modifier = Modifier.weight(1f))
            Text(text = forecastItem.weather[0].description.capitalize(), modifier = Modifier.weight(1f))
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        MaterialTheme {
            WeatherApp()
        }
    }
}
