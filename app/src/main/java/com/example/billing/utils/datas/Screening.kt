package com.example.billing.utils.datas

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.billing.activitys.Billing
import com.example.billing.utils.RememberState
import com.google.gson.Gson
import com.google.gson.annotations.Expose

//data class Screening(
//    @Expose val startTime: String = "1/1/1",
//    @Expose val endTime: String = "5000/1/1",
//    @Expose var minMoney: Double = 0.0,
//    @Expose val maxMoney: Double = 9999999.0,
//    @Expose val message: String = "",
//    @Expose val type: DetailType = DetailTypeState.All.getData(),
//    @Expose val direction: MovDirection = MovDirectionState.All.getData(),
//    @Expose val channel: MovDirection = MovDirectionState.All.getData()
//) {
//    fun getScreened() = Billing.db.getDetailDao().queryWithAllValue(
//        startTime = startTime,
//        endTime = endTime,
//        minMoney = minMoney,
//        maxMoney = maxMoney,
//        message = "%${message}%",
//        type = if (type == DetailTypeState.All.getData()) {
//            "%%"
//        } else if (type == DetailTypeState.UpAll.getData() || type == DetailTypeState.DownAll.getData()) {
//            "%${type.triad}%"
//        } else {
//            "%${type.name}%"
//        },
//        direction = direction.toString(),
//        channel = channel.toString()
//    )
//}

data class ScreeningPlus(
    @Expose val startTime: String = "1/1/1",
    @Expose val endTime: String = "5000/1/1",
    @Expose var minMoney: Double = 0.0,
    @Expose val maxMoney: Double = 9999999.0,
    @Expose val message: String = "",
    @Expose val type: RememberState<DetailTypeState> = RememberState(DetailTypeState.All),
    @Expose val directions: SnapshotStateList<MovDirection> = mutableStateListOf(),
    @Expose val channels: SnapshotStateList<MovDirection> = mutableStateListOf()
) {
    fun getScreened() = Billing.db.getDetailDao().queryWithAllValue(
        startTime = startTime,
        endTime = endTime,
        minMoney = minMoney,
        maxMoney = maxMoney,
        message = "%${message}%",
        type = if (type.value == DetailTypeState.All) {
            "%%"
        } else if (type.value == DetailTypeState.UpAll || type.value == DetailTypeState.DownAll) {
            "%${type.value.triad.value}%"
        } else {
            "%${type.value.name.value}%"
        },
        direction = sync(directions),
        channel = sync(channels)
    )

    fun sync(value:SnapshotStateList<MovDirection>):List<String> {
        val a = mutableListOf<String>()
        value.forEach {
            a.add(Gson().toJson(it))
        }
        return a.toList()
    }
}

data class Screening(
    @Expose val startTime: TimeState = Time(1, 1, 1, 1).getState(),
    @Expose val endTime: TimeState = Time(5000, 1, 1, 1).getState(),
    @Expose val minMoney: RememberState<Double> = RememberState(0.0),
    @Expose val maxMoney: RememberState<Double> = RememberState(9999999.0),
    @Expose val message: RememberState<String> = RememberState(""),
    @Expose val type: RememberState<DetailTypeState> = RememberState(DetailTypeState.All),
    @Expose val direction: RememberState<MovDirectionState> = RememberState(MovDirectionState.All),
    @Expose val channel: RememberState<MovDirectionState> = RememberState(MovDirectionState.All)
) {
    fun getScreened() = Billing.db.getDetailDao().queryWithAllValue(
        startTime = if (startTime.month.getState().value != 13) startTime.toString() else "${startTime.year.getState().value}/1/1",
        endTime = if (endTime.month.getState().value != 13) endTime.toString() else "${endTime.year.getState().value+1}/1/0",
        minMoney = minMoney.getState().value,
        maxMoney = maxMoney.getState().value,
        message = "%${message.getState().value}%",
        type = if (type.value == DetailTypeState.All) {
            "%%"
        } else if (type.value == DetailTypeState.UpAll || type.value == DetailTypeState.DownAll) {
            "%${type.value.triad.value}%"
        } else {
            "%${type.value.name.value}%"
        },
        direction = if (direction.getState().value == MovDirectionState.All) {
            Log.w("hello","1")
            "%%"
        } else {
            Log.w("hello",direction.getState().value.name.value)
            "%${direction.getState().value.name.value}%"
        },
        channel = if (channel.getState().value == MovDirectionState.All) {
            "%%"
        } else {
            "${channel.getState().value.name.getState().value}%"
        }
    )

    fun getSql() =
        "SELECT * FROM details " +
                "WHERE time BETWEEN '$startTime' AND '$endTime' " +
                "AND money BETWEEN ${maxMoney.value} AND ${minMoney.value} " +

                if (message.value == "") {
                    ""
                } else {
                    "AND message LIKE '%${message.value}%' "
                } +

                if (type.value == DetailTypeState.All) {
                    ""
                } else if (type.value == DetailTypeState.UpAll || type.value == DetailTypeState.DownAll) {
                    "AND type LIKE '%${type.value.triad.value}%' "
                } else {
                    "AND type LIKE '%${type.value.name.value}%' "
                } +

                        if (direction.value == MovDirectionState.All) {
                            ""
                        } else {
                            "AND direction LIKE '%${direction.value.name.value}%' "
                        } +

                        if (channel.value == MovDirectionState.All) {
                            ""
                        } else {
                            "AND channel LIKE '%${direction.value.name.value}%' "
                        }
}