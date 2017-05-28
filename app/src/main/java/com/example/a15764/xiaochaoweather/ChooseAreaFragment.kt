package com.example.a15764.xiaochaoweather

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import com.example.a15764.xiaochaoweather.db.City
import com.example.a15764.xiaochaoweather.db.County
import com.example.a15764.xiaochaoweather.db.Province
import com.example.a15764.xiaochaoweather.util.handleCityReponse
import com.example.a15764.xiaochaoweather.util.handleCountyReponse
import com.example.a15764.xiaochaoweather.util.handleProvinceReponse
import com.example.a15764.xiaochaoweather.util.sendOkHttpRequest
import kotlinx.android.synthetic.main.fragment_choose_area.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.onUiThread
import org.jetbrains.anko.support.v4.toast
import org.litepal.crud.DataSupport
import java.io.IOException

class ChooseAreaFragment : Fragment() {

    val LEVEL_PROVINCE = 0
    val LEVEL_CITY = 1
    val LEVEL_COUNTRY = 2

    private val TAG = "ChooseAreaFragment"

    private var queryProgressDialog: ProgressDialog? = null
    private var titleText: TextView?= null
    private var backButtton: Button?= null
    private var listView: ListView?= null
    private var adapter: ArrayAdapter<String>? = null
    private var datalist = ArrayList<String>()
    //省列表
    private var provinceList: MutableList<Province>? = null
    //市列表
    private var cityList: MutableList<City>? = null
    //县列表
    private var countryList: MutableList<County>? = null
    //选中的省份
    private var selectedProvince: Province? = null
    //选中的城市
    private var selectedCity: City? = null
    //当前选中的级别
    private var currentLevel: Int? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_choose_area, container, false)
        titleText = view.find(R.id.title_text)
        backButtton = view.find(R.id.back_button)
        listView = view.findViewById(R.id.list_view) as ListView

        adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, datalist)
        listView?.adapter = adapter
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        listView?.setOnItemClickListener { parent, view, position, id ->
            if (currentLevel == LEVEL_PROVINCE) {
                selectedProvince = provinceList!!.get(position)
                queryCities()
            }
            else if (currentLevel == LEVEL_CITY){
                selectedCity = cityList!!.get(position)
                queryCounties()
            }
        }
        backButtton?.setOnClickListener {
            when(currentLevel){
                LEVEL_COUNTRY -> queryCities()
                LEVEL_CITY -> queryProvinces()
            }
        }
        queryProvinces()
    }

    /**
     * 查询全国所有的省，优先从数据库查询，如果没有再去服务器查询
     */
    private fun queryProvinces() {
        titleText?.text = "中国"
        backButtton?.visibility = View.GONE
        provinceList = DataSupport.findAll(Province::class.java)
        if (provinceList!!.size > 0) {
            datalist.clear()
            provinceList?.forEach {
                datalist.add(it.provinceName)
            }
            adapter?.notifyDataSetChanged()
            list_view.setSelection(0)
            currentLevel = LEVEL_PROVINCE
        } else {
            val address = "http://guolin.tech/api/china"
            queryFromServer(address, "province")
        }

    }


    private fun queryCities(){
        titleText?.text = selectedProvince!!.provinceName
        backButtton?.visibility = View.VISIBLE
        cityList = DataSupport.where("provinceid = ?", "${selectedProvince?.id}").find(City::class.java)
        if (cityList!!.size>0){
            datalist.clear()
            cityList?.forEach {
                datalist.add(it.cityName)
            }
            adapter?.notifyDataSetChanged()
            list_view.setSelection(0)
            currentLevel = LEVEL_CITY
        }
        else{
            val provinceCode = selectedProvince!!.provinceCode
            log(provinceCode.toString())
            val address = "http://guolin.tech/api/china/$provinceCode"
            queryFromServer(address, "city")
        }
    }


    private fun queryCounties(){
        titleText?.text = selectedCity?.cityName
        backButtton?.visibility = View.VISIBLE
        countryList = DataSupport.where("cityid = ?", "${selectedCity?.id}").find(County::class.java)
        if(countryList!!.size>0){
            datalist.clear()
            countryList?.forEach {
                datalist.add(it.countyName)
            }
            adapter?.notifyDataSetChanged()
            list_view.setSelection(0)
            currentLevel = LEVEL_COUNTRY
        }
        else{
            val provenceCode = selectedProvince?.provinceCode
            val cityCode = selectedCity?.cityCode
            val address = "http://guolin.tech/api/china/$provenceCode/$cityCode"
            log("$provenceCode and $cityCode")
            queryFromServer(address, "country")
        }

    }


    private fun queryFromServer(address: String, type: String) {
        showProcessDialog()
        sendOkHttpRequest(address, object :Callback{
            override fun onResponse(call: Call?, response: Response?) {
                val responseText = response!!.body().string()
                log(responseText)
                log(type)
                var result = false
                when(type) {
                    "province"-> result = handleProvinceReponse(responseText)
                    "city" -> result = handleCityReponse(responseText, selectedProvince!!.id)
                    "country" -> result = handleCountyReponse(responseText, selectedCity!!.id)
                }
                if (result){
                    onUiThread {
                        closeProgressDialog()
                        when(type){
                            "province" -> queryProvinces()
                            "city" -> queryCities()
                            "country" -> queryCounties()
                        }
                    }
                }
            }

            override fun onFailure(call: Call?, e: IOException?) {
                onUiThread {
                    closeProgressDialog()
                    toast("加载失败")
                }
            }
        })
    }


    private fun showProcessDialog() {
        if (queryProgressDialog == null){
            queryProgressDialog = ProgressDialog(activity)
        }
        queryProgressDialog?.setMessage("loading...")
        queryProgressDialog?.setCanceledOnTouchOutside(false)
        queryProgressDialog?.show()

    }

    private fun closeProgressDialog(){
        queryProgressDialog?.dismiss()
    }

    private fun log(message:String){
        Log.d(TAG, message)
    }
}
