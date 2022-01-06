package com.bysofy.bydaily

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView

import android.view.View
import com.bysofy.bydaily.design.decoratior.MathTeacher
import com.bysofy.bydaily.design.decoratior.PeTeacher
import com.bysofy.bydaily.design.decoratior.Student
import com.bysofy.bydaily.design.proxyLsn.ByRelSub
import com.bysofy.bydaily.design.proxyLsn.IShop
import com.bysofy.bydaily.design.proxyLsn.ProxyEr
import java.time.chrono.IsoEra


class MainActivity : AppCompatActivity() {
    private var lv_one: ListView? = null
    private var lv_two: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initProxy()
        initAdapter()
        initDecorator()
    }

    private fun initDecorator() {
        val student = Student()
        val mathTeacher = MathTeacher(student)
        val peTeacher = PeTeacher(student)
        mathTeacher.attackMagic()
        peTeacher.attackMagic()
    }

    private fun initAdapter() {
        lv_one = findViewById<View>(R.id.lv_one) as ListView
        lv_two = findViewById<View>(R.id.lv_two) as ListView
        val strs1 =
            arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15")
        val adapter1 = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, strs1)
        lv_one!!.adapter = adapter1

        val strs2 =
            arrayOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O")
        val adapter2 = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, strs2)
        lv_two!!.adapter = adapter2
    }

    /**
     * 代理模式
     */
    private fun initProxy() {
        val byShop: IShop = ByRelSub()
        val proxyBy: IShop = ProxyEr(byShop)
        proxyBy.buyHouse()
    }
}