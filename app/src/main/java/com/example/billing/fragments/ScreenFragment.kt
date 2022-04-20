package com.example.billing.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.billing.R
import com.example.billing.activitys.Billing
import com.example.billing.activitys.EXTRA_FRAGMENT
import com.example.billing.activitys.STATE_BAR
import com.example.billing.activitys.TemplateActivity
import com.example.billing.ui.theme.keyboard
import com.example.billing.ui.theme.keyboardTime
import com.example.billing.utils.RememberState
import com.example.billing.utils.datas.DetailTypeState
import com.example.billing.utils.datas.MovDirection
import com.example.billing.utils.datas.MovDirectionState
import com.example.billing.utils.datas.Screening
import com.example.billing.utils.getTimeOfToday
import com.example.sport.ui.view.MDialog
import com.example.sport.ui.view.SettingItemColum
import com.example.sport.ui.view.components.EditText
import com.example.sport.ui.view.components.EditTextIconBox.Companion.Null
import com.example.sport.ui.view.components.EditTextPromptBox.Companion.textAndText
import com.example.sport.ui.view.components.EditTextSettingBox.Companion.NumberOption
import com.example.sport.ui.view.components.EditTextSettingBox.Companion.TextOption

class ScreenFragmentModel : ViewModel() {

    @SuppressLint("StaticFieldLeak")
    var templateActivity: TemplateActivity? = null

    val keyboardVisible = RememberState(false)
    var money: RememberState<Double>? = null

    val channels = mutableStateListOf<MovDirection>()
    val channelVisible = RememberState(false)

    val directions = mutableStateListOf<MovDirection>()
    val directionVisible = RememberState(false)
}

