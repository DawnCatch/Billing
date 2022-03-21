package com.example.billing.fragments

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.height
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.billing.R
import com.example.billing.activitys.Billing
import com.example.billing.activitys.BottomNavigation
import com.example.sport.ui.view.MCard
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalPagerApi::class)
@Composable
fun SettingStyle() {
    MCard {
        Scaffold(
            modifier = Modifier.height(100.dp),
            bottomBar = {
                val pagerState = rememberPagerState(pageCount = 4)
                val coroutineScope = rememberCoroutineScope()
                coroutineScope.launch { pagerState.animateScrollToPage(Billing.sSettings.navLastTimeSelected.getState().value) }
                BottomNavigation(pagerState = pagerState)
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
            floatingActionButtonPosition = if (Billing.sSettings.bottomBarStyle.getState().value) FabPosition.Center else FabPosition.End,
        ) {}
    }
}