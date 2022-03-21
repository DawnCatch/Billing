package com.example.billing.activitys

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.room.Room
import com.example.billing.utils.BillingData
import com.example.sport.utils.Local
import com.example.sport.utils.Local.LOCAL_DATA
import com.example.billing.utils.Screen
import com.example.billing.utils.Settings
import com.example.billing.utils.datas.BillingDatabase
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tencent.mmkv.MMKV

class Billing:Application() {
    companion object {
        lateinit var sSettings: Settings
        lateinit var sBillingData: BillingData
        lateinit var sGson: Gson
        lateinit var sPreferences: SharedPreferences
        lateinit var screen: Screen
        lateinit var db: BillingDatabase

        @SuppressLint("StaticFieldLeak")
        var sContext: Context? = null

        fun getsContext(): Context? {
            return sContext
        }

        fun saveData() {
            Local.setSetting(sSettings)
            Local.setBillingData(sBillingData)
        }
    }

    override fun onCreate() {
        super.onCreate()
        sContext = this

        sGson = GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()

        db = Room
            .databaseBuilder(sContext as Billing,BillingDatabase::class.java,"db_details")
            .build()

        sPreferences = getSharedPreferences(LOCAL_DATA, MODE_PRIVATE)

        MMKV.initialize(this)

        sSettings = Local.getSetting()
        sBillingData = Local.getBillingData()

        val wm = this.getSystemService(WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
        val width = dm.widthPixels // 屏幕宽度（像素）
        val height = dm.heightPixels // 屏幕高度（像素）
        val density = dm.density // 屏幕密度（0.75 / 1.0 / 1.5）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        val screenWidth = (width / density).toInt() // 屏幕宽度(dp)
        val screenHeight = (height / density).toInt() // 屏幕高度(dp)
        screen = Screen(screenWidth, screenHeight)
    }
}