package com.example.billing.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.billing.R
import com.example.billing.activitys.Billing
import com.example.billing.activitys.EXTRA_FRAGMENT
import com.example.billing.activitys.STATE_BAR
import com.example.billing.activitys.TemplateActivity
import com.example.billing.utils.*
import com.example.sport.ui.view.MDialog
import com.example.sport.ui.view.TimeSelectView
import com.example.sport.ui.view.TopAppBar
import com.example.sport.ui.view.components.EditText
import com.example.sport.ui.view.components.EditTextIcon
import com.example.sport.ui.view.components.EditTextPromptBox
import com.example.sport.ui.view.components.EditTextSettingBox
import com.example.billing.utils.RememberState
import com.example.billing.utils.datas.*
import com.example.billing.utils.getTimeOfToday
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

class AddDetailFragmentModel : ViewModel() {
    var keyboardVisible: RememberState<Boolean>? = null
    var timeVisible: RememberState<Boolean>? = null
    var channelVisible: RememberState<Boolean>? = null
    var directionVisible: RememberState<Boolean>? = null

    @SuppressLint("StaticFieldLeak")
    var templateActivity: TemplateActivity? = null
    var detailFormState: DetailFormState? = null

    @ExperimentalPagerApi
    var pagerState: PagerState? = null
    val detail = Detail(
        time = getTimeOfToday().getData(),
        money = 0.0,
        message = "",
        type = DetailTypeState.All.getData(),
        direction = MovDirectionState.All.getData(),
        channel = MovDirectionState.All.getData()
    ).getState()
}

@Stable
class DetailFormState(
    val visible: RememberState<Boolean>,
    val detailType: RememberState<DetailTypeState>,
)

@Composable
fun rememberDetailFormState() = remember {
    DetailFormState(visible = RememberState(false), detailType = RememberState(DetailTypeState.All))
}

@OptIn(
    ExperimentalPagerApi::class, ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class,
    androidx.compose.ui.ExperimentalComposeUiApi::class,
)
@Composable
fun AddDetailFragment(templateActivity: TemplateActivity) {
    val model: AddDetailFragmentModel = viewModel()
    val pagerState = rememberPagerState(pageCount = 2)
    val detailFormState = rememberDetailFormState()
    model.pagerState = pagerState
    model.detailFormState = detailFormState
    model.templateActivity = templateActivity

    Box {
        Column {
            AddDetailTopTitleView()
            HorizontalPager(state = pagerState) { page: Int ->
                when (page) {
                    0 -> {
                        DetailTypeGrid(triad = true)
                    }

                    1 -> {
                        DetailTypeGrid(triad = false)
                    }
                }
            }
        }
        Box(
            modifier = Modifier.align(Alignment.BottomCenter),
        ) {
            AddDetailAnimatedEditView()
        }
    }

}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AddDetailTopTitleView() {
    val model: AddDetailFragmentModel = viewModel()
    val templateActivity = model.templateActivity!!
    val pagerState = model.pagerState!!
    val pages = listOf("收入", "支出")
    val coroutineScope = rememberCoroutineScope()

    TopAppBar(
        onLeft = {
            IconButton(onClick = {
                templateActivity.finish()
                isRefreshing.value = true
                isRefreshing.value = false
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "取消",
                    modifier = Modifier
                        .size(32.dp, 32.dp),
                    tint = Color.White
                )
            }
        }
    ) {
        Row {
            Spacer(modifier = Modifier.weight(0.5f))
            pages.forEachIndexed() { index, title ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            indication = null,
                            interactionSource = MutableInteractionSource()
                        ) {
                            coroutineScope.launch { pagerState.animateScrollToPage(index) }
                        }
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Text(
                            title,
                            maxLines = 1,
                            fontSize = 16.sp,
                            fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal
                        )
                        Spacer(
                            modifier = Modifier
                                .padding(top = 13.dp)
                                .size(70.dp, 5.dp)
                                .background(
                                    color = if (pagerState.currentPage == index) Color.Green else Color.Unspecified,
                                    shape = RoundedCornerShape(100)
                                )
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(0.5f))
        }
    }
}

