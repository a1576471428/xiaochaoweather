package com.example.a15764.xiaochaoweather.util

import android.text.TextUtils
import com.example.a15764.xiaochaoweather.db.City
import com.example.a15764.xiaochaoweather.db.County
import com.example.a15764.xiaochaoweather.db.Province
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONException


/**
 * 与服务器进行交互，发送http请求，注册一个回调来处理响应
 */
public fun sendOkHttpRequest(address: String,callback:okhttp3.Callback){
    val client = okhttp3.OkHttpClient()
    val request = okhttp3.Request.Builder().url(address).build()
    client.newCall(request).enqueue(callback)
}

/**
 * 解析和处理服务器返回的省级数据
 */
public fun handleProvinceReponse(response: String):Boolean{
    if (!android.text.TextUtils.isEmpty(response)){
        try {
            val allProvinces = org.json.JSONArray(response)
            for (i in 0..allProvinces.length()){
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
public fun handleCityReponse(response: String, provinceId: Int):Boolean{
    if (!android.text.TextUtils.isEmpty(response)){
        try {
            val allCitys = org.json.JSONArray(response)
            for (i in 0..allCitys.length()){
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
public fun handleCountyReponse(response: String, cityId: Int):Boolean{
    if (!android.text.TextUtils.isEmpty(response)){
        try {
            val allCounties = org.json.JSONArray(response)
            for (i in 0..allCounties.length()){
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
