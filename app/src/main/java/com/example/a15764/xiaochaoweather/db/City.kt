package com.example.a15764.xiaochaoweather.db

import org.litepal.crud.DataSupport

/**
 * Created by 15764 on 2017-05-20.
 */
class City:DataSupport() {
    var id = 0
    var cityName = "no name"
    var cityCode = 0
    var provinceId = 0
}