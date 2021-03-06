package com.example.a15764.xiaochaoweather.util

import android.text.TextUtils
import android.util.Log
import com.example.a15764.xiaochaoweather.db.City
import com.example.a15764.xiaochaoweather.db.County
import com.example.a15764.xiaochaoweather.db.Province
import com.example.a15764.xiaochaoweather.gson.WeatherData
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.AnkoLogger
import org.json.JSONArray
import org.json.JSONException


/**
 * 与服务器进行交互，发送http请求，注册一个回调来处理响应
 */
fun sendOkHttpRequest(address: String,callback:okhttp3.Callback){
    val client = okhttp3.OkHttpClient()
    val request = okhttp3.Request.Builder().url(address).build()
    client.newCall(request).enqueue(callback)
}

/**
 * 解析和处理服务器返回的省级数据
 */
fun handleProvinceReponse(response: String):Boolean{
    if (!android.text.TextUtils.isEmpty(response)){
        try {
            val allProvinces = org.json.JSONArray(response)
            //Log.d("utils","pro:${allProvinces.length()}")
            for (i in 0..allProvinces.length()-1){
                val provenceObject = allProvinces.getJSONObject(i)
                val province = com.example.a15764.xiaochaoweather.db.Province()
                province.provinceName = provenceObject.getString("name")
                province.provinceCode = provenceObject.getInt("id")
                province.save()
            }
            return true
        }
        catch (e: org.json.JSONException){
            e.printStackTrace()
        }
    }
    return false
}

/**
 * 解析和处理服务器返回的市级数据
 */
fun handleCityReponse(response: String, provinceId: Int):Boolean{
    if (!android.text.TextUtils.isEmpty(response)){
        try {
            val allCitys = org.json.JSONArray(response)
            //Log.d("utils","city:${allCitys.length()}")
            for (i in 0..allCitys.length()-1){
                val cityObject = allCitys.getJSONObject(i)
                val city = com.example.a15764.xiaochaoweather.db.City()
                city.cityName = cityObject.getString("name")
                city.cityCode = cityObject.getInt("id")
                city.provinceId = provinceId
                city.save()
            }
            return true
        }
        catch (e: org.json.JSONException){
            e.printStackTrace()
        }
    }
    return false
}

/**
 * 解析和处理服务器返回的县级数据
 */
fun handleCountyReponse(response: String, cityId: Int):Boolean{
    if (!android.text.TextUtils.isEmpty(response)){
        try {
            val allCounties = org.json.JSONArray(response)
            //Log.d("utils","country:${allCounties.length()}")
            for (i in 0..allCounties.length()-1){
                val countryObject = allCounties.getJSONObject(i)
                val country = com.example.a15764.xiaochaoweather.db.County()
                country.countyName = countryObject.getString("name")
                country.weatherId = countryObject.getString("weather_id")
                country.cityId = cityId
                country.save()
            }
            return true
        }
        catch (e: org.json.JSONException){
            e.printStackTrace()
        }
    }
    return false
}

/**
 * 将返回的weather解析成实体类
 */
fun handleWeatherResponse(response: String?):WeatherData?{
    try {
        //Log.d("utils",response)
        return Gson().fromJson(response, WeatherData::class.java)
    }
    catch(e:Exception){
        e.printStackTrace()
    }
    return null
}