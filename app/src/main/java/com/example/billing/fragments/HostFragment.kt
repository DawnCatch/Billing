package com.example.billing.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.billing.R
import com.example.billing.activitys.*
import com.example.sport.ui.view.MCard
import com.example.sport.ui.view.MDialog
import com.example.sport.ui.view.TimeSelectView
import com.example.billing.utils.RememberState
import com.example.billing.utils.datas.*
import com.example.billing.utils.getTimeOfToday
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HostFragment(mainActivity: MainActivity) {
    HostBaseFragment(mainActivity = mainActivity, type = Type.Host)
}

val isRefreshing = mutableStateOf(false)

class HostFragmentModel : ViewModel() {
    var timeBoxState: TimeState = getTimeOfToday()
    var timeBoxValueTemp = listOf(
        listOf(RememberState(0.0), RememberState(0.0)),
        listOf(RememberState(0.0), RememberState(0.0)),
        listOf(RememberState(0.0), RememberState(0.0)),
    )

    @SuppressLint("StaticFieldLeak")
    var mainActivity: MainActivity? = null
}

@SuppressLint("UnrememberedMutableState", "CoroutineCreationDuringComposition")
@Composable
fun HostBaseFragment(
    mainActivity: MainActivity,
    type: Type,
    model: HostFragmentModel = viewModel()
) {
    model.mainActivity = mainActivity
    val timeBoxState = model.timeBoxState
    val index = if (type == Type.Host) 0 else if (type == Type.Borrowers) 1 else 2
    val leftBoxMoney = model.timeBoxValueTemp[index][0]
    val rightBoxMoney = model.timeBoxValueTemp[index][1]
    var leftBoxTitle = ""
    var rightBoxTitle = "偿还"
    var screening = Screening()
    val state = rememberSwipeRefreshState(isRefreshing = isRefreshing.value)
    val scope = rememberCoroutineScope()
    SwipeRefresh(
        state = state,
        onRefresh = {
            isRefreshing.value = true
            scope.launch {
                delay(1000)
                isRefreshing.value = false
            }
        }
    ) {
        when (type) {
            Type.Host -> {
                leftBoxTitle = "收入"
                rightBoxTitle = "支出"
                screening = Screening(
                    startTime = Time(
                        timeBoxState.year.getState().value,
                        timeBoxState.month.getState().value,
                        1,
                        1
                    ).getState(),
                    endTime = Time(
                        timeBoxState.year.getState().value,
                        timeBoxState.month.getState().value,
                        32,
                        1
                    ).getState(),
                    type = RememberState(
                        DetailTypeState.All
                    )
                )
            }
        }
        val list = screening.getScreened().asLiveData()
        val listState = list.observeAsState(arrayListOf())

        list.observe(mainActivity) { details ->
            var leftTemp = 0.0
            var rightTemp = 0.0
            details.forEach { detail ->
                if (detail.type.triad) {
                    leftTemp += detail.money
                } else {
                    rightTemp += detail.money
                }
            }
            leftBoxMoney set leftTemp
            rightBoxMoney set rightTemp
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colors.primary,
                                MaterialTheme.colors.background
                            )
                        )
                    )
            ) {
                MCard {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(modifier = Modifier.weight(0.7f), contentAlignment = Alignment.Center) {
                            TimeBox()
                        }
                        Line()
                        Box(modifier = Modifier.weight(1f)) {
                            MoneyBox(title = leftBoxTitle, leftBoxMoney.getState().value) {

                            }
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            MoneyBox(title = rightBoxTitle, rightBoxMoney.getState().value) {

                            }
                        }
                    }
                }
            }
            DetailLazyColumView(
                modifier = Modifier.fillMaxSize(),
                list = list,
                groupBy = {
                          if (model.timeBoxState.month.getState().value == 13) {
                              "${it.time.month}月"
                          }else {
                              it.time.toDate()
                          }
                },
                context = model.mainActivity!!
            )
        }
    }
}

@Composable
fun DetailItem(detail: Detail, onclick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onclick()
            }
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = detail.type.name,
            Modifier
                .weight(1f)
                .padding(start = 5.dp)
        )
        IconButton(onClick = {
            Thread {
                Billing.db.getDetailDao().delete(detail = detail)
            }.start()
        }) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "删除"
            )
        }
    }
}

@Composable
fun Line() {
    Spacer(
        modifier = Modifier
            .padding(start = 5.dp, end = 25.dp)
            .fillMaxHeight()
            .width(0.5.dp)
            .background(Color.Gray)
    )
}

@Composable
fun MoneyBox(
    title: String,
    money: Double,
    onclick: () -> Unit
) {
    ItemColum(hint = title, onclick = { onclick() }) {
        Text(text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )
            ) {
                append(money.toInt().toString())
            }
            withStyle(
                style = SpanStyle(
                    color = Color.Black,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
            ) {
                val floatAfter = (money * 100).toInt() - money.toInt() * 100
                if (floatAfter < 10) {
                    append(".0${String.format("%d", floatAfter)}")
                } else {
                    append(".${String.format("%2d", floatAfter)}")
                }
            }
        })
    }
}

