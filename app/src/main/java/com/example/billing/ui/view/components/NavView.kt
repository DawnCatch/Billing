package com.example.billing.ui.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.billing.activitys.Billing
import com.example.billing.utils.RememberState
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.launch

@Composable
fun RowMenuItem(
    text:String,
    icon:Int = -1,
    id:String,
    scaffoldState: ScaffoldState,
    onclick: (String) -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier
            .clickable {
                scope.launch {
                    scaffoldState.drawerState.close()
                }
                onclick(id)
            }
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != -1) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = text,
                modifier = Modifier
                    .padding(start = 15.dp, end = 40.dp)
                    .size(24.dp, 24.dp)
            )
        }else {
            Spacer(modifier = Modifier.size(36.dp, 16.dp))
        }
        Text(text = text)
    }
}

@OptIn(ExperimentalAnimationApi::class, com.google.accompanist.pager.ExperimentalPagerApi::class)
@Composable
fun ColumMenuItem(
    text:String,
    icon:Int = -1,
    id:Int,
    highLight:Color = MaterialTheme.colors.primary,
    lowLight:Color = Color(156, 156, 156),
//    navController: RememberState<String>? = null,
    navController: PagerState
) {
    val coroutineScope = rememberCoroutineScope()

    val iconSize = RememberState(24.dp)
    val iconColor = RememberState(lowLight)
    val isText = RememberState(false)
    if (navController.currentPage == id) {
        iconColor set highLight
        isText set true
    } else {
        iconColor set lowLight
        isText set false
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
//            .background(if (navController.getState().value == id) highLight else lowLight)
            .clickable(
                indication = null,
                interactionSource = MutableInteractionSource()
            ) {
                coroutineScope.launch { navController.animateScrollToPage(id) }
                Billing.sSettings.navLastTimeSelected set id
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (icon != -1) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = text,
                modifier = Modifier
//                    .padding(bottom = 5.dp)
                    .size(iconSize.getState().value),
                tint = iconColor.getState().value
            )
        }else {
            Spacer(modifier = Modifier.size(36.dp, 16.dp))
        }
        AnimatedVisibility(
            visible = isText.getState().value,
        ) {
            Text(
                text = text,
                fontSize = 12.sp,
                color = iconColor.getState().value,
                modifier = Modifier.padding(top = 3.dp)
            )
        }
    }
}

@Composable
fun MenuGroup(
    text: String? = null,
    menuitem: @Composable ColumnScope.() -> Unit,
) {
    if (text != null) {
        Text(
            modifier = Modifier.padding(start = 10.dp, top = 5.dp),
            text = text,
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
    Column(
        content = menuitem
    )
}

@Composable
fun MenuColum(
    menugroud: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        content = menugroud
    )
}

@Composable
fun MenuRow(
    menugroud: @Composable RowScope.() -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10,10,0,0),
//        elevation = 10.dp
    ) {
        BottomAppBar(
            cutoutShape = MaterialTheme.shapes.small.copy(
                CornerSize(percent = 50)
            ),
            backgroundColor = MaterialTheme.colors.primaryVariant,
            modifier = Modifier
//                .border(BorderStroke(0.1.dp, Color.Black))
                .fillMaxWidth()
                .height(60.dp),
            content = menugroud
        )
    }
}

@Composable
fun HorizontallyLine() {
    Spacer(
        modifier = Modifier
            .padding(start = 5.dp, end = 5.dp)
            .fillMaxWidth()
            .height(0.5.dp)
            .background(Color.Gray)
    )
}

@Composable
fun VerticallyLine() {
    Spacer(
        modifier = Modifier
            .padding(top = 5.dp, bottom = 5.dp)
            .fillMaxHeight()
            .width(0.5.dp)
            .background(Color.Gray)
    )
}