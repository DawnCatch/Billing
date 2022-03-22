package com.example.billing.utils.datas

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.billing.R
import com.example.billing.utils.RememberState
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.reflect.TypeToken

@Entity(tableName = "detailTypes")
data class DetailType(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id", typeAffinity = ColumnInfo.INTEGER)
    val id: Int? = null,
    val name: String,
    val triad: Boolean,
    val icon: Int = R.drawable.ic_other,
    var diy: Boolean = false
) {
    fun getState(): DetailTypeState = DetailTypeState(
        id = id,
        name = RememberState(name),
        triad = RememberState(triad),
        icon = RememberState(icon),
        diy = diy
    )
}

data class DetailTypeState(
    @Expose var id: Int? = null,
    @Expose val name: RememberState<String>,
    @Expose val triad: RememberState<Boolean>,
    @Expose val icon: RememberState<Int> = RememberState<Int>(R.drawable.ic_other),
    @Expose var diy: Boolean = false
) {
    companion object {
        var index = 0
        fun create(
            id: Int? = null,
            name: String,
            triad: Boolean,
            icon: Int = R.drawable.ic_other,
            diy: Boolean = false
        ) = DetailTypeState(name = RememberState(name), triad = RememberState(triad), icon = RememberState(icon), diy = diy)

        fun SelfSubscribe() = create(name = "自定义:${index++}", triad = true, diy = true)

        val All = create(id = null,"全部", true)

        val UpAll = create(id = null,"符号全部", true)
        val DownAll = create(id = null,"符号全部", false)

        //收入
        val Borrowing = create(id = 1,"借入", true)
        val Wage = create(id = 2,"工资", true)

        //支出
        val Lending = create(id = 3,"借出", false)
        val Play = create(id = 4,"娱乐", false)
        val Eat = create(id = 5,"饮食", false)

        fun Positive() = mutableListOf<DetailTypeState>(Borrowing, Wage)

        fun NegativeNumber() = mutableListOf<DetailTypeState>(Lending, Play, Eat)
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            !is DetailTypeState -> false
            else -> this === other ||
//                    (other.name.value == "全部" && triad == other.triad) ||
                    (name == other.name && triad == other.triad)
        }
    }

    fun getData() = DetailType(
        id = id,
        name = name.value,
        triad = triad.value,
        icon = icon.getState().value,
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