package com.example.billing.utils.datas

import com.example.billing.activitys.Billing
import com.example.billing.utils.BillingData
import com.example.billing.utils.RememberState
import com.example.billing.utils.getTimeOfToday
import com.google.gson.annotations.Expose
import java.time.Month

data class Screening(
    @Expose val startTime: TimeState = Time(1, 1, 1, 1).getState(),
    @Expose val endTime: TimeState = Time(5000, 1, 1, 1).getState(),
    @Expose val maxMoney: RememberState<Double> = RememberState(0.0),
    @Expose val minMoney: RememberState<Double> = RememberState(Double.MAX_VALUE),
    @Expose val message: RememberState<String> = RememberState(""),
    @Expose val type: RememberState<DetailTypeState> = RememberState(DetailTypeState.All),
    @Expose val direction: RememberState<MovDirectionState> = RememberState(MovDirectionState.All),
    @Expose val channel: RememberState<MovDirectionState> = RememberState(MovDirectionState.All)
) {
    fun getTime() =
        Billing.db.getDetailDao().queryWithTime(startTime.toString(), endTime.toString())

    fun getScreened() = Billing.db.getDetailDao().A(
        startTime = if (startTime.month.getState().value != 13) startTime.toString() else "${startTime.year.getState().value}/1/1",
        endTime = if (endTime.month.getState().value != 13) endTime.toString() else "${endTime.year.getState().value+1}/1/0",
        maxMoney = maxMoney.getState().value,
        minMoney = minMoney.getState().value,
        message = "%${message.getState().value}%",
        type = if (type.value == DetailTypeState.All) {
            "%%"
        } else if (type.value == DetailTypeState.UpAll || type.value == DetailTypeState.DownAll) {
            "%${type.value.triad.value}%"
        } else {
            "%${type.value.name.value}%"
        },
        direction = if (direction.value == MovDirectionState.All) {
            "%%"
        } else {
            "%${direction.value.name.value}%"
        },
        channel = if (channel.value == MovDirectionState.All) {
            "%%"
        } else {
            "${direction.value.name.value}%"
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