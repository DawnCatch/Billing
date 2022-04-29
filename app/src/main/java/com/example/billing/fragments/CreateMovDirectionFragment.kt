package com.example.billing.fragments

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.billing.R
import com.example.billing.activitys.Billing
import com.example.billing.activitys.TemplateActivity
import com.example.billing.ui.theme.itemBackgroud
import com.example.billing.utils.RememberState
import com.example.billing.utils.datas.MovDirection
import com.example.billing.utils.datas.MovDirectionState
import com.example.sport.ui.view.MDialog
import com.example.sport.ui.view.TopAppBar
import com.example.sport.ui.view.components.EditText
import com.example.sport.ui.view.components.EditTextIcon
import com.example.sport.ui.view.components.EditTextPromptBox
import com.example.sport.ui.view.components.EditTextSettingBox


class CreateMovDirectionFragmentModel : ViewModel() {
    @SuppressLint("StaticFieldLeak")
    var templateActivity: TemplateActivity? = null
    var movDirectionFormState: MovDirectionFormState? = null
    var type: Boolean? = null
}

@Stable
class MovDirectionFormState(
    val visible: RememberState<Boolean>,
    val movDirection: RememberState<MovDirectionState>,
)

@Composable
fun rememberMovDirectionFormState() = remember {
    MovDirectionFormState(
        visible = RememberState(false), movDirection = RememberState(
            MovDirectionState.All
        )
    )
}

@Composable
fun CreateMovDirectionFragment(
    templateActivity: TemplateActivity,
    type: Boolean,
    model: CreateMovDirectionFragmentModel = viewModel()
) {
    model.movDirectionFormState = rememberMovDirectionFormState()
    model.templateActivity = templateActivity
    model.type = type

    Box {
        Column {
            CreateMovDirectionTopTitleView()
            val list = Billing.db.getMovDirectionDao().queryWithType(type).asLiveData()
            val listState = list.observeAsState(arrayListOf())
            LazyColumn(
                contentPadding = if (model.movDirectionFormState!!.visible.getState().value) {
                    PaddingValues(bottom = 230.dp)
                } else {
                    PaddingValues(0.dp)
                },
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(listState.value) { it ->
                    if (it != MovDirectionState.All.getData() && it != MovDirectionState.NullT.getData() && it != MovDirectionState.NullF.getData()) {
                        MovDirectionHorizontalView(it) {
                            if (model.movDirectionFormState!!.visible.value && model.movDirectionFormState!!.movDirection.value == it.getState()) {
                                model.movDirectionFormState!!.run {
                                    visible set false
                                    movDirection set MovDirectionState.All
                                }
                            } else {
                                model.movDirectionFormState!!.run {
                                    visible set true
                                    movDirection set it.getState()
                                }
                            }
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier.align(Alignment.BottomCenter),
        ) {
            CreateMovDirectionAnimatedEditView()
        }
    }
}

@Composable
fun CreateMovDirectionTopTitleView(model: CreateMovDirectionFragmentModel = viewModel()) {
    TopAppBar(
        onLeft = {
            IconButton(
                onClick = {
                    Billing.saveData()
                    model.templateActivity!!.finish()
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
                    val movDirectionState = MovDirectionState.SelfSubscribe(model.type!!)
                    Thread {
                        movDirectionState.id = Billing.db.getMovDirectionDao()
                            .insert(model.movDirectionFormState!!.movDirection.getState().value.getData())
                            .toInt()
                    }.start()
                    model.movDirectionFormState!!.run {
                        movDirection set movDirectionState
                        visible set true
                    }
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
}

@Composable
fun MovDirectionHorizontalView(
    movDirection: MovDirection,
    model: CreateMovDirectionFragmentModel = viewModel(),
    onclick: (() -> Unit)? = null
) {
    val visible = RememberState(false)
    if (model.type!!) {
        MDialog(visible = visible) {
            val list = Billing.db.getDetailDao().queryWithDirectionFlow("%${movDirection.name}%")
                .asLiveData()
            DetailLazyColumView(
                modifier = Modifier.height(300.dp),
                list = list,
                context = model.templateActivity!!
            )
        }
    }else {
        MDialog(visible = visible) {
            val list = Billing.db.getDetailDao().queryWithChannelFlow("%${movDirection.name}%")
                .asLiveData()
            DetailLazyColumView(
                modifier = Modifier.height(300.dp),
                list = list,
                context = model.templateActivity!!
            )
        }
    }

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
                onclick?.let { it() }
            }
    ) {
        Text(
            text = movDirection.name,
            modifier = Modifier
                .padding(5.dp)
                .weight(1f)
        )
        IconButton(
            onClick = {
                Thread {
                    if (model.type!!) {
                        val list =
                            Billing.db.getDetailDao().queryWithDirection("%${movDirection.name}%")
                        if (list.isEmpty()) {
                            Billing.db.getMovDirectionDao().delete(movDirection)
                        } else {
                            visible set true
                        }
                    }else {
                        val list =
                            Billing.db.getDetailDao().queryWithChannel("%${movDirection.name}%")
                        if (list.isEmpty()) {
                            Billing.db.getMovDirectionDao().delete(movDirection)
                        } else {
                            visible set true
                        }
                    }
                }.start()
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CreateMovDirectionAnimatedEditView(model: CreateMovDirectionFragmentModel = viewModel()) {
    val detailFormState = model.movDirectionFormState!!
    AnimatedVisibility(
        modifier = Modifier.background(color = MaterialTheme.colors.itemBackgroud),
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
                                    .getMovDirectionDao()
                                    .insert(model.movDirectionFormState!!.movDirection.getState().value.getData())
                            }.start()
                        }
                        .focusable(),
                    editTextSttting = EditTextSettingBox.TextOption() {
                        Thread {
                            Billing.db.getMovDirectionDao()
                                .insert(model.movDirectionFormState!!.movDirection.getState().value.getData())
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
                                modifier = Modifier.size(24.dp, 24.dp)
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = {
                                Thread {
                                    Billing.db.getMovDirectionDao()
                                        .insert(model.movDirectionFormState!!.movDirection.getState().value.getData())
                                }.start()
                                focusManager.clearFocus()
                            }) {
                                Icon(imageVector = Icons.Default.Done, contentDescription = "确定")
                            }
                        }
                    ),
                    sidevalue = detailFormState.movDirection.getState().value.name
                )
            }
        }
    }
}