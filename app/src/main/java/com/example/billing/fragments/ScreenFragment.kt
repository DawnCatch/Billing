package com.example.billing.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.billing.R
import com.example.billing.activitys.*
import com.example.billing.ui.theme.keyboard
import com.example.billing.ui.theme.surfaceColor
import com.example.billing.utils.RememberState
import com.example.billing.utils.datas.*
import com.example.sport.ui.view.MDialog
import com.example.sport.ui.view.SettingItemColum
import com.example.sport.ui.view.components.*
import com.example.sport.ui.view.components.EditTextIconBox.Companion.Null
import com.example.sport.ui.view.components.EditTextPromptBox.Companion.textAndText
import com.example.sport.ui.view.components.EditTextSettingBox.Companion.NumberOption
import com.example.sport.ui.view.components.EditTextSettingBox.Companion.TextOption
import com.google.gson.Gson

class ScreenFragmentModel : ViewModel() {

    @SuppressLint("StaticFieldLeak")
    var templateActivity: TemplateActivity? = null

    val keyboardVisible = RememberState(false)
    var money: RememberState<Double>? = null

    var status = mutableStateOf(false)
    var min = mutableStateOf(0.0)
    var max = mutableStateOf(9999999.0)

    val channels = mutableStateListOf<MovDirection>(MovDirectionState.NullF.getData())
    val channelVisible = RememberState(false)

    val directions = mutableStateListOf<MovDirection>(MovDirectionState.NullT.getData())
    val directionVisible = RememberState(false)
}

