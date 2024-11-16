package com.example.myweather.data

import com.google.gson.annotations.SerializedName

// Root response object for the forecast API
data class ForecastResponse(
    val cod: String,         // Response status code ("200" for success)
    val message: Int,        // Internal parameter, not usually relevant
    val cnt: Int,            // Number of forecast data points
    val list: List<ForecastItem>,  // List of forecast entries
    val city: City           // Information about the city
)

// Represents a single forecast entry in the list
data class ForecastItem(
    val dt: Long,             // Forecast time (Unix timestamp)
    val main: Main,           // Main weather metrics
    val weather: List<Weather>, // List of weather conditions
    val clouds: Clouds,       // Cloudiness data
    val wind: Wind,           // Wind data
    val visibility: Int,      // Visibility in meters
    @SerializedName("dt_txt")
    val dtTxt: String         // Human-readable forecast time (e.g., "2023-11-16 15:00:00")
)

// Information about the city in the forecast
data class City(
    val id: Int,              // City ID
    val name: String,         // City name
    val coord: Coord,         // City coordinates (lat/lon)
    val country: String,      // Country code (e.g., "GB")
    val timezone: Int,        // Timezone offset in seconds
    val sunrise: Long,        // Sunrise time (Unix timestamp)
    val sunset: Long          // Sunset time (Unix timestamp)
)

// Represents temperature and pressure data
data class Main(
    val temp: Double,         // Current temperature
    val feels_like: Double,   // Feels like temperature
    val temp_min: Double,     // Minimum temperature
    val temp_max: Double,     // Maximum temperature
    val pressure: Int,        // Atmospheric pressure at sea level (hPa)
    val humidity: Int         // Humidity percentage
)

// Represents weather conditions
data class Weather(
    val id: Int,              // Weather condition ID
    val main: String,         // Group of weather parameters (e.g., "Rain", "Clear")
    val description: String,  // Weather condition description (e.g., "light rain")
    val icon: String          // Weather icon code (e.g., "10d")
)

// Represents cloudiness data
data class Clouds(
    val all: Int              // Cloudiness percentage
)

// Represents wind data
data class Wind(
    val speed: Double,        // Wind speed (meter/sec)
    val deg: Int,             // Wind direction in degrees
    val gust: Double?         // Wind gust speed (meter/sec), optional
)

// Represents geographical coordinates
data class Coord(
    val lat: Double,          // Latitude
    val lon: Double           // Longitude
)
