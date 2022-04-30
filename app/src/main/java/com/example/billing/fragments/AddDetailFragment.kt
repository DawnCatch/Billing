package com.example.billing.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.billing.R
import com.example.billing.activitys.Billing
import com.example.billing.activitys.EXTRA_FRAGMENT
import com.example.billing.activitys.STATE_BAR
import com.example.billing.activitys.TemplateActivity
import com.example.billing.ui.theme.*
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
import com.google.gson.Gson
import kotlinx.coroutines.launch

class AddDetailFragmentModel : ViewModel() {
    var keyboardVisible: RememberState<Boolean> = RememberState(false)
    var timeVisible: RememberState<Boolean>? = null
    var channelVisible: RememberState<Boolean>? = null
    var directionVisible: RememberState<Boolean>? = null

    @SuppressLint("StaticFieldLeak")
    var templateActivity: TemplateActivity? = null
    var detailFormState: DetailFormState? = null

    @ExperimentalPagerApi
    var pagerState: PagerState? = null
    var detail = Detail(
        time = getTimeOfToday().getData(),
        money = 0.0,
        message = "",
        type = DetailTypeState.All.getData(),
        direction = MovDirectionState.NullT.getData(),
        channel = MovDirectionState.NullF.getData()
    ).getState()
    val modifierState = RememberState(true)
    val erroVisible = RememberState(false)
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

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(
    ExperimentalPagerApi::class, ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class,
    androidx.compose.ui.ExperimentalComposeUiApi::class,
)
@Composable
fun AddDetailFragment(
    templateActivity: TemplateActivity,
    model: AddDetailFragmentModel = viewModel()
) {
    val pagerState = rememberPagerState(pageCount = 2)
    model.detail = Gson().fromJson(
        templateActivity.intent.extras!!.getString(
            "DetailData",
            Gson().toJson(model.detail.getData())
        ), Detail::class.java
    ).getState()
    if (!model.detail.getData().type.triad) {
        val coroutineScope = rememberCoroutineScope()
        coroutineScope.launch { pagerState.scrollToPage(1) }
    }
    val detailFormState = rememberDetailFormState()
    model.pagerState = pagerState
    model.detailFormState = detailFormState
    model.templateActivity = templateActivity
    detailFormState.run {
        if (model.detail.getData().type.id != null) {
            val d =
                Billing.db.getDetailTypeDao().queryWithId(model.detail.getData().type.id!!)
                    .asLiveData()
            d.observe(templateActivity) { it ->
                if (it != null) {
                    detailFormState.detailType set it.getState()
                    visible set true
                } else {
                    model.modifierState set false
                    if (model.detail.getData().type.triad) {
                        detailFormState.detailType set DetailTypeState.UpAll
                    } else {
                        detailFormState.detailType set DetailTypeState.DownAll
                    }
                }
            }
        }
    }

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

//    if (model.erroVisible.getState().value) {
//        MDialog(
//            onDismissRequest = {
//                model.erroVisible set false
//            }
//        ) {
//            Text(text = "原来的明细格式已被删除", fontSize = 26.sp)
//            Text(text = "现将使用以下明细格式")
//            Row(
//                modifier = Modifier.width(500.dp),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.Center
//            ) {
//                DetailTypeVerticalView(detailType = model.detailFormState!!.detailType.getState().value.getData()) {
//                    model.erroVisible set false
//                }
//            }
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(3.dp)
//            ) {
//                Text(
//                    text = "取消",
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier
//                        .weight(1f)
//                        .clickable {
//                            model.erroVisible set false
//                        }
//                )
//                Spacer(
//                    modifier = Modifier
//                        .width(1.dp)
//                        .height(16.dp)
//                        .background(
//                            MaterialTheme.colors.onBackground.copy(0.5f)
//                        )
//                )
//                Text(
//                    text = "确定",
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier
//                        .weight(1f)
//                        .fillMaxWidth()
//                        .clickable {
//                            if (model.detail.money.value != 0.0) {
//                                model.detail.type set model.detailFormState!!.detailType
//                                Thread {
//                                    Billing.db
//                                        .getDetailDao()
//                                        .insert(model.detail.getData())
//                                }.start()
//                                model.templateActivity!!.finish()
//                                isRefreshing.value = true
//                                isRefreshing.value = false
//                            } else {
//                                Toast
//                                    .makeText(
//                                        templateActivity,
//                                        "金额:${model.detail.money.value}不是一个有意义的数字",
//                                        Toast.LENGTH_LONG
//                                    )
//                                    .show()
//                                model.erroVisible set false
//                            }
//                        }
//                )
//            }
//        }
//    }

    TopAppBar(
        onLeft = {
            IconButton(onClick = {
                if (model.modifierState.getState().value) {
                    templateActivity.finish()
                    isRefreshing.value = true
                    isRefreshing.value = false
                } else {
                    model.erroVisible set true
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "取消",
                    modifier = Modifier
                        .size(32.dp, 32.dp),
                    tint = MaterialTheme.colors.onPrimary
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
                                    color = if (pagerState.currentPage == index) MaterialTheme.colors.secondary else Color.Unspecified,
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
    type: Boolean,
    owner: LifecycleOwner,
    create: () -> Unit
) {
    val list = Billing.db.getMovDirectionDao().queryWithType(type).asLiveData()
    val listState = list.observeAsState(arrayListOf())

    val itemHeight = 40

    var height by remember {
        mutableStateOf(0)
    }
//    list.observe(model.templateActivity!!) {
    list.observe(owner) {
        if (it.isEmpty()) {
            height = itemHeight
        } else {
            height = it.size * itemHeight
        }
    }

    MDialog(modifier = Modifier.height(height.dp), visible = visible) {
        listState.value.forEach {
            if (it != MovDirectionState.NullT.getData() && it != MovDirectionState.NullF.getData()) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    .width(200.dp)
                    .height(itemHeight.dp)
                    .clickable {
                        if (value.value == it.getState()) {
                            value set if (type) MovDirectionState.NullT else MovDirectionState.NullF
                        } else {
                            value set it.getState()
                        }
                        visible set false
                    }) {
                    Text(
                        text = it.name,
                        textAlign = TextAlign.Left,
                        modifier = Modifier
                            .weight(3f)
                            .padding(10.dp)
                            .padding(start = 15.dp)
                    )
                    if (value.getState().value == it.getState()) {
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
        }
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
            .width(200.dp)
            .height(itemHeight.dp)
            .clickable {
                value set if (type) MovDirectionState.NullT else MovDirectionState.NullF
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

@OptIn(ExperimentalFoundationApi::class, com.google.accompanist.pager.ExperimentalPagerApi::class)
@Composable
fun DetailTypeGrid(
    triad: Boolean,
    model: AddDetailFragmentModel = viewModel()
) {
    val detailFormState = model.detailFormState!!
    val templateActivity = model.templateActivity!!

    val list = Billing.db.getDetailTypeDao().queryWithTriad(triad).asLiveData()
    val listState = list.observeAsState(arrayListOf())

    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            cells = GridCells.Fixed(4),
            contentPadding = if (detailFormState.visible.getState().value) {
                PaddingValues(bottom = 230.dp)
            } else {
                PaddingValues(0.dp)
            }
        ) {
            items(listState.value) { it ->
                DetailTypeVerticalView(detailType = it) {
                    if (detailFormState.visible.value && detailFormState.detailType.value == it.getState()) {
                        detailFormState.visible set false
                        detailFormState.detailType set DetailTypeState.All
                    } else {
                        detailFormState.visible set true
                        detailFormState.detailType set it.getState()
                    }
                }
            }
            item {
                DetailTypeVerticalView(
                    detailType = DetailTypeState(
                        name = RememberState("设置"),
                        triad = RememberState(true)
                    ).getData()
                ) {
                    val bundle = Bundle()
                    bundle.putString(EXTRA_FRAGMENT, "类别设置")
                    bundle.putBoolean(STATE_BAR, false)
                    bundle.putBoolean("Triad",triad)
                    templateActivity.startActivity(
                        Intent(templateActivity, TemplateActivity::class.java).putExtras(
                            bundle
                        )
                    )
//                    detailFormState.visible set false
//                    detailFormState.detailType set DetailTypeState.All
                }
            }
        }
    }
}

@Composable
fun DetailTypeVerticalView(
    detailType: DetailType,
    onclick: (() -> Unit)? = null
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
                onclick?.let { it() }
            }
    ) {
        Box(
            modifier = Modifier
                .size(48.dp, 48.dp)
                .background(
                    color = if (detailFormState.detailType.getState().value == detailType.getState()) MaterialTheme.colors.itemSelectedBackgroud else MaterialTheme.colors.itemBackgroud,
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
            text = detailType.name,
            modifier = Modifier.padding(5.dp)
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AddDetailAnimatedEditView() {
    val model: AddDetailFragmentModel = viewModel()
    val timeVisible = RememberState(false)
    val channelVisible = RememberState(false)
    val directionVisible = RememberState(false)
    val detail = model.detail
    val templateActivity = model.templateActivity!!
    val detailFormState = model.detailFormState!!
    val keyboardVisible = model.keyboardVisible
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
    MovDirectionCheck(channelVisible, "渠道", detail.channel, false, model.templateActivity!!) {
        val bundle = Bundle()
        bundle.putString(EXTRA_FRAGMENT, "渠道设置")
        bundle.putBoolean(STATE_BAR, false)
        templateActivity.startActivity(
            Intent(templateActivity, TemplateActivity::class.java).putExtras(
                bundle
            )
        )
    }
    MovDirectionCheck(directionVisible, "对象", detail.direction, true, model.templateActivity!!) {
        val bundle = Bundle()
        bundle.putString(EXTRA_FRAGMENT, "对象设置")
        bundle.putBoolean(STATE_BAR, false)
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
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.background),
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
                        editTextPrompt = EditTextPromptBox.textAndText(
                            "备注",
                            "点击写备注..."
                        ),
                        editTextIcon = EditTextIcon(
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_message),
                                    contentDescription = "备注",
                                    modifier = Modifier.size(24.dp, 24.dp),
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
fun KeyboardView(model: AddDetailFragmentModel = viewModel()) {
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
                if (detail.money.value.toString().length < 9 || pointAfter || it == ".") {
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
                } else {
                    Toast.makeText(model.templateActivity!!, "超过最大长度", Toast.LENGTH_SHORT).show()
                }
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
                        .background(MaterialTheme.colors.keyboardTime)
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
                    text = if (detail.channel.getState().value == MovDirectionState.NullF) "渠道" else detail.channel.getState().value.name.getState().value,
                    modifier = Modifier
                        .background(MaterialTheme.colors.keyboardMov)
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
                    text = if (detail.direction.getState().value == MovDirectionState.NullT) "对象" else detail.direction.getState().value.name.getState().value,
                    modifier = Modifier
                        .background(MaterialTheme.colors.keyboardMov)
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
                        .background(MaterialTheme.colors.itemSelectedBackgroud)
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
                        } else {
                            Toast.makeText(
                                templateActivity,
                                "金额:${detail.money.value}不是一个有意义的数字",
                                Toast.LENGTH_LONG
                            ).show()
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