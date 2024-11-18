package com.example.weatherreport.Activity


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
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


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
        var tempConverted = temp.toFloat().toInt().toString()
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
            { Log.e("badbadbad", it.toString())
                Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show() })
        queue.add(jsonRequest)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setValuesForecast(response: JSONObject){
//        var temp = response.getJSONArray("list").getJSONObject(0).getJSONObject("main").getString("temp_max")
//        var tempConverted = temp.toFloat().toInt().toString()
//        binding.dayOneMaxTemp.text = "${tempConverted}°C"
//        temp = response.getJSONArray("list").getJSONObject(0).getJSONObject("main").getString("temp_min")
//        tempConverted = temp.toFloat().toInt().toString()
//        binding.dayOneMinTemp.text = "${tempConverted}°C"
//
////        temp = response.getJSONArray("list").getJSONObject(0).getJSONObject("main").getString("temp_max")
////        tempConverted = temp.toFloat().toInt().toString()
////        binding.dayOneMaxTemp.text = "${tempConverted}°C"
//
//        var dateStr = response.getJSONArray("list").getJSONObject(0).getString("dt_txt").take(10)
//        var date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        var serialization = Json { ignoreUnknownKeys = true}
        val tempArray: ArrayList<cForecast> = serialization.decodeFromString(response.toString())
    }

}