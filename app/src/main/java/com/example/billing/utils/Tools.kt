package com.example.billing.utils

import com.example.billing.utils.datas.TimeState
import java.util.*

fun getTimeOfToday(): TimeState {
    val tms = Calendar.getInstance()
    return TimeState(
        RememberState(tms.get(Calendar.YEAR)),
        RememberState(tms.get(Calendar.MONTH) + 1),
        RememberState(tms.get(Calendar.DAY_OF_MONTH)),
        RememberState(
            if (tms.get(Calendar.DAY_OF_WEEK) - 1 == 0) {
                7
            } else tms.get(Calendar.DAY_OF_WEEK) - 1
        ) //日,一,二,三,四,五,六   1,2,3,4,5,6,7
    )
}

fun <T> isItemOfList(
    value1: List<T>,
    value2: List<T>
): Boolean {
    if (value2.isEmpty()) return false
    value1.forEach {
        if (value2.indexOf(it) == -1) {
            return false
        }
    }
    return true
}