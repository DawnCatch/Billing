package com.example.billing.utils.datas

import androidx.room.TypeConverter
import com.example.billing.R
import com.example.billing.utils.RememberState
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.reflect.TypeToken

data class DetailType(
    val name: String,
    val triad: Boolean,
    val icon: Int = R.drawable.ic_other,
    var diy: Boolean = false
) {
    fun getState(): DetailTypeState = DetailTypeState(
        name = RememberState(name),
        triad = RememberState(triad),
        icon = icon,
        diy = diy
    )
}

data class DetailTypeState(
    @Expose val name: RememberState<String>,
    @Expose val triad: RememberState<Boolean>,
    @Expose val icon: Int = R.drawable.ic_other,
    @Expose var diy: Boolean = false
) {
    companion object {
        fun create(
            name: String,
            triad: Boolean,
            icon: Int = R.drawable.ic_other,
            diy: Boolean = false
        ) = DetailTypeState(RememberState(name), RememberState(triad), icon, diy)

        val All = create("全部", true)

        val UpAll = create("符号全部", true)
        val DownAll = create("符号全部", false)

        //收入
        val Borrowing = create("借入", true)
        val Wage = create("工资", true)

        //支出
        val Lending = create("借出", false)
        val Play = create("娱乐", false)
        val Eat = create("饮食", false)

        fun Positive() = mutableListOf<DetailTypeState>(Borrowing, Wage)

        fun NegativeNumber() = mutableListOf<DetailTypeState>(Lending, Play, Eat)
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            !is DetailTypeState -> false
            else -> this === other ||
                    (other.name.value == "全部" && triad == other.triad) ||
                    (name == other.name && triad == other.triad)
        }
    }

    fun getData() = DetailType(
        name = name.value,
        triad = triad.value,
        icon = icon,
        diy = diy
    )
}

class DetailTypeConverters {
    @TypeConverter
    fun stringToObject(value: String?): DetailType? {
        val it = object : TypeToken<DetailType>() {}.type
        return Gson().fromJson(value, it)
    }

    @TypeConverter
    fun objectToString(value: DetailType?): String? {
        val gson = Gson()
        return gson.toJson(value)
    }
}