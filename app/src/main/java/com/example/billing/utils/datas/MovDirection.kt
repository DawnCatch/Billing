package com.example.billing.utils.datas

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.billing.utils.RememberState
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.reflect.TypeToken

@Entity(tableName = "movDirections")
data class MovDirection(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id", typeAffinity = ColumnInfo.INTEGER)
    val id: Int? = null,
    val name: String,
    val type: Boolean
) {
    fun getState(): MovDirectionState = MovDirectionState(
        id = id,
        name = RememberState(name),
        type = RememberState(type)
    )
}

data class MovDirectionState(
    @Expose var id: Int? = null,
    @Expose val name: RememberState<String>,
    @Expose val type: RememberState<Boolean>
) {

    companion object {
        var index = 0

        fun create(id: Int? = null, name: String, type: Boolean) =
            MovDirectionState(id, RememberState(name), RememberState(type))

        fun SelfSubscribe(type: Boolean) = create(
            name = "自定义:${index++}",
            type = type
        )

        val All = create(1, "无", false)

        //渠道
        val ALiPay = create(2, "支付宝", false)
        val WeCharPay = create(3, "微信支付", false)
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            !is MovDirectionState -> false
            else -> this === other ||
                    name.value == other.name.value && type.value == other.type.value
        }
    }

    fun getData() = MovDirection(
        id = id,
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