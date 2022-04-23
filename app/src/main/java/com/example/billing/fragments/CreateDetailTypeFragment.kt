package com.example.billing.fragments

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.billing.R
import com.example.billing.activitys.Billing
import com.example.billing.activitys.TemplateActivity
import com.example.billing.ui.theme.itemBackgroud
import com.example.billing.ui.theme.itemSelectedBackgroud
import com.example.billing.utils.datas.DetailType
import com.example.billing.utils.datas.DetailTypeState
import com.example.sport.ui.view.TopAppBar
import com.example.sport.ui.view.components.EditText
import com.example.sport.ui.view.components.EditTextIcon
import com.example.sport.ui.view.components.EditTextPromptBox
import com.example.sport.ui.view.components.EditTextSettingBox
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

class CreateDetailTypeFragmentModel : ViewModel() {
    @SuppressLint("StaticFieldLeak")
    var templateActivity: TemplateActivity? = null
    var detailFormState: DetailFormState? = null

    var list: MutableList<DetailTypeState>? = null

    @ExperimentalPagerApi
    var pagerState: PagerState? = null
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(
    ExperimentalPagerApi::class, ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class,
    androidx.compose.ui.ExperimentalComposeUiApi::class,
)
@Composable
fun CreateDetailTypeFragment(templateActivity: TemplateActivity) {
    val model: CreateDetailTypeFragmentModel = viewModel()
    val pagerState = rememberPagerState(pageCount = 2)
    val detailFormState = rememberDetailFormState()
    model.pagerState = pagerState
    model.detailFormState = detailFormState
    model.templateActivity = templateActivity
    Box {
        Column {
            CreateDetailTypeTopTitleView()
            HorizontalPager(state = pagerState) { page: Int ->
                when (page) {
                    0 -> {
                        DetailTypeColum(triad = true)
                    }

                    1 -> {
                        DetailTypeColum(triad = false)
                    }
                }
            }
        }
        Box(
            modifier = Modifier.align(Alignment.BottomCenter),
        ) {
            CreateDetailTypeAnimatedEditView()
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CreateDetailTypeTopTitleView(model: CreateDetailTypeFragmentModel = viewModel()) {
    val templateActivity = model.templateActivity!!
    val pagerState = model.pagerState!!
    val pages = listOf("收入", "支出")
    val coroutineScope = rememberCoroutineScope()
    TopAppBar(
        onLeft = {
            IconButton(
                onClick = {
                    Billing.saveData()
                    templateActivity.finish()
                    isRefreshing.value = true
                    isRefreshing.value = false
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp, 32.dp),
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        },
        onRight = {
            IconButton(
                onClick = {
                    val detailTypeState = DetailTypeState.SelfSubscribe()
                    Thread {
                        detailTypeState.id = Billing.db.getDetailTypeDao()
                            .insert(model.detailFormState!!.detailType.getState().value.getData()).toInt()
                    }.start()
                    model.detailFormState!!.run {
                        detailType set detailTypeState
                        visible set true
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp, 32.dp),
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
    ) {
        Text(
            text = "类别设置"
        )
    }
    TopAppBar(
        height = 30.dp
    ) {
        Row {
            pages.forEachIndexed() { index, title ->
                Box(
                    modifier = Modifier
                        .padding(bottom = 5.dp)
                        .weight(1f)
                        .clip(
                            if (index == 0) {
                                RoundedCornerShape(25, 0, 0, 25)
                            } else {
                                RoundedCornerShape(0, 25, 25, 0)
                            }
                        )
                        .background(
                            if (pagerState.currentPage == index) MaterialTheme.colors.onBackground else MaterialTheme.colors.primary
                        )
                        .border(2.dp, color = MaterialTheme.colors.onBackground)
                        .clickable {
                            coroutineScope.launch { pagerState.animateScrollToPage(index) }
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = title,
                        modifier = Modifier.padding(vertical = 5.dp),
                        fontSize = 12.sp,
                        color = if (pagerState.currentPage == index) MaterialTheme.colors.primary else MaterialTheme.colors.onBackground,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CreateDetailTypeAnimatedEditView(model: CreateDetailTypeFragmentModel = viewModel()) {
    val detailFormState = model.detailFormState!!
    AnimatedVisibility(
        visible = detailFormState.visible.getState().value,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colors.itemBackgroud)
                .fillMaxWidth()
        ) {
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
                            Thread {
                                Billing.db
                                    .getDetailTypeDao()
                                    .insert(model.detailFormState!!.detailType.getState().value.getData())
                            }.start()
                        }
                        .focusable(),
                    editTextSttting = EditTextSettingBox.TextOption() {
                        Thread {
                            Billing.db.getDetailTypeDao()
                                .insert(model.detailFormState!!.detailType.getState().value.getData())
                        }.start()
                        focusManager.clearFocus()
                    },
                    editTextPrompt = EditTextPromptBox.textAndText(
                        "名称",
                        "点击写名称..."
                    ),
                    editTextIcon = EditTextIcon(
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_message),
                                contentDescription = "名称",
                                modifier = Modifier.size(24.dp, 24.dp),
                                tint = MaterialTheme.colors.onBackground
                            )
                        },
                        trailingIcon = {
                            Text(
                                text = if (detailFormState.detailType.getState().value.triad.getState().value) "收入" else "支出",
                                modifier = Modifier
                                    .clickable(
                                        indication = null,
                                        interactionSource = MutableInteractionSource()
                                    ) {
                                        detailFormState.detailType.getState().value.triad set !detailFormState.detailType.getState().value.triad.value
                                        Thread {
                                            Billing.db.getDetailTypeDao()
                                                .insert(model.detailFormState!!.detailType.getState().value.getData())
                                        }.start()
                                        focusManager.clearFocus()
                                    }
                                    .padding(end = 15.dp)
                                ,
                                color = MaterialTheme.colors.onBackground,
                                maxLines = 1,
                                fontSize = 22.sp
                            )
                        }
                    ),
                    sidevalue = detailFormState.detailType.getState().value.name
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailTypeColum(triad: Boolean) {
    val model: CreateDetailTypeFragmentModel = viewModel()
    val detailFormState = model.detailFormState!!
    val templateActivity = model.templateActivity!!

    val list = Billing.db.getDetailTypeDao().queryWithTriad(triad).asLiveData()
    val listState = list.observeAsState(arrayListOf())

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = if (detailFormState.visible.getState().value) {
                PaddingValues(bottom = 230.dp)
            } else {
                PaddingValues(0.dp)
            },
        ) {
            items(items = listState.value) { it ->
                DetailTypeHorizontalView(detailType = it) {
                    if (it.diy) {
                        if (detailFormState.visible.value && detailFormState.detailType.value == it.getState()) {
                            detailFormState.visible set false
                            detailFormState.detailType set DetailTypeState.All
                        } else {
                            detailFormState.visible set true
                            detailFormState.detailType set it.getState()
                        }
                    } else {
                        Toast.makeText(
                            templateActivity,
                            "你无法修改${it.name}的数据!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailTypeHorizontalView(
    detailType: DetailType,
    model: CreateDetailTypeFragmentModel = viewModel(),
    onclick: () -> Unit
) {
    val detailFormState = model.detailFormState!!

    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
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
            modifier = Modifier
                .padding(5.dp)
                .weight(1f)
        )
        IconButton(
            onClick = {
                if (detailType.diy) {
                    Thread {
                        Billing.db.getDetailTypeDao().delete(detailType)
                    }.start()
                } else {
                    Toast.makeText(
                        model.templateActivity,
                        "你无法修改${detailType.name}的数据!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_remove),
                contentDescription = "删除",
                tint = Color.Red,
                modifier = Modifier.size(24.dp, 24.dp),
            )
        }
    }
}