package com.example.billing.utils.datas

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.billing.utils.RememberState
import com.google.gson.annotations.Expose

@Entity(tableName = "details")
data class Detail(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id", typeAffinity = ColumnInfo.INTEGER)
    val id: Int? = null,
    val time: Time,
    val money: Double,
    val message: String,
    val type: DetailType,
    val direction: MovDirection,
    val channel: MovDirection
) {
    @Ignore
    fun getState() = DetailState(
        id = id,
        time = RememberState(time.getState()),
        money = RememberState(money),
        message = RememberState(message),
        type = RememberState(type.getState()),
        direction = RememberState(direction.getState()),
        channel = RememberState(channel.getState())
    )
}

data class DetailState(
    @Expose val id:Int? = null,
    @Expose val time: RememberState<TimeState>,
    @Expose val money: RememberState<Double>,
    @Expose val message: RememberState<String>,
    @Expose val type: RememberState<DetailTypeState>,
    @Expose val direction: RememberState<MovDirectionState>,
    @Expose val channel: RememberState<MovDirectionState>
) {
    fun getData() = Detail(
        id = id,
        time = time.value.getData(),
        money = money.value,
        message = message.value,
        type = type.value.getData(),
        direction = direction.value.getData(),
        channel = channel.value.getData()
    )
}

enum class Type {
    Borrowers,
    Lenders,
    Host
}