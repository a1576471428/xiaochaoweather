package com.example.a15764.xiaochaoweather

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.a15764.xiaochaoweather.db.City
import com.example.a15764.xiaochaoweather.db.County
import com.example.a15764.xiaochaoweather.db.Province
import kotlinx.android.synthetic.main.fragment_choose_area.*


class ChooseAreaFragment : Fragment() {

    // TODO: Rename and change types of parameters
    val LEVEL_PROVINCE = 0
    val LEVEL_CITY = 1
    val LEVEL_COUNTRY = 2
    private var adapter: ArrayAdapter<String>?=null
    private var datalist = ArrayList<String>()
    //省列表
    private var provinceList: List<Province>?=null
    //市列表
    private var cityList: List<City>?=null
    //县列表
    private var countryList: List<County>?=null
    //选中的省份
    private var selectedProvince: Province?=null
    //选中的城市
    private var selectedCity: City?=null
    //当前选中的级别
    private var currentLevel: Int?=null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_choose_area, container, false)
        adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, datalist)
        list_view.adapter = adapter
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
  //      list_view.setOnItemClickListener { parent, view, position, id ->  }
    }
}