@Composable
fun UserBox(
    value: RememberState<MovDirectionState>
) {
    val visible = RememberState(false)
    ItemColum(hint = "目标", onclick = {
        visible set true
    }) {
        Row {
            Text(text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    )
                ) {
                    append(value.getState().value.name.getState().value[0])
                }
                withStyle(
                    style = SpanStyle(
                        color = Color.Blue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                ) {
                    append(value.getState().value.name.getState().value.substring(1))
                }
            })
            Icon(
                painter = painterResource(id = R.drawable.ic_expand),
                contentDescription = "设置时间",
                modifier = Modifier
                    .size(22.dp)
                    .align(Alignment.Bottom)
                    .padding(bottom = 5.dp),
                tint = MaterialTheme.colors.onPrimary
            )
        }
    }
}

@Composable
fun TimeBox() {
    val model: HostFragmentModel = viewModel()
    val time = model.timeBoxState

    val visible = RememberState(false)
    var yearTemp by remember {
        mutableStateOf(0)
    }
    var monthTemp by remember {
        mutableStateOf(0)
    }

    MDialog(visible = visible) {
        Row(
            modifier = Modifier.width(500.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            TimeSelectView(start = 1, end = 3000, default = time.year.value, modifier = Modifier
                .padding(start = 10.dp)
                .padding(vertical = 25.dp), selected = {
                yearTemp = it
            })
            Spacer(modifier = Modifier.width(50.dp))
            TimeSelectView(
                start = 1, end = 12, default = time.month.value, modifier = Modifier
                    .padding(end = 10.dp)
                    .padding(vertical = 25.dp), selected = {
                    monthTemp = it
                }, other = "全年"
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(3.dp)
        ) {
            Text(
                text = "取消",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        visible set false
                    }
            )
            Spacer(
                modifier = Modifier
                    .width(1.dp)
                    .height(16.dp)
                    .background(
                        Color.Black.copy(0.5f)
                    )
            )
            Text(
                text = "保存",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clickable {
                        time.year set yearTemp
                        time.month set monthTemp
                        isRefreshing.value = true
                        isRefreshing.value = false
                        visible set false
                    }
            )
        }
    }
    ItemColum(hint = "${time.year.getState().value}年", onclick = {
        visible set true
    }) {
        Row {
            Text(
                text = if (time.month.getState().value != 13) {
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp
                            )
                        ) {
                            append(time.month.getState().value.toString())
                        }
                        withStyle(
                            style = SpanStyle(
                                color = Color.Blue,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        ) {
                            append("月")
                        }
                    }
                } else {
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )
                        ) {
                            append("全年")
                        }
                    }
                }
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_expand),
                contentDescription = "设置时间",
                modifier = Modifier
                    .size(22.dp)
                    .align(Alignment.Bottom)
                    .padding(bottom = 5.dp),
                tint = MaterialTheme.colors.secondary
            )
        }
    }
}


@Composable
fun ItemColum(
    hint: RememberState<String>,
    onclick: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        Modifier
            .clickable(
                indication = null,
                interactionSource = MutableInteractionSource()
            ) {
                onclick()
            },
    ) {
        Text(text = hint.getState().value, color = Color.Gray, fontSize = 15.sp)
        content()
    }
}

@Composable
fun ItemColum(
    hint: String,
    onclick: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        Modifier
            .clickable(
                indication = null,
                interactionSource = MutableInteractionSource()
            ) {
                onclick()
            },
    ) {
        Text(text = hint, color = Color.Gray, fontSize = 15.sp)
        content()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailLazyColumView(
    modifier: Modifier = Modifier,
    list: LiveData<List<Detail>>,
    groupBy:(it:Detail) -> String = {
                                    it.time.toDate()
    },
    context: BaseActivity
) {
    val listState = list.observeAsState(arrayListOf())
    val grouped = listState.value.groupBy { groupBy(it) }
    LazyColumn(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 8.dp)
    ) {
        grouped.forEach { (initial, details) ->
            stickyHeader {
                val headTime = initial
                var left by remember {
                    mutableStateOf(0.0)
                }
                var right by remember {
                    mutableStateOf(0.0)
                }
                list.observe(context) { details ->
                    left = 0.0
                    right = 0.0
                    details.forEach {
                        if (it.time.toDate() == headTime) {
                            if (it.type.triad) {
                                left += it.money
                            } else {
                                right += it.money
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(25, 25, 0, 0))
                        .background(Color.Gray)
                ) {
                    ProvideTextStyle(
                        value = TextStyle(
                            color = MaterialTheme.colors.onSecondary
                        )
                    ) {
                        Text(
                            text = headTime,
                            Modifier
                                .padding(start = 6.dp)
                                .weight(1f),
                        )
                        if (left != 0.0) {
                            Text(
                                text = "收入:$left",
                                modifier = Modifier.padding(end = 5.dp)
                            )
                        }
                        if (right != 0.0) {
                            Text(
                                text = "支出:$right",
                                modifier = Modifier.padding(
                                    start = 5.dp,
                                    end = 5.dp
                                )
                            )
                        }
                    }
                }
            }

            items(details) { it ->
                DetailItem(detail = it) {
                    val bundle = Bundle()
                    bundle.putString(EXTRA_FRAGMENT, "添加明细")
                    bundle.putBoolean(STATE_BAR, false)
                    bundle.putBoolean(RE_INIT, true)
                    bundle.putString("DetailData", Gson().toJson(it))
                    context.startActivity(
                        Intent(
                            context,
                            TemplateActivity::class.java
                        ).putExtras(
                            bundle
                        )
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(70.dp))
        }
    }
}