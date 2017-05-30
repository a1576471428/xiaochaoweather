package com.example.a15764.xiaochaoweather.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock
import android.preference.PreferenceManager
import com.example.a15764.xiaochaoweather.util.handleWeatherResponse
import com.example.a15764.xiaochaoweather.util.sendOkHttpRequest
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class AutoUpdateService : Service() {
    val key = "6379fa59b03042e1b75d4c3e27da69c0"
    val backgrounPicApiUrl = "http://guolin.tech/api/bing_pic"

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        updateWeather()
        updateBackgroundPic()
        val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val anHour = 8*60*60*1000
        val triggerAtTime = SystemClock.elapsedRealtime() + anHour
        val i = Intent(this, AutoUpdateService::class.java)
        val pi = PendingIntent.getService(this, 0, i, 0)
        manager.cancel(pi)
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi)
        return super.onStartCommand(intent, flags, startId)
    }

    /**
     * 更新天气信息
     */
    private fun updateWeather() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val weatherString = prefs.getString("weather", null)
        if (weatherString != null) {
            //有缓存直接解析天气数据
            val weather = handleWeatherResponse(weatherString)
            val cityId = weather?.heweather5?.get(0)?.basic?.id
            val weatherUrl = "https://free-api.heweather.com/v5/weather?city=$cityId&key=$key"
            sendOkHttpRequest(weatherUrl, object :Callback{
                override fun onResponse(call: Call?, response: Response?) {
                    val responseText = response?.body()?.string()
                    val weather = handleWeatherResponse(responseText)
                    if (weather != null && "ok".equals(weather.heweather5?.get(0)?.status)) {
                        val editor = PreferenceManager.getDefaultSharedPreferences(this@AutoUpdateService).edit()
                        //缓存数据在这里存下了
                        editor.putString("weather", responseText)
                        editor.apply()
                    }
                }

                override fun onFailure(call: Call?, e: IOException?) {
                    e?.printStackTrace()
                }
            })
        }
    }

    /**
     * 更新每日一图
     */
    private fun updateBackgroundPic(){
        sendOkHttpRequest(backgrounPicApiUrl, object :Callback{
            override fun onFailure(call: Call?, e: IOException?) {
                e?.printStackTrace()
            }

            override fun onResponse(call: Call?, response: Response?) {
                val backgroundPic = response?.body()?.string()
                val editor = PreferenceManager.getDefaultSharedPreferences(this@AutoUpdateService).edit()
                editor.putString("background_pic", backgroundPic)
                editor.apply()
            }
        })
    }

}
