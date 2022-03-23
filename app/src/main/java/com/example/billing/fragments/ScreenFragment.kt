package com.example.billing.fragments

import android.util.Log
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
import com.example.billing.R
import com.example.billing.utils.RememberState
import com.example.billing.utils.datas.Screening
import com.example.sport.ui.view.SettingItemColum
import com.example.sport.ui.view.components.EditText
import com.example.sport.ui.view.components.EditTextIconBox.Companion.Null
import com.example.sport.ui.view.components.EditTextPromptBox.Companion.TextAndText
import com.example.sport.ui.view.components.EditTextSettingBox.Companion.NumberOption
import com.example.sport.ui.view.components.EditTextSettingBox.Companion.TextOption

@Composable
fun ScreenFragment() {
    val screening = Screening()
    val focusManager = LocalFocusManager.current
    val keyboardVisible = RememberState(false)
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
                ) {
                    Text(text = "最小", fontSize = 12.sp)
                    Text(
                        text = screening.minMoney.getState().value.toString(),
                        modifier = Modifier
                            .clickable(
                                indication = null,
                                interactionSource = MutableInteractionSource()
                            ) {
                                focusManager.clearFocus()
                                keyboardVisible set true
                            },
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
                ) {
                    Text(
                        text = "最大",
                        fontSize = 12.sp,
                        modifier = Modifier.align(Alignment.End)
                    )
                    Text(
                        text = screening.maxMoney.getState().value.toString(),
                        modifier = Modifier
                            .align(Alignment.End)
                            .clickable(
                                indication = null,
                                interactionSource = MutableInteractionSource()
                            ) {
                                focusManager.clearFocus()
                                keyboardVisible set true
                            },
                        color = Color.Black,
                        maxLines = 1,
                        fontSize = 16.sp
                    )
                }
            }
        }
    ))
}