@Composable
fun ScreenFragment(
    templateActivity: TemplateActivity,
    model: ScreenFragmentModel = viewModel()
) {
    model.templateActivity = templateActivity
    val screening = Screening()
    val focusManager = LocalFocusManager.current
    Box(Modifier.fillMaxSize()) {
        Column {
            SettingItemColum(key = "时间", value = mutableListOf(
                {
                    Row {
                        EditText(
                            modifier = Modifier
                                .weight(1f)
                                .focusable(),
                            editTextSttting = TextOption {
                                focusManager.clearFocus()
                            },
                            editTextPrompt = textAndText(
                                "年", ""
                            ),
                            editTextIcon = Null(),
                            sidevalue = screening.startTime.year.toStringState(),
                            shape = RoundedCornerShape(0)
                        )
                        EditText(
                            modifier = Modifier
                                .weight(1f)
                                .focusable(),
                            editTextSttting = TextOption {
                                focusManager.clearFocus()
                            },
                            editTextPrompt = textAndText(
                                "月", ""
                            ),
                            editTextIcon = Null(),
                            sidevalue = screening.startTime.month.toStringState(),
                            shape = RoundedCornerShape(0)
                        )
                        EditText(
                            modifier = Modifier
                                .weight(1f)
                                .focusable(),
                            editTextSttting = TextOption {
                                focusManager.clearFocus()
                            },
                            editTextPrompt = textAndText(
                                "日", ""
                            ),
                            editTextIcon = Null(),
                            sidevalue = screening.startTime.dayOfMonth.toStringState(),
                            shape = RoundedCornerShape(0)
                        )
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
                                .focusable(),
                            editTextSttting = TextOption {
                                focusManager.clearFocus()
                            },
                            editTextPrompt = textAndText(
                                "年", ""
                            ),
                            editTextIcon = Null(),
                            sidevalue = screening.endTime.year.toStringState(),
                            shape = RoundedCornerShape(0)
                        )
                        EditText(
                            modifier = Modifier
                                .weight(1f)
                                .focusable(),
                            editTextSttting = TextOption {
                                focusManager.clearFocus()
                            },
                            editTextPrompt = textAndText(
                                "月", ""
                            ),
                            editTextIcon = Null(),
                            sidevalue = screening.endTime.month.toStringState(),
                            shape = RoundedCornerShape(0)
                        )
                        EditText(
                            modifier = Modifier
                                .weight(1f)
                                .focusable(),
                            editTextSttting = TextOption {
                                focusManager.clearFocus()
                            },
                            editTextPrompt = textAndText(
                                "日", ""
                            ),
                            editTextIcon = Null(),
                            sidevalue = screening.endTime.dayOfMonth.toStringState(),
                            shape = RoundedCornerShape(0)
                        )
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
                                .padding(start = 17.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = MutableInteractionSource()
                                ) {
                                    focusManager.clearFocus()
                                    if (model.money == screening.minMoney) {
                                        model.keyboardVisible set !model.keyboardVisible.value
                                    }
                                    model.money = screening.minMoney
                                }
                        ) {
                            Text(text = "最小", fontSize = 12.sp)
                            Text(
                                text = screening.minMoney.getState().value.toString(),
                                color = Color.Black,
                                maxLines = 1,
                                fontSize = 16.sp
                            )
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
                                .padding(end = 17.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = MutableInteractionSource()
                                ) {
                                    focusManager.clearFocus()
                                    if (model.money == screening.maxMoney) {
                                        model.keyboardVisible set !model.keyboardVisible.value
                                    }
                                    model.money = screening.maxMoney
                                }
                        ) {
                            Text(
                                text = "最大",
                                fontSize = 12.sp,
                                modifier = Modifier.align(Alignment.End)
                            )
                            Text(
                                text = screening.maxMoney.getState().value.toString(),
                                modifier = Modifier
                                    .align(Alignment.End),
                                color = Color.Black,
                                maxLines = 1,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            ))
            SettingItemColum(key = "渠道", value = mutableListOf(
                {
                    MovDirectionCheck(model.channelVisible, "渠道", model.channels, false) {
                        val bundle = Bundle()
                        bundle.putString(EXTRA_FRAGMENT, "渠道设置")
                        bundle.putBoolean(STATE_BAR, false)
                        templateActivity.startActivity(
                            Intent(templateActivity, TemplateActivity::class.java).putExtras(
                                bundle
                            )
                        )
                    }
                    MovDirectionGrid(triad = false, value = model.channels)
                }
            ))
            SettingItemColum(key = "对象", value = mutableListOf(
                {
                    MovDirectionCheck(model.directionVisible, "对象", model.directions, true) {
                        val bundle = Bundle()
                        bundle.putString(EXTRA_FRAGMENT, "对象设置")
                        bundle.putBoolean(STATE_BAR, false)
                        templateActivity.startActivity(
                            Intent(templateActivity, TemplateActivity::class.java).putExtras(
                                bundle
                            )
                        )
                    }
                    MovDirectionGrid(triad = true, value = model.directions)
                }
            ))
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
            if (value.size == 0) {
                item {
                    Text(text = "无", modifier = Modifier.padding(10.dp))
                }
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
        }else {
            height = it.size * itemHeight
        }
    }

    MDialog(modifier = Modifier.height(height.dp), visible = visible) {
        listState.value.forEach {
            if (it != MovDirectionState.All.getData()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                        .width(200.dp)
                        .height(itemHeight.dp)
                        .clickable {
                            if (value.indexOf(it) != -1) {
                                value.remove(it)
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
        }
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .width(200.dp)
            .height(itemHeight.dp)
            .clickable {
                value.clear()
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
                if (model.money!!.value.toString().length < 9 || pointAfter || it == ".") {
                    model.money!!.value.toString().split(".").run {
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
                    model.money!! set "$integer.${float}".toDouble()
                } else {
                    Toast.makeText(model.templateActivity!!, "超过最大长度", Toast.LENGTH_SHORT).show()
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
                    model.money!!.value.toString().split(".").run {
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
                    model.money!! set "$integer.${float}".toDouble()
                }
            }
        }
    }
}