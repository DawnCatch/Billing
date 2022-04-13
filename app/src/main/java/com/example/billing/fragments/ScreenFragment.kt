package com.example.billing.fragments

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.RememberObserver
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.billing.R
import com.example.billing.activitys.Billing
import com.example.billing.activitys.TemplateActivity
import com.example.billing.utils.RememberState
import com.example.billing.utils.datas.MovDirectionState
import com.example.billing.utils.datas.Screening
import com.example.billing.utils.getTimeOfToday
import com.example.sport.ui.view.SettingItemColum
import com.example.sport.ui.view.components.EditText
import com.example.sport.ui.view.components.EditTextIconBox.Companion.Null
import com.example.sport.ui.view.components.EditTextPromptBox.Companion.TextAndText
import com.example.sport.ui.view.components.EditTextSettingBox.Companion.NumberOption
import com.example.sport.ui.view.components.EditTextSettingBox.Companion.TextOption

class ScreenFragmentModel : ViewModel() {
    var templateActivity: TemplateActivity? = null

    val keyboardVisible = RememberState(false)
    var money: RememberState<Double>? = null
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
                            editTextPrompt = TextAndText(
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
                            editTextPrompt = TextAndText(
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
                            editTextPrompt = TextAndText(
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
                            editTextPrompt = TextAndText(
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
                            editTextPrompt = TextAndText(
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
                            editTextPrompt = TextAndText(
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
                    
                }
            ))
            SettingItemColum(key = "对象", value = mutableListOf(
                {

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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ScreenAnimatedEditView(model: ScreenFragmentModel = viewModel()) {
    AnimatedVisibility(
        visible = model.keyboardVisible.getState().value
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .background(Color(213, 213, 215))
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