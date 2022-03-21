package com.example.billing.utils.datas

import androidx.room.TypeConverter
import com.example.billing.utils.RememberState
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.reflect.TypeToken

data class MovDirection(
    val name: String,
    val type: Boolean
) {
    fun getState(): MovDirectionState = MovDirectionState(
        name = RememberState(name),
        type = RememberState(type)
    )
}

data class MovDirectionState(
    @Expose val name: RememberState<String>,
    @Expose val type: RememberState<Boolean>
) {

    companion object {
        fun create(name: String, type: Boolean) =
            MovDirectionState(RememberState(name), RememberState(type))

        val All = create("无", false)

        //渠道
        val ALiPay = create("支付宝", false)
        val WeCharPay = create("微信支付", false)
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            !is MovDirectionState -> false
            else -> this === other ||
                    other.name.value == "无" ||
                    name.value == other.name.value && type.value == other.type.value
        }
    }

    fun getData() = MovDirection(
        name = name.value,
        type = type.value
    )
}

class MovDirectionConverters {
    @TypeConverter
    fun stringToObject(value: String?): MovDirection? {
        val it = object : TypeToken<MovDirection>() {}.type
        return Gson().fromJson(value, it)
    }

    @TypeConverter
    fun objectToString(value: MovDirection?): String? {
        val gson = Gson()
        return gson.toJson(value)
    }
}