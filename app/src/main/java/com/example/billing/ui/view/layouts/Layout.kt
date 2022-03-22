package com.example.sport.ui.view

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.billing.utils.RememberState
import kotlinx.coroutines.launch

@Composable
fun MCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 6.dp, end = 6.dp, top = 5.dp, bottom = 3.dp),
        elevation = 3.dp, // 设置阴影
        content = content
    )
}

@Composable
fun MDialog(
    visible: RememberState<Boolean>,
    content: @Composable ColumnScope.() -> Unit
) {
    if (visible.getState().value) {
        Dialog(onDismissRequest = {
            visible.set(false)
        }) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .background(Color.White, shape = RoundedCornerShape(5.dp)),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = content
            )
        }
    }
}

@Composable
fun MDialog(
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .background(Color.White, shape = RoundedCornerShape(5.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            content = content
        )
    }
}


@Composable
fun SettingItemColum(
    key: String,
    value: MutableList<@Composable () -> Unit>
) {
    MCard(
        modifier = Modifier.padding(bottom = 7.dp)
    ) {
        Column(
//            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Gray.copy(0.3f))
            ) {
                Text(
                    text = key,
                    color = Color(104, 107, 222),
                    modifier = Modifier.padding(start = 10.dp, bottom = 5.dp, top = 3.dp)
                )
            }
            for (i in 0 until value.size) {
                value.get(i)()
                if (i != value.size - 1) {
                    Spacer(
                        modifier = Modifier
                            .padding(start = 5.dp, end = 5.dp)
                            .fillMaxWidth()
                            .height(0.5.dp)
                            .background(Color.Gray)
                    )
                }
            }
        }
    }
}

@Composable
fun TopAppBar(
    height: Dp = 50.dp,
    onLeft: (@Composable () -> Unit)? = null,
    onRight: (@Composable () -> Unit)? = null,
    title: (@Composable () -> Unit)? = null,
) {
    val modifier = Modifier.size(height)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = height)
            .background(Color(98, 0, 238))
    ) {
        Box(contentAlignment = Alignment.CenterStart, modifier = modifier) {
            onLeft?.let {
                it()
            }
        }
        Row(
            Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
//            ProvideTextStyle(value = MaterialTheme.typography.h6) {
            ProvideTextStyle(
                value = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp,
                    letterSpacing = 0.15.sp,
                    color = Color.White
                )
            ) {
                title?.let {
                    it()
                }
            }
        }
        Box(contentAlignment = Alignment.CenterEnd, modifier = modifier) {
            onRight?.let {
                it()
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun TimeSelectView(
    start: Int,
    end: Int,
    other: String = "",
    default: Int,
    modifier: Modifier = Modifier,
    selected: (Int) -> Unit
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    var offsetInit by remember {
        mutableStateOf(true)
    }

    if (!listState.isScrollInProgress) {
        scope.launch {
            listState.animateScrollToItem(listState.firstVisibleItemIndex)
        }
        selected(listState.firstVisibleItemIndex + start)
    }

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
    ) {
        Spacer(
            modifier = Modifier
                .padding(top = 36.dp)
                .width(60.dp)
                .height(1.dp)
                .background(Color.Black)
        )
        Spacer(
            modifier = Modifier
                .padding(top = 72.dp)
                .width(60.dp)
                .height(1.dp)
                .background(Color.Black)
        )
        LazyColumn(
            state = listState,
            modifier = Modifier
                .height(108.dp)
                .width(50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Spacer(modifier = Modifier.height(36.dp))
            }
            items(if (other != "") end - start + 2 else end - start + 1) { index ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(36.dp)
                        .clickable {
                            scope.launch { listState.scrollToItem(index) }
                        }
                ) {
                    Text(
                        text = if (index == end) other else "${index + start}",
                        fontSize = if (index == listState.firstVisibleItemIndex + start - 1) 20.sp else 17.sp,
                        color = if (index == listState.firstVisibleItemIndex + start - 1) Color.Black else Color.Gray
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(36.dp))
            }
        }
    }

    if (offsetInit) {
        scope.launch { listState.scrollToItem(default - start) }
        offsetInit = false
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun <T> TimeSelectView(
    array: List<T>,
    default: Int,
    selected: (Int, T) -> Unit
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    var offsetInit by remember {
        mutableStateOf(true)
    }

    if (!listState.isScrollInProgress) {
        scope.launch {
            listState.animateScrollToItem(listState.firstVisibleItemIndex)
        }
        selected(listState.firstVisibleItemIndex, array[listState.firstVisibleItemIndex])
    }

    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        Spacer(
            modifier = Modifier
                .padding(top = 36.dp)
                .width(60.dp)
                .height(1.dp)
                .background(Color.Black)
        )
        Spacer(
            modifier = Modifier
                .padding(top = 72.dp)
                .width(60.dp)
                .height(1.dp)
                .background(Color.Black)
        )
        LazyColumn(
            state = listState,
            modifier = Modifier
                .height(108.dp)
                .width(50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Spacer(modifier = Modifier.height(36.dp))
            }
            itemsIndexed(array) { index, it ->
                Box(contentAlignment = Alignment.Center, modifier = Modifier.height(36.dp)) {
                    Text(
                        text = "$it",
                        fontSize = if (index == listState.firstVisibleItemIndex) 20.sp else 17.sp,
                        color = if (index == listState.firstVisibleItemIndex) Color.Black else Color.Gray
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(36.dp))
            }
        }
    }

    if (offsetInit) {
        scope.launch { listState.scrollToItem(default) }
        offsetInit = false
    }
}

fun getOffset(float: Float): Float {
    var a = float * 0.5f
    if (a > 150f) {
        a = 0f
    }
    return a
}

