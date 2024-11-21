package com.example.weatherreport.Activity


import android.R
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.weatherreport.databinding.ActivityMainBinding
import com.google.gson.Gson
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import org.json.JSONObject
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    fun shortDayName(day: String): String {
        val result = when(day){
            "понедельник" -> "Пн"
            "вторник" -> "Вт"
            "среда" -> "Ср"
            "четверг" -> "Чт"
            "пятница" -> "Пт"
            "суббота" -> "Сб"
            "воскресенье" -> "Вс"
            else -> "null"
        }
        return result
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        enableEdgeToEdge()
        setContentView(view)


        val lat = intent.getStringExtra("lat")
        val long = intent.getStringExtra("long")
        
        getJsonDataNow(lat, long)
        getJsonDataForecast(lat, long)
    }

    private fun getJsonDataNow(lat: String?, long: String?) {
        val API_KEY = "4092c6357241c95b09fc66605900de08"
        val queue = Volley.newRequestQueue(this)
        val url = "https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${long}&appid=${API_KEY}&units=metric&lang=ru"
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            {
                response ->
                setValues(response)
            },
            {
                Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show() })
        queue.add(jsonRequest)
    }

    private fun setValues(response: JSONObject){
        binding.city.text = response.getString("name")
        binding.weather.text = response.getJSONArray("weather").getJSONObject(0).getString("description")
        var temp = response.getJSONObject("main").getString("temp")
        var tempConverted = temp.toFloat().roundToInt().toString()
        binding.tempText.text = "${tempConverted}°C"
        var hum = response.getJSONObject("main").getString("humidity")
        binding.humidityText.text = "${hum}%"
        var windspeed = response.getJSONObject("wind").getString("speed")
        binding.windText.text = "${windspeed} Км/ч"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getJsonDataForecast(lat: String?, long: String?) {
        val API_KEY = "4092c6357241c95b09fc66605900de08"
        val queue = Volley.newRequestQueue(this)
        val url = "https://api.openweathermap.org/data/2.5/forecast?lat=${lat}&lon=${long}&appid=${API_KEY}&units=metric&lang=ru"
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            {
                    response ->
                setValuesForecast(response)
            },
            {
                Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show() })
        queue.add(jsonRequest)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setValuesForecast(response: JSONObject){

        var serialization = Json { ignoreUnknownKeys = true}
        val tempArray = serialization.decodeFromString<WeatherResponse>(response.toString())



        var c : Calendar = Calendar.getInstance();
        val dayOne = tempArray.list.slice(0..7)
        val dayTwo = tempArray.list.slice(8..15)
        val dayThree = tempArray.list.slice(16..23)
        val dayFour = tempArray.list.slice(24..31)
        val dayFive = tempArray.list.slice(32..39)

        binding.dayOneMaxTemp.text = dayOne.maxBy { it.main.tempMax }.main.tempMax.roundToInt().toString() + "°C"
        binding.dayOneMinTemp.text = dayOne.minBy { it.main.tempMin }.main.tempMin.roundToInt().toString() + "°C"

        binding.dayTwoMaxTemp.text = dayTwo.maxBy { it.main.tempMax }.main.tempMax.roundToInt().toString() + "°C"
        binding.dayTwoMinTemp.text = dayTwo.minBy { it.main.tempMin }.main.tempMin.roundToInt().toString() + "°C"

        var dateStr= dayThree[0].dtTxt.take(10)
        var date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        var langRu = Locale("ru")
        var day = date.dayOfWeek.getDisplayName(TextStyle.FULL, langRu)
        binding.dayThreeDay.text = shortDayName(day)
        binding.dayThreeMaxTemp.text = dayThree.maxBy { it.main.tempMax }.main.tempMax.roundToInt().toString() + "°C"
        binding.dayThreeMinTemp.text = dayThree.minBy { it.main.tempMin }.main.tempMin.roundToInt().toString() + "°C"

        dateStr= dayFour[0].dtTxt.take(10)
        date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        langRu = Locale("ru")
        day = date.dayOfWeek.getDisplayName(TextStyle.FULL, langRu)
        binding.dayFourDay.text = shortDayName(day)
        binding.dayFourMaxTemp.text = dayFour.maxBy { it.main.tempMax }.main.tempMax.roundToInt().toString() + "°C"
        binding.dayFourMinTemp.text = dayFour.minBy { it.main.tempMin }.main.tempMin.roundToInt().toString() + "°C"

        dateStr= dayFive[0].dtTxt.take(10)
        date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        langRu = Locale("ru")
        day = date.dayOfWeek.getDisplayName(TextStyle.FULL, langRu)
        binding.dayFiveDay.text = shortDayName(day)
        binding.dayFiveMaxTemp.text = dayFive.maxBy { it.main.tempMax }.main.tempMax.roundToInt().toString() + "°C"
        binding.dayFiveMinTemp.text = dayFive.minBy { it.main.tempMin }.main.tempMin.roundToInt().toString() + "°C"
    }

}