@Composable
fun MovDirectionCheck(
    visible: RememberState<Boolean>,
    contentDescription: String,
    value: RememberState<MovDirectionState>,
    values: MutableList<MovDirectionState>,
    create: () -> Unit
) {
    MDialog(visible = visible) {
        for (i in 0 until values.size) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .width(200.dp)
                .clickable {
                    if (value.value.equals(values[i])) {
                        value set MovDirectionState.All
                    } else {
                        value set values[i]
                    }
                    visible set false
                }) {
                Text(
                    text = Billing.sBillingData.channels[i].name.getState().value,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .weight(3f)
                        .padding(10.dp)
                        .padding(start = 15.dp)
                )
                if (value.getState().value.equals(values[i])) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = contentDescription,
                        modifier = Modifier.weight(
                            1f
                        )
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .width(200.dp)
            .clickable {
                visible set false
                create()
            }) {
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

@OptIn(ExperimentalFoundationApi::class, com.google.accompanist.pager.ExperimentalPagerApi::class)
@Composable
fun DetailTypeGrid(
    triad: Boolean,
) {
    val model: AddDetailFragmentModel = viewModel()
    val detailFormState = model.detailFormState!!
    val templateActivity = model.templateActivity!!
    val pagerState = model.pagerState!!

    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            cells = GridCells.Fixed(4),
            contentPadding = if (detailFormState.visible.getState().value) {
                PaddingValues(bottom = 230.dp)
            } else {
                PaddingValues(0.dp)
            }
        ) {
            Billing.sBillingData.detailTypes.forEach {
                if (it.triad.getState().value == triad) {
                    item {
                        DetailTypeVerticalView(detailType = it) {
                            if (detailFormState.visible.value && detailFormState.detailType.value == it) {
                                detailFormState.visible set false
                                detailFormState.detailType set DetailTypeState.All
                            } else {
                                detailFormState.visible set true
                                detailFormState.detailType set it
                            }
                        }
                    }
                }
            }
            item {
                DetailTypeVerticalView(
                    detailType = DetailTypeState(
                        name = RememberState("设置"),
                        triad = RememberState(true)
                    )
                ) {
                    val bundle = Bundle()
                    bundle.putString(EXTRA_FRAGMENT, "类别设置")
                    bundle.putBoolean(STATE_BAR, false)
                    templateActivity.startActivity(
                        Intent(templateActivity, TemplateActivity::class.java).putExtras(
                            bundle
                        )
                    )
                    detailFormState.visible set false
                    detailFormState.detailType set DetailTypeState.All
                }
            }
        }
    }
}

