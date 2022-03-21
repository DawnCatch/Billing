package com.example.billing.utils

import com.example.billing.R
import com.example.billing.utils.datas.DetailState
import com.google.gson.annotations.Expose

data class User(
    @Expose val name: RememberState<String> = RememberState("用户"),
    @Expose val details: MutableList<DetailState> = mutableListOf()
)

//data class MovDirection(
//    @Expose val name: RememberState<String>,
//    @Expose val type: RememberState<Boolean>,
//) {
//
//    companion object {
//        fun create(name: String, type: Boolean) =
//            MovDirection(RememberState(name), RememberState(type))
//
//        val All = create("无", false)
//
//        //渠道
//        val ALiPay = create("支付宝", false)
//        val WeCharPay = create("微信支付", false)
//    }
//
//    override fun equals(other: Any?): Boolean {
//        return when (other) {
//            !is MovDirection -> false
//            else -> this === other ||
//                    other.name.value == "无" ||
//                    name.value == other.name.value && type.value == other.type.value
//        }
//    }
//}
//
//fun getDetail() = Detail(
//    RememberState(getTime()),
//    RememberState(0.0),
//    RememberState(""),
//    RememberState(DetailType.All),
//    RememberState(MovDirection.All),
//    RememberState(MovDirection.All)
//)
//
//data class Detail(
//    @Expose val time: RememberState<Time>,
//    @Expose val money: RememberState<Double>,
//    @Expose val message: RememberState<String>,
//    @Expose val type: RememberState<DetailType>,
//    @Expose val direction: RememberState<MovDirection>,
//    @Expose val channel: RememberState<MovDirection>
//) {
//    fun Screened(screening: Screening): Boolean {
//        if (
//            timeContrast(time.value, screening.startTime)
//            && timeContrast(screening.endTime, time.value)
//        ) {
//            if (
//                money.value >= screening.minMoney.value
//                && money.value <= screening.maxMoney.value
//            ) {
//                if (
//                    screening.message.value == "" ||
//                    message.value.indexOf(screening.message.value, 0, false) != -1
//                ) {
//                    if (
//                        direction == screening.direction && channel == screening.channel
//                    ) {
//                        if (type == screening.type) {
//                            return true
//                        }
//                    }
//                }
//            }
//        }
//        return false
//    }
//}
//
//data class DetailType(
//    @Expose val name: RememberState<String>,
//    @Expose val triad: RememberState<Boolean>,
//    @Expose val icon: Int = R.drawable.ic_other,
//    @Expose var diy: Boolean = false
//) {
//    companion object {
//        fun create(
//            name: String,
//            triad: Boolean,
//            icon: Int = R.drawable.ic_other,
//            diy: Boolean = false
//        ) = DetailType(RememberState(name), RememberState(triad), icon, diy)
//
//        val All = create("全部", true)
//
//        //收入
//        val Borrowing = create("借入", true)
//        val Wage = create("工资", true)
//
//        //支出
//        val Lending = create("借出", false)
//        val Play = create("娱乐", false)
//        val Eat = create("饮食", false)
//
//        fun Positive() = mutableListOf<DetailType>(Borrowing, Wage)
//
//        fun NegativeNumber() = mutableListOf<DetailType>(Lending, Play, Eat)
//    }
//
//    override fun equals(other: Any?): Boolean {
//        return when (other) {
//            !is DetailType -> false
//            else -> this === other ||
//                    (other.name.value == "全部" && triad == other.triad) ||
//                    (name == other.name && triad == other.triad)
//        }
//    }
//}
//
//data class Time(
//    @Expose val year: RememberState<Int>,
//    @Expose val month: RememberState<Int>,
//    @Expose val dayOfMonth: RememberState<Int>,
//    @Expose val dayOfWeek: RememberState<Int>
//) {
//    override fun toString(): String {
//        return "${year.getState().value}/${month.getState().value}/${dayOfMonth.getState().value}"
//    }
//
//    fun toInt(): Int {
//        val monthToInt = if (month.getState().value < 10) "0${month.getState().value}" else "${month.getState().value}"
//        val dayToInt = if (dayOfMonth.getState().value < 10) "0${dayOfMonth.getState().value}" else "${dayOfMonth.getState().value}"
//        return "${year.getState().value}$monthToInt$dayToInt".toInt()
//    }
//
//    override fun equals(other: Any?): Boolean {
//        return other.toString() == this.toString()
//    }
//}
//
//enum class Type {
//    Borrowers,
//    Lenders,
//    Host
//}
//
//data class Screening(
//    @Expose val startTime: Time = getTime(1, 1, 1, 1),
//    @Expose val endTime: Time = getTime(Int.MAX_VALUE, 1, 1, 1),
//    @Expose var minMoney: RememberState<Double> = RememberState(0.0),
//    @Expose var maxMoney: RememberState<Double> = RememberState(Double.MAX_VALUE),
//    @Expose var message: RememberState<String> = RememberState(""),
//    @Expose var direction: RememberState<MovDirection> = RememberState(MovDirection.All),
//    @Expose var channel: RememberState<MovDirection> = RememberState(MovDirection.All),
//    @Expose val type: RememberState<DetailType> = RememberState(DetailType.All),
//)