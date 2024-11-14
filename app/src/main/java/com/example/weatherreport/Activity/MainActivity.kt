package com.example.weatherreport.Activity


import android.graphics.RenderEffect
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.weatherreport.databinding.ActivityMainBinding
import eightbitlab.com.blurview.RenderScriptBlur
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        enableEdgeToEdge()
        setContentView(view)


        val lat = intent.getStringExtra("lat")
        val long = intent.getStringExtra("long")
        
        getJsonData(lat, long)
    }

    private fun getJsonData(lat: String?, long: String?) {
        val API_KEY = "4092c6357241c95b09fc66605900de08"
        val queue = Volley.newRequestQueue(this)
        val url = "https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${long}&appid=${API_KEY}&units=metric&lang=ru"
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            {
                response ->
                setValues(response)
            },
            { Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show() })
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


}