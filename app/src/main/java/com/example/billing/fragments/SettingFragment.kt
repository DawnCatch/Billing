package com.example.billing.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.billing.R
import com.example.billing.activitys.*
import com.example.billing.ui.view.components.SettingItemView
import com.example.sport.ui.view.SettingItemColum
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@SuppressLint("UnusedCrossfadeTargetStateParameter")
@Composable
fun SettingFragment(templateActivity: TemplateActivity) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            SettingItemColum(key = "添加设置", value = mutableListOf(
                {
                    SettingItemView(title = "类别") {
                        val bundle = Bundle()
                        bundle.putString(EXTRA_FRAGMENT, "类别设置")
                        bundle.putBoolean(STATE_BAR, false)
                        templateActivity.startActivity(
                            Intent(templateActivity, TemplateActivity::class.java).putExtras(
                                bundle
                            )
                        )
                    }
                },
                {
                    SettingItemView(title = "渠道") {
                        val bundle = Bundle()
                        bundle.putString(EXTRA_FRAGMENT, "渠道设置")
                        bundle.putBoolean(STATE_BAR, false)
                        templateActivity.startActivity(
                            Intent(templateActivity, TemplateActivity::class.java).putExtras(
                                bundle
                            )
                        )
                    }
                },
                {
                    SettingItemView(title = "对象") {
                        val bundle = Bundle()
                        bundle.putString(EXTRA_FRAGMENT, "对象设置")
                        bundle.putBoolean(STATE_BAR, false)
                        templateActivity.startActivity(
                            Intent(templateActivity, TemplateActivity::class.java).putExtras(
                                bundle
                            )
                        )
                    }
                }
            ))
        }
        item {
            SettingItemColum(key = "系统", value = mutableListOf(
                {
                    SettingItemView(
                        title = "底部导航栏",
                        selectlist = mutableListOf(
                            "上次关闭位置", "明细", "图表"
                        ),
                        default = Billing.sSettings.navDefault.getState().value,
                        onClickeditem = {
                            Billing.sSettings.navDefault set it
                            Billing.saveData()
                        }
                    )
                },
                {
                    SettingItemView(
                        title = "悬浮按钮位置",
                        checketext = mutableListOf(
                            "靠右", "居中"
                        ), default = Billing.sSettings.bottomBarStyle.value, onCheckedChange = {
                            Billing.sSettings.bottomBarStyle set it
                            Billing.saveData()
                        }
                    ) {
                        Scaffold(
                            modifier = Modifier.height(100.dp),
                            bottomBar = {
                                BottomNavigation(
                                    pagerState = rememberPagerState(
                                        pageCount = 4,
                                        initialPage = Billing.sSettings.navLastTimeSelected.value
                                    )
                                )
                            },
                            floatingActionButton = {
                                FloatingActionButton(onClick = { /*TODO*/ }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_plus),
                                        contentDescription = "添加"
                                    )
                                }
                            },
                            isFloatingActionButtonDocked = true,
                            floatingActionButtonPosition =
                            if (Billing.sSettings.bottomBarStyle.getState().value) FabPosition.Center
                            else FabPosition.End,
                        ) {}
                    }
                },
                {
                    SettingItemView(
                        title = "主题",
                        selectlist = mutableListOf(
                            "黑色","紫色","红色"
                        ),
                        default = Billing.sSettings.theme.getState().value,
                        onClickeditem = {
                            Billing.sSettings.theme set it
                            Billing.saveData()
                        }
                    )
                }
            ))
        }
    }
}