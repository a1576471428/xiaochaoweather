package com.example.a15764.xiaochaoweather.activitys

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import com.example.a15764.xiaochaoweather.R
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        if (prefs.getString("weather", null) != null){
            startActivity<WeatherActivity>()
            finish()
        }
    }
}
