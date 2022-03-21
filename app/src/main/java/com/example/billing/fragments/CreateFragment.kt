package com.example.billing.fragments

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.billing.activitys.Billing
import com.example.billing.activitys.TemplateActivity
import com.example.billing.utils.datas.DetailTypeState
import com.example.sport.ui.view.TopAppBar
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
fun CreateDetailTypeTopTitleView() {
    val model: CreateDetailTypeFragmentModel = viewModel()
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
                    tint = Color.White
                )
            }
        },
        onRight = {
            IconButton(
                onClick = {

                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp, 32.dp),
                    tint = Color.White
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
                            if (pagerState.currentPage == index) Color.Black else Color(
                                98,
                                0,
                                238
                            )
                        )
                        .border(2.dp, color = Color.Black)
                        .clickable {
                            coroutineScope.launch { pagerState.animateScrollToPage(index) }
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = title,
                        modifier = Modifier.padding(vertical = 5.dp),
                        fontSize = 12.sp,
                        color = if (pagerState.currentPage == index) Color(
                            98,
                            0,
                            238
                        ) else Color.Black,
                    )
                }
            }
        }
    }
}

@Composable
fun CreateDetailTypeAnimatedEditView() {

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailTypeColum(triad: Boolean) {
    val model: CreateDetailTypeFragmentModel = viewModel()
    val detailFormState = model.detailFormState!!
    val templateActivity = model.templateActivity!!

    var list by remember {
        mutableStateOf(Billing.sBillingData.detailTypes)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = if (detailFormState.visible.getState().value) {
                PaddingValues(bottom = 230.dp)
            } else {
                PaddingValues(0.dp)
            },
        ) {
            items(items = list, key = { it.name.getState().value }) { it ->
                val dismissState = rememberDismissState()
                if (dismissState.isDismissed(DismissDirection.StartToEnd)) {
                    list = list.toMutableList().also { detailTypes ->
                        detailTypes.remove(it)
                    }
                    Billing.sBillingData.detailTypes.remove(it)
                    Billing.saveData()
                }
                if (it.triad.getState().value == triad) {
                    SwipeToDismiss(
                        state = dismissState,
                        background = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.horizontalGradient(
                                            colors = listOf(
                                                Color.Red,
                                                Color.White
                                            ),
                                            endX = 1000f
                                        )
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    modifier = Modifier
                                        .align(Alignment.CenterStart)
                                        .padding(start = 15.dp),
                                    contentDescription = "删除"
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        dismissThresholds = {
                            FractionalThreshold(if (it == DismissDirection.StartToEnd) 0.25f else 0.5f)
                        },
                        directions = setOf(DismissDirection.StartToEnd)
                    ) {
                        DetailTypeHorizontalView(detailType = it) {
                            if (it.diy) {
                                if (detailFormState.visible.value && detailFormState.detailType.value == it) {
                                    detailFormState.visible set false
                                    detailFormState.detailType set DetailTypeState.All
                                } else {
                                    detailFormState.visible set true
                                    detailFormState.detailType set it
                                }
                            } else {
                                Toast.makeText(
                                    templateActivity,
                                    "你无法修改${it.name.value}的数据!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailTypeHorizontalView(
    detailType: DetailTypeState,
    onclick: () -> Unit
) {
    val model: CreateDetailTypeFragmentModel = viewModel()
    val detailFormState = model.detailFormState!!

    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
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
            modifier = Modifier
                .padding(5.dp)
        )
    }
}

@Composable
fun CreateDirectionFragment(templateActivity: TemplateActivity) {

}

@Composable
fun CreateChannelFragment(templateActivity: TemplateActivity) {

}