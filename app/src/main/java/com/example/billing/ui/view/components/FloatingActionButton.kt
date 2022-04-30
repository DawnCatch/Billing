package com.example.sport.ui.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.billing.activitys.Billing

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MultiFloatingActionButton(
    modifier: Modifier = Modifier,
    srcIcon: ImageVector,
    srcIconColor: Color = Color.White,
    fabBackgroundColor: Color = Color.Unspecified,
    items: List<MultiFabItem>,
    onFabItemClicked: (index: Int, item: MultiFabItem, state: MutableState<MultiFabState>) -> Unit
) {
    var hostState by remember {
        mutableStateOf(false)
    }
    //当前菜单默认状态处于：Collapsed
    val currentState = remember { mutableStateOf(MultiFabState.Collapsed) }
    //创建过渡对象，用于管理多个动画值，并且根据状态变化运行这些值
    val transition = updateTransition(targetState = currentState, label = "")
    //用于+号按钮的旋转动画
    val rotateAnim: Float by transition.animateFloat(
        transitionSpec = {
            if (targetState.value == MultiFabState.Expanded) {
                spring(stiffness = Spring.StiffnessLow)
            } else {
                spring(stiffness = Spring.StiffnessMedium)
            }
        }, label = ""
    ) { state ->
        //根据state来设置最终的角度
        if (state.value == MultiFabState.Collapsed) 0F else -45F
    }
    //透明度动画
    val alphaAnim: Float by transition.animateFloat(transitionSpec = {
        tween(durationMillis = 200)
    }, label = "") { state ->
        if (state.value == MultiFabState.Expanded) 1F else 0F
    }
    //记录每个Item的收缩动画的Transition
    val shrinkListAnim: MutableList<Float> = mutableListOf()
    items.forEachIndexed { index, _ ->
        //循环生成Transition
        val shrinkAnim by transition.animateFloat(targetValueByState = { state ->
            when (state.value) {
                MultiFabState.Collapsed -> 5F
                //根据位置，递增每个item的位置高度
                MultiFabState.Expanded -> (index + 1) * 60F + if (index == 0) 5F else 0F
            }
        }, label = "", transitionSpec = {
            if (targetState.value == MultiFabState.Expanded) {
                //dampingRatio属性删除等于默认1F，没有回弹效果
                spring(stiffness = Spring.StiffnessLow, dampingRatio = 0.58F)
            } else {
                spring(stiffness = Spring.StiffnessMedium)
            }
        })
        //添加到收缩列表中
        shrinkListAnim.add(index, shrinkAnim)
    }
    Box(modifier = modifier, contentAlignment = Alignment.BottomEnd) {
        //创建多个Item,Fab按钮
        items.forEachIndexed { index, item ->
            Row(
//                verticalAlignment = Alignment.CenterVertically,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .padding(
                        bottom = shrinkListAnim[index].dp,
                        top = 5.dp,
                        end = 5.dp
                    )
                    .alpha(animateFloatAsState(alphaAnim).value)
            ) {
                AnimatedVisibility(
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .border(width = 1.dp, Color.Black, shape = RoundedCornerShape(15))
                        .clip(MaterialTheme.shapes.medium)
                        .alpha(animateFloatAsState(alphaAnim).value)
                        .background(color = item.labelBackgroundColor)
                        .padding(start = 6.dp, end = 6.dp, top = 4.dp, bottom = 4.dp),
                    visible = (item.showlabel.value && hostState),
                ) {
                    Column(Modifier.width((Billing.screen.width - 100).dp)) {
                        item.label()
                    }
                }
                FloatingActionButton(
                    backgroundColor = if (item.fabBackgroundColor == Color.Unspecified) MaterialTheme.colors.primary else item.fabBackgroundColor,
                    modifier = Modifier.size(46.dp),
                    onClick = {
                        hostState = true
                        //传入[item][currentState],自主开合子级按钮
                        onFabItemClicked(index, item, currentState)
                    },
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 2.dp,
                        pressedElevation = 4.dp
                    )
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = item.icon,
                        tint = item.srcIconColor,
                        contentDescription = item.text
                    )
                }
            }
        }
        //"+"号，切换按钮
        FloatingActionButton(
            backgroundColor = if (fabBackgroundColor == Color.Unspecified) MaterialTheme.colors.primary else fabBackgroundColor,
            onClick = {
                //更新状态执行：收缩动画
                currentState.value =
                    if (currentState.value == MultiFabState.Collapsed) MultiFabState.Expanded else MultiFabState.Collapsed
                hostState = false
                items.forEachIndexed { index, item ->
                    item.showlabel.value = false
                }
            }) {
            Icon(
                imageVector = srcIcon,
                modifier = Modifier.rotate(rotateAnim),
                tint = srcIconColor,
                contentDescription = null
            )
        }
    }
}

class MultiFabItem(
    val icon: ImageVector,
    val text: String = "",
    var showlabel: MutableState<Boolean> = mutableStateOf(value = true),
    val srcIconColor: Color = Color.White,
    val labelTextColor: Color = Color.White,
    val labelBackgroundColor: Color = Color.White.copy(alpha = 0.8F),
    val fabBackgroundColor: Color = Color.Unspecified,
    val label: @Composable () -> Unit = {},
)

enum class MultiFabState {
    Collapsed,
    Expanded
}