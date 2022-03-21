package com.example.billing.utils.datas

import androidx.room.TypeConverter
import com.example.billing.utils.RememberState
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.reflect.TypeToken
import java.util.*

fun intToTime(value: Int?) = try {
    val time = Time(
        year = value.toString().substring(0, 4).toInt(),
        month = value.toString().substring(4, 6).toInt(),
        dayOfMonth = value.toString().substring(6, 8).toInt(),
        dayOfWeek = value.toString().substring(8).toInt(),
    )
    time
} catch (e: Exception) {
    null
}

fun StringToTime(value: String?) = try {
    val date = value!!.split("/")
    Time(date[0].toInt(), date[1].toInt(), date[2].toInt(), getDayOfWeek(date))
} catch (e: Exception) {
    null
}

fun getDayOfWeek(date: List<String>): Int {
    val tms = Calendar.getInstance()
    tms.set(date[0].toInt(), date[1].toInt() - 1, date[2].toInt())
    return if (tms.get(Calendar.DAY_OF_WEEK) - 1 == 0) {
        7
    } else {
        tms.get(Calendar.DAY_OF_WEEK) - 1
    }
}

fun getTimeOfToday():TimeState {
    val tms = Calendar.getInstance()
    return TimeState(
        RememberState(tms.get(Calendar.YEAR)),
        RememberState(tms.get(Calendar.MONTH)+1),
        RememberState(tms.get(Calendar.DAY_OF_MONTH)),
        RememberState(
            if(tms.get(Calendar.DAY_OF_WEEK)-1 == 0) {
                7
            }else tms.get(Calendar.DAY_OF_WEEK)-1
        ) //日,一,二,三,四,五,六   1,2,3,4,5,6,7
    )
}

data class Time(
    val year: Int,
    val month: Int,
    val dayOfMonth: Int,
    val dayOfWeek: Int
) {
    fun getState(): TimeState = TimeState(
        year = RememberState(year),
        month = RememberState(month),
        dayOfMonth = RememberState(dayOfMonth),
        dayOfWeek = RememberState(dayOfWeek)
    )

    fun toDate() = "$year/$month/$dayOfMonth"

    fun toInt(): Int {
        var yearToInt = ""
        for (i in 0 until 4 - year.toString().length) {
            yearToInt += "0"
        }
        yearToInt += year.toString()
        val monthToInt = if (month < 10) "0$month" else "$month"
        val dayToInt = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
        return "$yearToInt$monthToInt$dayToInt$dayOfWeek".toInt()
    }
}

data class TimeState(
    @Expose val year: RememberState<Int>,
    @Expose val month: RememberState<Int>,
    @Expose val dayOfMonth: RememberState<Int>,
    @Expose val dayOfWeek: RememberState<Int>
) {
    override fun toString(): String {
        return "${year.getState().value}/${month.getState().value}/${dayOfMonth.getState().value}"
    }

    fun toInt(): Int {
        val monthToInt =
            if (month.getState().value < 10) "0${month.getState().value}" else "${month.getState().value}"
        val dayToInt =
            if (dayOfMonth.getState().value < 10) "0${dayOfMonth.getState().value}" else "${dayOfMonth.getState().value}"
        return "${year.getState().value}$monthToInt$dayToInt".toInt()
    }

    override fun equals(other: Any?): Boolean {
        return other.toString() == this.toString()
    }

    fun getData() = Time(
        year = year.value,
        month = month.value,
        dayOfMonth = dayOfMonth.value,
        dayOfWeek = dayOfWeek.value
    )

    override fun hashCode(): Int {
        var result = year.hashCode()
        result = 31 * result + month.hashCode()
        result = 31 * result + dayOfMonth.hashCode()
        result = 31 * result + dayOfWeek.hashCode()
        return result
    }
}

class TimeConverters {
    @TypeConverter
    fun StringToObject(value: String?): Time? = StringToTime(value)

    @TypeConverter
    fun objectToString(value: Time?): String? = value?.toDate()
}