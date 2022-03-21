package com.example.sport.utils

import android.content.SharedPreferences
import com.example.billing.activitys.Billing
import com.example.billing.utils.BillingData
import com.example.billing.utils.Settings

object Local {
    const val LOCAL_DATA = "local_data"
    val SETTINGS = "setting"
    val BILLINGDATA = "billing_data"

    fun getSetting(): Settings {
        val settingsString: String? =
            Billing.sPreferences.getString(SETTINGS, "")
        val settings: Settings? = Billing.sGson.fromJson(settingsString, Settings::class.java)
        return settings ?: Settings()
    }

    fun setSetting(settings: Settings) {
        val settingsGson = Billing.sGson.toJson(settings)
        val editor:SharedPreferences.Editor = Billing.sPreferences.edit()
        editor.putString(SETTINGS,settingsGson)
        editor.apply()
        Billing.sSettings = settings
    }

    fun getBillingData(): BillingData {
        val billingDataString:String? =
            Billing.sPreferences.getString(BILLINGDATA,"")
        val billingData: BillingData? = Billing.sGson.fromJson(billingDataString, BillingData::class.java)
        return billingData?: BillingData()
    }

    fun setBillingData(billingData: BillingData) {
        val billingDataGson = Billing.sGson.toJson(billingData)
        val editor:SharedPreferences.Editor = Billing.sPreferences.edit()
        editor.putString(BILLINGDATA,billingDataGson)
        editor.apply()
        Billing.sBillingData = billingData
    }
}