package com.example.billing.activitys

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.billing.R
import com.example.billing.fragments.*
import com.example.billing.ui.view.components.*
import com.example.sport.ui.view.MDialog
import com.example.sport.ui.view.TopAppBar
import com.example.billing.utils.RememberState
import com.example.billing.utils.datas.MovDirectionState
import com.example.billing.utils.datas.Screening
import com.example.billing.utils.datas.TimeState
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {

    var exitTime: Long = 0

    override fun onBackPressed() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_LONG).show()
            exitTime = System.currentTimeMillis()
        } else {
            finish()
        }
    }

    override fun other() {
        super.other()
        StatusBar set false
    }

    @SuppressLint("CoroutineCreationDuringComposition")
    @OptIn(ExperimentalPagerApi::class)
    @Composable
    override fun Content() {
        val scaffoldState = rememberScaffoldState()
        val scope = rememberCoroutineScope()
        if (Billing.sSettings.navDefault.value != 0) {
            Billing.sSettings.navLastTimeSelected =
                RememberState(Billing.sSettings.navDefault.value - 1)
        }
        val pagerState = rememberPagerState(
            pageCount = 4,
            initialPage = Billing.sSettings.navLastTimeSelected.value,
            initialOffscreenLimit = 4
        )
        Billing.sSettings.navLastTimeSelected set pagerState.currentPage
        val title = RememberState<String>(
            listOf<String>(
                "明细", "图表", "借入", "借出"
            )[pagerState.currentPage]
        )
        var drawerGesturesEnabled by remember {
            mutableStateOf(false)
        }
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen
        val topContent = RememberState<@Composable () -> Unit>(
            {
                Text(
                    text = title.getState().value,
                )
            }
        )
        Scaffold(
            scaffoldState = scaffoldState,
            drawerContent = {
                Navigation(scaffoldState)
            },
            drawerGesturesEnabled = drawerGesturesEnabled,
            bottomBar = {
                BottomNavigation(pagerState = pagerState)
            },
            drawerShape = RoundedCornerShape(0, 4, 4, 0),
            //标题栏区域
            topBar = {
                TopAppBar(
                    onLeft = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    scaffoldState.drawerState.open()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    },
                    onRight = {
                        IconButton(
                            onClick = {
                                val bundle = Bundle()
                                bundle.putString(EXTRA_FRAGMENT, "筛选")
                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        TemplateActivity::class.java
                                    ).putExtras(
                                        bundle
                                    )
                                )
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_screening),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp, 24.dp),
                                tint = Color.White
                            )
                        }
                    }) {
                    topContent.getState().value()
                }
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    val bundle = Bundle()
                    bundle.putString(EXTRA_FRAGMENT, "添加明细")
                    bundle.putBoolean(STATE_BAR, false)
                    bundle.putBoolean(RE_INIT,true)
                    startActivity(
                        Intent(this@MainActivity, TemplateActivity::class.java).putExtras(
                            bundle
                        )
                    )
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_plus),
                        contentDescription = "添加"
                    )
                }
            },
            isFloatingActionButtonDocked = true,
            floatingActionButtonPosition = if (Billing.sSettings.bottomBarStyle.getState().value) FabPosition.Center else FabPosition.End,

            //屏幕内容区域
            content = {
                HorizontalPager(state = pagerState) { page: Int ->
                    when (page) {
                        0 -> {
                            HostFragment(this@MainActivity)
                        }
                        1 -> {
                            HostFragment(this@MainActivity)
                        }
                        2 -> {
                            topContent set {
                                TopBar(
                                    title = title,
                                    array = Billing.sBillingData.directions,
                                    checked = Billing.sSettings.borrowersChecked
                                )
                            }
                            if (Billing.sSettings.borrowersChecked.getState().value == MovDirectionState.All &&
                                Billing.sBillingData.directions.indexOf(Billing.sSettings.borrowersChecked.getState().value) != -1
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "null",
                                        Modifier.size(50.dp, 50.dp)
                                    )
                                    Text(text = "暂无数据")
                                }
                            } else {
                                BorrowingFragment(this@MainActivity)
                            }
                        }
                        3 -> {
                            topContent set {
                                TopBar(
                                    title = title,
                                    array = Billing.sBillingData.directions,
                                    checked = Billing.sSettings.lendersChecked
                                )
                            }
                            if (Billing.sSettings.lendersChecked.getState().value == MovDirectionState.All &&
                                Billing.sBillingData.directions.indexOf(Billing.sSettings.lendersChecked.getState().value) != -1
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "null",
                                        Modifier.size(50.dp, 50.dp)
                                    )
                                    Text(text = "暂无数据")
                                }
                            } else {
                                LendingFragment(this@MainActivity)
                            }
                        }
                    }
                }
            }
        )
    }

    @Composable
    fun Navigation(scaffoldState: ScaffoldState) {
        val scope = rememberCoroutineScope()
        Box {
            Image(
                painter = painterResource(id = R.drawable.ic_nav_bg),
                contentDescription = "侧边栏背景"
            )
        }
        val bundle = Bundle()
        MenuColum {
            RowMenuItem(
                text = "设置",
                icon = R.drawable.ic_setting,
                id = "设置",
                scaffoldState = scaffoldState
            ) {
                bundle.putString(EXTRA_FRAGMENT, it)
                startActivity(
                    Intent(this@MainActivity, TemplateActivity::class.java).putExtras(
                        bundle
                    )
                )
            }
            RowMenuItem(
                text = "关注",
                icon = R.drawable.ic_star,
                id = "关注",
                scaffoldState = scaffoldState
            ) {
                bundle.putString(EXTRA_FRAGMENT, it)
                startActivity(
                    Intent(this@MainActivity, TemplateActivity::class.java).putExtras(
                        bundle
                    )
                )
            }
            HorizontallyLine()
            MenuGroup("其他") {
                RowMenuItem(
                    text = "关于",
                    icon = R.drawable.ic_error,
                    id = "关于",
                    scaffoldState = scaffoldState
                ) {
                    bundle.putString(EXTRA_FRAGMENT, it)
                    startActivity(
                        Intent(this@MainActivity, TemplateActivity::class.java).putExtras(
                            bundle
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun TopBar(
    title: RememberState<String>,
    array: MutableList<MovDirectionState>,
    checked: RememberState<MovDirectionState>
) {
    val newArray = mutableListOf<MovDirectionState>()
    array.forEach {
        if (it.type.value) {
            newArray.add(it)
        }
    }
    val visible = RememberState(false)
    Text(
        text = title.getState().value,
        color = Color.White,
        modifier = Modifier.clickable(
            indication = null,
            interactionSource = MutableInteractionSource()
        ) {
            visible set true
        }
    )
    if (newArray.size != 0) {
        MDialog(visible = visible) {
            newArray.forEachIndexed { index, it ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .width(200.dp)
                        .clickable {
                            checked set newArray[index]
                            visible set false
                        }
                ) {
                    Text(
                        text = it.name.getState().value,
                        textAlign = TextAlign.Left,
                        color = Color.Black,
                        modifier = Modifier
                            .weight(3f)
                            .padding(10.dp)
                            .padding(start = 15.dp)
                    )
                    if (checked.allEtc(newArray[index])) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = it.name.getState().value,
                            modifier = Modifier.weight(
                                1f
                            ),
                            tint = Color.Black
                        )
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun BottomNavigation(pagerState: PagerState) {
    MenuRow {
        Box(Modifier.weight(1f)) {
            ColumMenuItem(
                text = "明细",
                icon = R.drawable.ic_borrowing_and_lending,
                id = 0,
                navController = pagerState
            )
        }
        Box(Modifier.weight(1f)) {
            ColumMenuItem(
                text = "图表",
                icon = R.drawable.ic_chart,
                id = 1,
                navController = pagerState
            )
        }
        if (Billing.sSettings.bottomBarStyle.getState().value) {
            Spacer(modifier = Modifier.weight(0.8f))
        }
        Box(Modifier.weight(1f)) {
            ColumMenuItem(
                text = "借入",
                icon = R.drawable.ic_borrowing_registration,
                id = 2,
                navController = pagerState
            )
        }
        Box(Modifier.weight(1f)) {
            ColumMenuItem(
                text = "借出",
                icon = R.drawable.ic_lending_registration,
                id = 3,
                navController = pagerState
            )
        }
        if (!Billing.sSettings.bottomBarStyle.getState().value) {
            Spacer(modifier = Modifier.weight(0.8f))
        }
    }
}