@Composable
fun DetailTypeVerticalView(
    detailType: DetailTypeState,
    onclick: () -> Unit
) {
    val model: AddDetailFragmentModel = viewModel()
    val detailFormState = model.detailFormState!!
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(8.dp)
            .clickable(
                indication = null,
                interactionSource = MutableInteractionSource()
            ) {
                onclick()
            }
    ) {
        Box(
            modifier = Modifier
                .size(48.dp, 48.dp)
                .background(
                    color = if (detailFormState.detailType.getState().value == detailType) Color.Yellow else Color(
                        242,
                        243,
                        245
                    ),
                    shape = RoundedCornerShape(100)
                )
        ) {
            Icon(
                painter = painterResource(id = detailType.icon),
                contentDescription = "类型",
                modifier = Modifier
                    .size(36.dp, 36.dp)
                    .align(Alignment.Center)
            )
        }
        Text(
            text = detailType.name.getState().value,
            modifier = Modifier.padding(5.dp)
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AddDetailAnimatedEditView() {
    val model: AddDetailFragmentModel = viewModel()
    val keyboardVisible = RememberState(true)
    val timeVisible = RememberState(false)
    val channelVisible = RememberState(false)
    val directionVisible = RememberState(false)
    val detail = model.detail
    val templateActivity = model.templateActivity!!
    val detailFormState = model.detailFormState!!
    model.keyboardVisible = keyboardVisible
    model.timeVisible = timeVisible
    model.channelVisible = channelVisible
    model.directionVisible = directionVisible

    MDialog(visible = timeVisible) {
        Row(
            modifier = Modifier.width(500.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            TimeSelectView(
                start = 1,
                end = 3000,
                default = detail.time.getState().value.year.value,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .padding(vertical = 25.dp),
                selected = {
                    detail.time.getState().value.year set it
                }
            )
            Spacer(modifier = Modifier.width(50.dp))
            TimeSelectView(
                start = 1,
                end = 12,
                default = detail.time.getState().value.month.value,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .padding(vertical = 25.dp),
                selected = {
                    detail.time.getState().value.month set it
                }
            )
            Spacer(modifier = Modifier.width(50.dp))
            TimeSelectView(
                start = 1,
                end = 31,
                default = detail.time.getState().value.dayOfMonth.value,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .padding(vertical = 25.dp),
                selected = {
                    detail.time.getState().value.dayOfMonth set it
                }
            )
        }
    }  //时间
    MovDirectionCheck(channelVisible, "渠道", detail.channel, Billing.sBillingData.channels) {
        val bundle = Bundle()
        bundle.putString(EXTRA_FRAGMENT, "渠道设置")
        templateActivity.startActivity(
            Intent(templateActivity, TemplateActivity::class.java).putExtras(
                bundle
            )
        )
    }
    MovDirectionCheck(directionVisible, "对象", detail.direction, Billing.sBillingData.directions) {
        val bundle = Bundle()
        bundle.putString(EXTRA_FRAGMENT, "对象设置")
        templateActivity.startActivity(
            Intent(templateActivity, TemplateActivity::class.java).putExtras(
                bundle
            )
        )
    }
    AnimatedVisibility(
        visible = detailFormState.visible.getState().value,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Column(
            modifier = Modifier
                .background(color = Color(242, 243, 245))
                .fillMaxWidth()
        ) {
            Column {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val focusManager = LocalFocusManager.current
                    EditText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged {
                                keyboardVisible set !it.hasFocus
                            }
                            .focusable(),
                        editTextSttting = EditTextSettingBox.TextOption() {
                            focusManager.clearFocus()
                            keyboardVisible set true
                        },
                        editTextPrompt = EditTextPromptBox.TextAndText(
                            "备注",
                            "点击写备注..."
                        ),
                        editTextIcon = EditTextIcon(
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_message),
                                    contentDescription = "备注",
                                    modifier = Modifier.size(24.dp, 24.dp)
                                )
                            },
                            trailingIcon = {
                                Text(
                                    text = detail.money.getState().value.toString(),
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
                                    fontSize = 22.sp
                                )
                            }
                        ),
                        sidevalue = detail.message
                    )
                }
                KeyboardView()
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun KeyboardView() {
    val model: AddDetailFragmentModel = viewModel()
    val detailFormState = model.detailFormState!!
    val keyboardVisible = model.keyboardVisible!!
    val timeVisible = model.timeVisible!!
    val channelVisible = model.channelVisible!!
    val directionVisible = model.directionVisible!!
    val templateActivity = model.templateActivity!!
    val detail = model.detail

    AnimatedVisibility(
        visible = keyboardVisible.getState().value
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
                detail.money.value.toString().split(".").run {
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
                detail.money set "$integer.${float}".toDouble()
            }
            Row(Modifier.fillMaxWidth()) {
                TextKeyboardItem(text = "7", modifier = modifier, onclick = onclick)
                TextKeyboardItem(text = "8", modifier = modifier, onclick = onclick)
                TextKeyboardItem(text = "9", modifier = modifier, onclick = onclick)
                KeyboardItem(
                    lable = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            if (detail.time.getState().value == getTimeOfToday()) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_time),
                                    modifier = Modifier.size(16.dp),
                                    contentDescription = "日期"
                                )
                                Text(text = "今天")
                            } else {
                                Text(text = detail.time.getState().value.toString())
                            }
                        }
                    },
                    modifier = Modifier
                        .background(Color(248, 238, 235))
                        .then(modifier)
                ) {
                    timeVisible set true
                }
            }
            Row(Modifier.fillMaxWidth()) {
                TextKeyboardItem(text = "4", modifier = modifier, onclick = onclick)
                TextKeyboardItem(text = "5", modifier = modifier, onclick = onclick)
                TextKeyboardItem(text = "6", modifier = modifier, onclick = onclick)
                TextKeyboardItem(
                    text = if (detail.channel.getState().value == MovDirectionState.All) "渠道" else detail.channel.getState().value.name.value,
                    modifier = Modifier
                        .background(Color(144, 218, 228, 255))
                        .then(modifier)
                ) {
                    channelVisible set true
                }
            }
            Row(Modifier.fillMaxWidth()) {
                TextKeyboardItem(text = "1", modifier = modifier, onclick = onclick)
                TextKeyboardItem(text = "2", modifier = modifier, onclick = onclick)
                TextKeyboardItem(text = "3", modifier = modifier, onclick = onclick)
                TextKeyboardItem(
                    text = if (detail.direction.getState().value == MovDirectionState.All) "对象" else detail.direction.getState().value.name.value,
                    modifier = Modifier
                        .background(Color(144, 218, 228, 255))
                        .then(modifier)
                ) {
                    directionVisible set true
                }
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
                    detail.money.value.toString().split(".").run {
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
                    detail.money set "$integer.${float}".toDouble()
                }
                TextKeyboardItem(
                    text = "完成", modifier = Modifier
                        .background(Color.Yellow)
                        .then(modifier),
                    onclick = {
                        if (detail.money.value != 0.0) {
                            detail.type set detailFormState.detailType
                            Thread {
                                Billing.db.getDetailDao().insert(detail.getData())
                            }.start()
                            templateActivity.finish()
                            isRefreshing.value = true
                            isRefreshing.value = false
                        }else {
                            Toast.makeText(templateActivity,"金额:${detail.money.value}不是一个有意义的数字",Toast.LENGTH_LONG).show()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun KeyboardItem(
    lable: (@Composable () -> Unit),
    modifier: Modifier = Modifier,
    onclick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clickable {
                onclick()
            }
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        lable()
    }
}

@Composable
fun TextKeyboardItem(
    text: String,
    modifier: Modifier = Modifier,
    onclick: (String) -> Unit
) = KeyboardItem(
    lable = {
        Text(text = text)
    },
    modifier = modifier,
) {
    onclick(text)
}

@Composable
fun IconKeyboardItem(
    icon: Int,
    contentDescription: String = "按钮",
    modifier: Modifier = Modifier,
    onclick: () -> Unit
) = KeyboardItem(
    lable = {
        Icon(
            painter = painterResource(id = icon),
            modifier = Modifier.size(22.dp),
            contentDescription = contentDescription
        )
    },
    modifier = modifier,
    onclick = onclick
)