@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun ScreenFragment(
    templateActivity: TemplateActivity,
    model: ScreenFragmentModel = viewModel()
) {
    model.templateActivity = templateActivity
    var startYear by remember {
        mutableStateOf(1)
    }
    var startMonth by remember {
        mutableStateOf(1)
    }
    var startDay by remember {
        mutableStateOf(1)
    }
    var endYear by remember {
        mutableStateOf(5000)
    }
    var endMonth by remember {
        mutableStateOf(1)
    }
    var endDay by remember {
        mutableStateOf(1)
    }
    var message by remember {
        mutableStateOf("")
    }
    val screening = ScreeningPlus(
        startTime = "$startYear/$startMonth/$startDay",
        endTime = "$endYear/$endMonth/$endDay",
        minMoney = model.min.value,
        maxMoney = model.max.value,
        channels = model.channels,
        message = message,
        directions = model.directions
    )
    val focusManager = LocalFocusManager.current
    Box(
        Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            SettingItemColum(key = "备注", value = mutableListOf(
                {
                    EditText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged {
                                model.keyboardVisible set false
                            }
                            .focusable(),
                        editTextSttting = TextOption {
                            focusManager.clearFocus()
                        },
                        editTextPrompt = textAndText(
                            "备注", ""
                        ),
                        editTextIcon = EditTextIcon(
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_message),
                                    contentDescription = "备注",
                                    modifier = Modifier.size(24.dp, 24.dp)
                                )
                            },
                            trailingIcon = if (message != "") {
                                @Composable {
                                    IconButton(onClick = {
                                        message = ""
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "删除",
                                            modifier = Modifier.size(24.dp, 24.dp)
                                        )
                                    }
                                }
                            }else {
                                null
                            }
                        ),
                        sidevalue = message,
                        shape = RoundedCornerShape(0)
                    ) {
                        message = it
                    }
                }
            ))
            SettingItemColum(key = "时间", value = mutableListOf(
                {
                    Row {
                        EditText(
                            modifier = Modifier
                                .weight(1f)
                                .onFocusChanged {
                                    model.keyboardVisible set false
                                }
                                .focusable(),
                            editTextSttting = NumberOption {
                                focusManager.clearFocus()
                            },
                            editTextPrompt = textAndText(
                                "年", ""
                            ),
                            editTextIcon = Null(),
                            sidevalue = startYear.toString(),
                            shape = RoundedCornerShape(0)
                        ) {
                            try {
                                startYear = it.toInt()
                            } catch (e: NumberFormatException) {
                                startYear = 1
                                Toast.makeText(
                                    model.templateActivity!!,
                                    "非法输入",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        EditText(
                            modifier = Modifier
                                .weight(1f)
                                .onFocusChanged {
                                    model.keyboardVisible set false
                                }
                                .focusable(),
                            editTextSttting = NumberOption {
                                focusManager.clearFocus()
                            },
                            editTextPrompt = textAndText(
                                "月", ""
                            ),
                            editTextIcon = Null(),
                            sidevalue = startMonth.toString(),
                            shape = RoundedCornerShape(0)
                        ) {
                            try {
                                startMonth = it.toInt()
                            } catch (e: NumberFormatException) {
                                startMonth = 1
                                Toast.makeText(
                                    model.templateActivity!!,
                                    "非法输入",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        EditText(
                            modifier = Modifier
                                .weight(1f)
                                .onFocusChanged {
                                    model.keyboardVisible set false
                                }
                                .focusable(),
                            editTextSttting = NumberOption {
                                focusManager.clearFocus()
                            },
                            editTextPrompt = textAndText(
                                "日", ""
                            ),
                            editTextIcon = Null(),
                            sidevalue = startDay.toString(),
                            shape = RoundedCornerShape(0)
                        ) {
                            try {
                                startDay = it.toInt()
                            } catch (e: NumberFormatException) {
                                startDay = 1
                                Toast.makeText(
                                    model.templateActivity!!,
                                    "非法输入",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                },
                {
                    Row {
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_down),
                            modifier = Modifier.size(24.dp),
                            contentDescription = "到"
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                },
                {
                    Row {
                        EditText(
                            modifier = Modifier
                                .weight(1f)
                                .onFocusChanged {
                                    model.keyboardVisible set false
                                }
                                .focusable(),
                            editTextSttting = NumberOption {
                                focusManager.clearFocus()
                            },
                            editTextPrompt = textAndText(
                                "年", ""
                            ),
                            editTextIcon = Null(),
                            sidevalue = endYear.toString(),
                            shape = RoundedCornerShape(0)
                        ) {
                            try {
                                endYear = it.toInt()
                            } catch (e: NumberFormatException) {
                                endYear = 5000
                                Toast.makeText(
                                    model.templateActivity!!,
                                    "非法输入",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        EditText(
                            modifier = Modifier
                                .weight(1f)
                                .onFocusChanged {
                                    model.keyboardVisible set false
                                }
                                .focusable(),
                            editTextSttting = NumberOption {
                                focusManager.clearFocus()
                            },
                            editTextPrompt = textAndText(
                                "月", ""
                            ),
                            editTextIcon = Null(),
                            sidevalue = endMonth.toString(),
                            shape = RoundedCornerShape(0)
                        ) {
                            try {
                                endMonth = it.toInt()
                            } catch (e: NumberFormatException) {
                                endMonth = 1
                                Toast.makeText(
                                    model.templateActivity!!,
                                    "非法输入",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        EditText(
                            modifier = Modifier
                                .weight(1f)
                                .onFocusChanged {
                                    model.keyboardVisible set false
                                }
                                .focusable(),
                            editTextSttting = NumberOption {
                                focusManager.clearFocus()
                            },
                            editTextPrompt = textAndText(
                                "日", ""
                            ),
                            editTextIcon = Null(),
                            sidevalue = endDay.toString(),
                            shape = RoundedCornerShape(0)
                        ) {
                            try {
                                endDay = it.toInt()
                            } catch (e: NumberFormatException) {
                                endDay = 1
                                Toast.makeText(
                                    model.templateActivity!!,
                                    "非法输入",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            ))
            SettingItemColum(key = "金额", value = mutableListOf(
                {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            Modifier
                                .weight(1f)
                                .background(
                                    if (model.keyboardVisible.getState().value && !model.status.value)
                                        Brush.horizontalGradient(
                                            colors = listOf(
                                                MaterialTheme.colors.onBackground,
                                                MaterialTheme.colors.surfaceColor
                                            )
                                        )
                                    else
                                        Brush.horizontalGradient(
                                            colors = listOf(
                                                MaterialTheme.colors.surfaceColor,
                                                MaterialTheme.colors.surfaceColor
                                            )
                                        )
                                )
                                .padding(start = 17.dp)
                                .padding(vertical = 5.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = MutableInteractionSource()
                                ) {
                                    focusManager.clearFocus()
                                    if (!model.status.value) {
                                        model.keyboardVisible set !model.keyboardVisible.value
                                    } else {
                                        model.status.value = false
                                        model.keyboardVisible set true
                                    }
                                }
                        ) {
                            ProvideTextStyle(
                                value = TextStyle(
                                    color = if (model.keyboardVisible.getState().value && !model.status.value)
                                        MaterialTheme.colors.background
                                    else
                                        MaterialTheme.colors.onBackground
                                )
                            ) {
                                Text(text = "最小", fontSize = 12.sp)
                                Text(
                                    text = model.min.value.toString(),
                                    maxLines = 1,
                                    fontSize = 16.sp
                                )
                            }
                        }
                        Icon(
                            painter = painterResource(id = R.drawable.ic_go),
                            modifier = Modifier
                                .weight(0.5f)
                                .size(26.dp),
                            contentDescription = "到"
                        )
                        Column(
                            Modifier
                                .weight(1f)
                                .background(
                                    if (model.keyboardVisible.getState().value && model.status.value)
                                        Brush.horizontalGradient(
                                            startX = Float.POSITIVE_INFINITY,
                                            endX = 0.0f,
                                            colors = listOf(
                                                MaterialTheme.colors.onBackground,
                                                MaterialTheme.colors.surfaceColor
                                            )
                                        )
                                    else
                                        Brush.horizontalGradient(
                                            startX = Float.POSITIVE_INFINITY,
                                            endX = 0.0f,
                                            colors = listOf(
                                                MaterialTheme.colors.surfaceColor,
                                                MaterialTheme.colors.surfaceColor
                                            )
                                        )
                                )
                                .padding(end = 17.dp)
                                .padding(vertical = 5.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = MutableInteractionSource()
                                ) {
                                    focusManager.clearFocus()
                                    if (model.status.value) {
                                        model.keyboardVisible set !model.keyboardVisible.value
                                    } else {
                                        model.status.value = true
                                        model.keyboardVisible set true
                                    }
                                }
                        ) {
                            ProvideTextStyle(
                                value = TextStyle(
                                    color = if (model.keyboardVisible.getState().value && model.status.value)
                                        MaterialTheme.colors.background
                                    else
                                        MaterialTheme.colors.onBackground
                                )
                            ) {
                                Text(
                                    text = "最大",
                                    fontSize = 12.sp,
                                    modifier = Modifier.align(Alignment.End)
                                )
                                Text(
                                    text = model.max.value.toString(),
                                    modifier = Modifier
                                        .align(Alignment.End),
                                    maxLines = 1,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            ))
            SettingItemColum(key = "渠道", modifier = Modifier.height(
                if (model.channels.size == 0) {
                    71.dp
                } else if (model.channels.size > 4) {
                    113.dp
                } else {
                    71.dp
                }
            ), value = mutableListOf(
                {
                    MovDirectionCheck(
                        model.channelVisible,
                        "渠道",
                        model.channels,
                        false
                    ) {
                        val bundle = Bundle()
                        bundle.putString(EXTRA_FRAGMENT, "渠道设置")
                        bundle.putBoolean(STATE_BAR, false)
                        templateActivity.startActivity(
                            Intent(
                                templateActivity,
                                TemplateActivity::class.java
                            ).putExtras(
                                bundle
                            )
                        )
                    }
                    MovDirectionGrid(triad = false, value = model.channels)
                }
            ))
            SettingItemColum(key = "对象", modifier = Modifier.height(
                if (model.directions.size == 0) {
                    71.dp
                } else if (model.directions.size > 4) {
                    113.dp
                } else {
                    71.dp
                }
            ), value = mutableListOf(
                {
                    MovDirectionCheck(
                        model.directionVisible,
                        "对象",
                        model.directions,
                        true
                    ) {
                        val bundle = Bundle()
                        bundle.putString(EXTRA_FRAGMENT, "对象设置")
                        bundle.putBoolean(STATE_BAR, false)
                        templateActivity.startActivity(
                            Intent(
                                templateActivity,
                                TemplateActivity::class.java
                            ).putExtras(
                                bundle
                            )
                        )
                    }
                    MovDirectionGrid(triad = true, value = model.directions)
                }
            ))
            val list = screening.getScreened().asLiveData()
            var height by remember {
                mutableStateOf(50)
            }
            list.observe(model.templateActivity!!) {
                if (it.size != 0) {
                    height = 500
                }
            }
            val listState = list.observeAsState(arrayListOf())
            LazyColumn(
                Modifier.height(height.dp)
            ) {
                itemsIndexed(listState.value) { index, it ->
                    if (index == 0 ||
                        (listState.value[index - 1].time.toDate() != it.time.toDate()) ||
                        (listState.value[index - 1].time.month != it.time.month)
                    ) {
                        val headTime = listState.value[index].time
                        var left by remember {
                            mutableStateOf(0.0)
                        }
                        var right by remember {
                            mutableStateOf(0.0)
                        }
                        list.observe(model.templateActivity!!) { details ->
                            left = 0.0
                            right = 0.0
                            details.forEach {
                                if (it.time.toDate() == headTime.toDate()) {
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
                                .padding(horizontal = 5.dp, vertical = 8.dp)
                                .clip(RoundedCornerShape(25, 25, 0, 0))
                                .background(Color.Gray.copy(0.3f))
                        ) {
                            ProvideTextStyle(
                                value = TextStyle(
                                    color = MaterialTheme.colors.onSecondary
                                )
                            ) {
                                Text(
                                    text = it.time.toDate(),
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
                    DetailItem(detail = it) {
                        val bundle = Bundle()
                        bundle.putString(EXTRA_FRAGMENT, "添加明细")
                        bundle.putBoolean(STATE_BAR, false)
                        bundle.putBoolean(RE_INIT, true)
                        bundle.putString("DetailData", Gson().toJson(it))
                        model.templateActivity!!.startActivity(
                            Intent(
                                model.templateActivity!!,
                                TemplateActivity::class.java
                            ).putExtras(
                                bundle
                            )
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier.align(Alignment.BottomCenter),
        ) {
            ScreenAnimatedEditView()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovDirectionGrid(
    triad: Boolean,
    value: SnapshotStateList<MovDirection>,
    model: ScreenFragmentModel = viewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (triad) model.directionVisible set true
                else model.channelVisible set true
            }
    ) {
        LazyVerticalGrid(
            cells = GridCells.Fixed(4)
        ) {
            items(value) { it ->
                Text(text = it.name, modifier = Modifier.padding(10.dp))
            }
        }
    }
}

@Composable
fun MovDirectionCheck(
    visible: RememberState<Boolean>,
    contentDescription: String,
    value: SnapshotStateList<MovDirection>,
    type: Boolean,
    model: ScreenFragmentModel = viewModel(),
    create: () -> Unit
) {
    val list = Billing.db.getMovDirectionDao().queryWithType(type).asLiveData()
    val listState = list.observeAsState(arrayListOf())

    val itemHeight = 40

    var height by remember {
        mutableStateOf(0)
    }
    list.observe(model.templateActivity!!) {
        if (it.isEmpty()) {
            height = itemHeight
        } else {
            height = (it.size + 1) * itemHeight
        }
    }

    MDialog(modifier = Modifier.height(height.dp), visible = visible) {
        listState.value.forEach {
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    .width(200.dp)
                    .height(itemHeight.dp)
                    .clickable {
                        if (value.indexOf(it) != -1) {
                            value.remove(it)
                            if (value.size == 0) {
                                value.add(if (type) MovDirectionState.NullT.getData() else MovDirectionState.NullF.getData())
                            }
                        } else {
                            value.add(it)
                        }
                    }
            ) {
                Text(
                    text = it.name,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .weight(3f)
                        .padding(10.dp)
                        .padding(start = 15.dp)
                )
                if (value.indexOf(it) != -1) {
                    Box(Modifier.weight(1f)) {
                        Text(text = (value.indexOf(it) + 1).toString())
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = contentDescription,
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .width(200.dp)
            .height(itemHeight.dp)
            .clickable {
                value.clear()
                value.add(if (type) MovDirectionState.NullT.getData() else MovDirectionState.NullF.getData())
                visible set false
                create()
            }
        ) {
            Text(
                text = "添加设置",
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .weight(3f)
                    .padding(10.dp)
                    .padding(start = 15.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ScreenAnimatedEditView(model: ScreenFragmentModel = viewModel()) {
    AnimatedVisibility(
        visible = model.keyboardVisible.getState().value
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.keyboard)
        ) {
            val modifier = Modifier
                .weight(1f)
                .padding(vertical = 10.dp)
            var pointAfter = false
            var pointAfterFirstEnter = true
            val onclick: (String) -> Unit = { it ->
                var integer = ""
                var float = ""
                if ((if (model.status.value) model.max.value else model.min.value).toString().length < 9 || pointAfter || it == ".") {
                    (if (model.status.value) model.max.value else model.min.value).toString()
                        .split(".")
                        .run {
                            integer = this[0]
                            float = this[1]
                        }
                    when (it) {
                        "1", "2", "3", "4", "5", "6", "7", "8", "9", "0" -> {
                            if (pointAfter) {
                                if (float == "0" && pointAfterFirstEnter) {
                                    float = it
                                    pointAfterFirstEnter = false
                                } else {
                                    if (float.length < 2) {
                                        float += it
                                    }
                                }
                            } else {
                                if (integer == "0") {
                                    integer = it
                                } else {
                                    integer += it
                                }
                            }
                        }
                        "." -> {
                            pointAfter = true
                        }
                    }
                    if (model.status.value) {
                        model.max.value = "$integer.${float}".toDouble()
                    } else {
                        model.min.value = "$integer.${float}".toDouble()
                    }
                } else {
                    Toast.makeText(model.templateActivity!!, "超过最大长度", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            Row(Modifier.fillMaxWidth()) {
                TextKeyboardItem(text = "7", modifier = modifier, onclick = onclick)
                TextKeyboardItem(text = "8", modifier = modifier, onclick = onclick)
                TextKeyboardItem(text = "9", modifier = modifier, onclick = onclick)
            }
            Row(Modifier.fillMaxWidth()) {
                TextKeyboardItem(text = "4", modifier = modifier, onclick = onclick)
                TextKeyboardItem(text = "5", modifier = modifier, onclick = onclick)
                TextKeyboardItem(text = "6", modifier = modifier, onclick = onclick)
            }
            Row(Modifier.fillMaxWidth()) {
                TextKeyboardItem(text = "1", modifier = modifier, onclick = onclick)
                TextKeyboardItem(text = "2", modifier = modifier, onclick = onclick)
                TextKeyboardItem(text = "3", modifier = modifier, onclick = onclick)
            }
            Row(Modifier.fillMaxWidth()) {
                TextKeyboardItem(text = ".", modifier = modifier, onclick = onclick)
                TextKeyboardItem(text = "0", modifier = modifier, onclick = onclick)
                IconKeyboardItem(
                    icon = R.drawable.ic_back_space,
                    modifier = modifier
                ) {
                    var integer = 0
                    var float = 0
                    (if (model.status.value) model.max.value else model.min.value).toString()
                        .split(".")
                        .run {
                            integer = this[0].toInt()
                            float = this[1].toInt()
                        }
                    if (pointAfter) {
                        float /= 10
                        if (float == 0) {
                            pointAfter = false
                            pointAfterFirstEnter = true
                        }
                    } else {
                        integer /= 10
                    }
                    if (model.status.value) {
                        model.max.value = "$integer.${float}".toDouble()
                    } else {
                        model.min.value = "$integer.${float}".toDouble()
                    }
                }
            }
        }
    }
}