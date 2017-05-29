package com.example.a15764.xiaochaoweather.gson

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Auto-generated: 2017-05-29 14:48:16
 *
 * 本人只是搬运工，本应该写成数据类的，但是在线只能生成javabean，将就着用了
 * @author www.jsons.cn
 * @website http://www.jsons.cn/json2java/
 */

class WeatherData {

    @SerializedName("HeWeather5")
    var heweather5: List<Heweather5>? = null

}


class City {

    var aqi: String? = null
    var co: String? = null
    var no2: String? = null
    var o3: String? = null
    var pm10: String? = null
    var pm25: String? = null
    var qlty: String? = null
    var so2: String? = null

}

class Aqi {

    var city: City? = null

}

class Update {

    var loc: String? = null
    var utc: String? = null

}

class Basic {

    var city: String? = null
    var cnty: String? = null
    var id: String? = null
    var lat: String? = null
    var lon: String? = null
    var update: Update? = null

}

class Astro {

    var mr: String? = null
    var ms: String? = null
    var sr: String? = null
    var ss: String? = null
}

class Cond {
    var code: String? = null

    var txt: String? = null

}

class Cond1 {

    @SerializedName("code_d")
    var codeD: String? = null
    @SerializedName("code_n")
    var codeN: String? = null
    @SerializedName("txt_d")
    var txtD: String? = null
    @SerializedName("txt_n")
    var txtN: String? = null

}

class Tmp {

    var max: String? = null
    var min: String? = null

}

class Wind {

    var deg: String? = null
    var dir: String? = null
    var sc: String? = null
    var spd: String? = null

}

class DailyForecast {

    var astro: Astro? = null
    var cond: Cond1? = null
    var date: String? = null
    var hum: String? = null
    var pcpn: String? = null
    var pop: String? = null
    var pres: String? = null
    var tmp: Tmp? = null
    var uv: String? = null
    var vis: String? = null
    var wind: Wind? = null

}

class HourlyForecast {

    var cond: Cond? = null
    var date: String? = null
    var hum: String? = null
    var pop: String? = null
    var pres: String? = null
    var tmp: String? = null
    var wind: Wind? = null

}

class Now {

    var cond: Cond? = null
    var fl: String? = null
    var hum: String? = null
    var pcpn: String? = null
    var pres: String? = null
    var tmp: String? = null
    var vis: String? = null
    var wind: Wind? = null

}

class Comf {

    var brf: String? = null
    var txt: String? = null

}

class Cw {

    var brf: String? = null
    var txt: String? = null

}

class Drsg {

    var brf: String? = null
    var txt: String? = null

}

class Flu {

    var brf: String? = null
    var txt: String? = null

}

class Sport {

    var brf: String? = null
    var txt: String? = null

}

class Trav {

    var brf: String? = null
    var txt: String? = null

}

class Uv {

    var brf: String? = null
    var txt: String? = null

}

class Suggestion {

    var comf: Comf? = null
    var cw: Cw? = null
    var drsg: Drsg? = null
    var flu: Flu? = null
    var sport: Sport? = null
    var trav: Trav? = null
    var uv: Uv? = null

}

class Heweather5 {

    var aqi: Aqi? = null
    var basic: Basic? = null
    @SerializedName("daily_forecast")
    var dailyForecast: List<DailyForecast>? = null
    @SerializedName("hourly_forecast")
    var hourlyForecast: List<HourlyForecast>? = null
    var now: Now? = null
    var status: String? = null
    var suggestion: Suggestion? = null

}
