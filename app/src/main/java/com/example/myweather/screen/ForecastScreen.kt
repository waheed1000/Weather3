package com.example.myweather.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.myweather.data.City
import com.example.myweather.data.Clouds
import com.example.myweather.data.Coord
import com.example.myweather.data.ForecastItem
import com.example.myweather.data.ForecastResponse
import com.example.myweather.data.Main
import com.example.myweather.data.Weather
import com.example.myweather.data.Wind
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ForecastScreen(
    forecastList: List<ForecastItem>,
    isLoading: Boolean,
    cityName: String,
    fetchForecast: (String) -> Unit
) {
    var inputCity by remember { mutableStateOf("California") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CityInputField(inputCity) { newCity -> inputCity = newCity }
        Spacer(modifier = Modifier.height(8.dp))
        FetchForecastButton(inputCity) { fetchForecast(inputCity) }
        Spacer(modifier = Modifier.height(16.dp))
        ForecastContent(cityName, isLoading, forecastList)
    }
}

@Composable
fun CityInputField(city: String, onCityChange: (String) -> Unit) {
    OutlinedTextField(
        value = city,
        onValueChange = onCityChange,
        label = { Text("Enter City") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun FetchForecastButton(city: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = city.isNotBlank(),
        modifier = Modifier.fillMaxWidth().height(48.dp)
    ) {
        Text("Get Weather Forecast")
    }
}

@Composable
fun LoadingIndicator() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ForecastContent(cityName: String, isLoading: Boolean, forecastList: List<ForecastItem>) {
    when {
        isLoading -> LoadingIndicator()
        forecastList.isNotEmpty() -> ForecastList(forecastList)
        cityName.isNotEmpty() -> Text(text = "No data available for $cityName")
    }
}

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ForecastList(forecastList: List<ForecastItem>) {
    val groupedForecasts = groupForecastsByDay(forecastList)

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        groupedForecasts.forEach { (day, forecasts) ->
            stickyHeader {
                DayHeader(day)
            }
            items(forecasts) { forecastItem ->
                ForecastListItem(forecastItem)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ForecastListItem(forecastItem: ForecastItem) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f)
                        )
                    )
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                AsyncImage(
                    model = "https://openweathermap.org/img/wn/${forecastItem.weather[0].icon}@2x.png",
                    contentDescription = null,
                    modifier = Modifier.size(64.dp).padding(end = 16.dp)
                )
                Column {
                    TemperatureText(forecastItem.main.temp)
                    Spacer(modifier = Modifier.height(4.dp))
                    DescriptionText(forecastItem.weather[0].description)
                }
            }
            TimeText(formatTime(forecastItem.dtTxt))
        }
    }
}

@Composable
fun TemperatureText(temp: Double) {
    Text(
        text = "${temp}°C",
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
fun DescriptionText(description: String) {
    Text(
        text = description.replaceFirstChar { it.uppercase() },
        style = MaterialTheme.typography.bodySmall.copy(
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
    )
}

@Composable
fun TimeText(time: String) {
    Text(
        text = time,
        style = MaterialTheme.typography.headlineSmall.copy(
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
fun DayHeader(day: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                    )
                )
            )
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        Text(
            text = day,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ForecastScreenPreview() {
    val mockForecastList = listOf(
        ForecastItem(
            dt = 1631800800,
            main = Main(temp = 18.5, feels_like = 18.0, temp_min = 18.0, temp_max = 19.0, pressure = 1015, humidity = 72),
            weather = listOf(Weather(id = 800, main = "Clear", description = "clear sky", icon = "01d")),
            clouds = Clouds(all = 10),
            wind = Wind(speed = 3.5, deg = 200, gust = null),
            visibility = 10000,
            dtTxt = "2024-11-16 09:00:00"
        ),
        ForecastItem(
            dt = 1631815200,
            main = Main(temp = 20.0, feels_like = 19.5, temp_min = 19.0, temp_max = 21.0, pressure = 1013, humidity = 65),
            weather = listOf(Weather(id = 801, main = "Clouds", description = "few clouds", icon = "02d")),
            clouds = Clouds(all = 20),
            wind = Wind(speed = 4.0, deg = 210, gust = 6.0),
            visibility = 10000,
            dtTxt = "2024-11-16 12:00:00"
        ),
        ForecastItem(
            dt = 1631829600,
            main = Main(temp = 17.5, feels_like = 17.0, temp_min = 16.5, temp_max = 18.5, pressure = 1017, humidity = 80),
            weather = listOf(Weather(id = 500, main = "Rain", description = "light rain", icon = "10d")),
            clouds = Clouds(all = 75),
            wind = Wind(speed = 2.5, deg = 180, gust = 4.5),
            visibility = 8000,
            dtTxt = "2024-11-16 15:00:00"
        ),
        ForecastItem(
            dt = 1631844000,
            main = Main(temp = 14.0, feels_like = 13.5, temp_min = 13.0, temp_max = 15.0, pressure = 1020, humidity = 85),
            weather = listOf(Weather(id = 802, main = "Clouds", description = "scattered clouds", icon = "03d")),
            clouds = Clouds(all = 50),
            wind = Wind(speed = 3.0, deg = 190, gust = 5.0),
            visibility = 10000,
            dtTxt = "2024-11-16 18:00:00"
        ),
        ForecastItem(
            dt = 1631858400,
            main = Main(temp = 10.5, feels_like = 10.0, temp_min = 9.5, temp_max = 11.5, pressure = 1023, humidity = 90),
            weather = listOf(Weather(id = 803, main = "Clouds", description = "broken clouds", icon = "04n")),
            clouds = Clouds(all = 60),
            wind = Wind(speed = 2.0, deg = 170, gust = 3.5),
            visibility = 9000,
            dtTxt = "2024-11-16 21:00:00"
        )
    )

    MaterialTheme {
        ForecastScreen(
            isLoading = false,
            cityName = "Sample City",
            forecastList = mockForecastList,
            fetchForecast = {}
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun groupForecastsByDay(forecastList: List<ForecastItem>): Map<String, List<ForecastItem>> {
    return forecastList.groupBy { forecastItem ->
        val dateTime = LocalDateTime.parse(
            forecastItem.dtTxt,
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        )
        if (dateTime.toLocalDate() == LocalDateTime.now().toLocalDate()) {
            "Today"
        } else {
            dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatTime(dtTxt: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val dateTime = LocalDateTime.parse(dtTxt, formatter)
    return dateTime.format(
        DateTimeFormatter.ofPattern(
            "h:mm a",
            Locale.getDefault()
        )
    )
}
