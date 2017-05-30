package com.example.a15764.xiaochaoweather.activitys

import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide

import com.example.a15764.xiaochaoweather.R
import com.example.a15764.xiaochaoweather.gson.WeatherData
import com.example.a15764.xiaochaoweather.util.handleWeatherResponse
import com.example.a15764.xiaochaoweather.util.sendOkHttpRequest
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.foercast.*
import kotlinx.android.synthetic.main.now.*
import kotlinx.android.synthetic.main.title.*
import kotlinx.android.synthetic.main.aqi.*
import kotlinx.android.synthetic.main.suggestion.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import java.io.IOException

class WeatherActivity : AppCompatActivity() {
    val key = "6379fa59b03042e1b75d4c3e27da69c0"
    val backgrounPicApiUrl = "http://guolin.tech/api/bing_pic"
    var mWeatherId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= 21){
            val decorView = window.decorView
            // 这俩表示活动的布局会显示在状态栏上面
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.statusBarColor = Color.TRANSPARENT
        }

        setContentView(R.layout.activity_weather)
        swipe_refresh.setColorSchemeColors(R.color.colorPrimary)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val weatherString = prefs.getString("weather", null)
        if (weatherString != null){
            //有缓存
            val weather = handleWeatherResponse(weatherString)
            mWeatherId = weather?.heweather5?.get(0)?.basic?.id
            showWeatherInfo(weather)
        }
        else{
            //没缓存
            val cityId = intent.getStringExtra("city_id")
            weather_layout.visibility = View.INVISIBLE
            requestWeather(cityId)
        }

        swipe_refresh.setOnRefreshListener {
            requestWeather(mWeatherId)
        }

        nav_button.setOnClickListener {
            drawer_layout.openDrawer(Gravity.START)
        }

        val backgroundPic = prefs.getString("background_pic", null)
        if (backgroundPic != null){
            Glide.with(this@WeatherActivity).load(backgroundPic).into(background_pic)
        }
        else{
            loadBackgroundPic()
        }
    }

    /**
     * 根据城市id请求天气信息
     */
    fun requestWeather(cityId:String?){

     //   "https://free-api.heweather.com/v5/weather?city=beijing&key=6379fa59b03042e1b75d4c3e27da69c0"
        val weatherUrl = "https://free-api.heweather.com/v5/weather?city=$cityId&key=$key"
        sendOkHttpRequest(weatherUrl, object : Callback {
            override fun onResponse(call: Call?, response: Response?) {
                val responseText = response?.body()?.string()
                val weather = handleWeatherResponse(responseText)
                runOnUiThread {
                    if (weather != null && "ok".equals(weather.heweather5?.get(0)?.status)){
                        val editor = PreferenceManager.getDefaultSharedPreferences(this@WeatherActivity).edit()
                        //缓存数据在这里存下了
                        editor.putString("weather", responseText)
                        editor.apply()
                        showWeatherInfo(weather)
                    }
                    else{
                        toast("获取天气信息失败")
                    }
                    //刷新事件结束
                    swipe_refresh.isRefreshing = false
                }

            }

            override fun onFailure(call: Call?, e: IOException?) {
                e?.printStackTrace()
                runOnUiThread {
                    toast("获取天气信息失败")
                    //刷新事件结束
                    swipe_refresh.isRefreshing = false
                }
            }
        })
        loadBackgroundPic()
    }

    /**
     *处理并展示weatherData中的数据
     */
    private fun showWeatherInfo(weather:WeatherData?){
        val cityName = weather?.heweather5?.get(0)?.basic?.city
        val updateTime = weather?.heweather5?.get(0)?.basic?.update?.loc?.split(" ")?.get(1)
        val degree = weather?.heweather5?.get(0)?.now?.tmp + "℃"
        val weatherInfo = weather?.heweather5?.get(0)?.now?.cond?.txt

        title_city.text = cityName
        title_update_time.text = updateTime
        degree_text.text = degree
        weather_info_text.text = weatherInfo

        forecast_layout.removeAllViews()
        weather?.heweather5?.get(0)?.dailyForecast?.forEach {
            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecast_layout, false)
            val dateText = view.find<TextView>(R.id.date_text)
            val infoText = view.find<TextView>(R.id.info_text)
            val maxText = view.find<TextView>(R.id.max_text)
            val minText = view.find<TextView>(R.id.min_text)
            dateText.text = it.date
            infoText.text = it.cond?.txtD
            maxText.text = it.tmp?.max
            minText.text = it.tmp?.min
            forecast_layout.addView(view)
        }

        if (weather?.heweather5?.get(0)?.aqi != null){
            aqi_text.text = weather.heweather5?.get(0)?.aqi?.city?.aqi
            pm25_text.text = weather.heweather5?.get(0)?.aqi?.city?.pm25
        }
        else{
            aqi_text.text = "暂无"
            pm25_text.text = "暂无"
        }

        comfort_text.text = "舒适度：${weather?.heweather5?.get(0)?.suggestion?.comf?.txt}"
        car_wash_text.text = "洗车指数：${weather?.heweather5?.get(0)?.suggestion?.cw?.txt}"
        sport_text.text = "运动建议：${weather?.heweather5?.get(0)?.suggestion?.sport?.txt}"
        weather_layout.visibility = View.VISIBLE
    }

    /**
     * 加载背景图
     */
    private fun loadBackgroundPic(){
        sendOkHttpRequest(backgrounPicApiUrl, object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                e?.printStackTrace()
            }

            override fun onResponse(call: Call?, response: Response?) {
                val backgroundPic = response?.body()?.string()
                val editor = PreferenceManager.getDefaultSharedPreferences(this@WeatherActivity).edit()
                editor.putString("background_pic", backgroundPic)
                editor.apply()
                runOnUiThread {
                    Glide.with(this@WeatherActivity).load(backgroundPic).into(background_pic)
                }
            }
        })
    }


}
