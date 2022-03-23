package com.example.billing.activitys

import android.os.Bundle
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.billing.fragments.*
import com.example.sport.ui.view.TopAppBar
import com.example.billing.utils.RememberState
import com.example.billing.utils.datas.Screening

const val EXTRA_FRAGMENT = "dataType"
const val STATE_BAR = "StatusBar"
const val RE_INIT = "reInit"

class TemplateActivity : BaseActivity() {

    val dataType: RememberState<String> = RememberState("null")

    @Composable
    override fun StatusBar() {
        TopAppBar(
            onLeft = {
                IconButton(
                    onClick = {
                        Billing.saveData()
                        finish()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp, 32.dp),
                        tint = Color.White
                    )
                }
            }
        ) {
            Text(
                text = dataType.getState().value
            )
        }
    }

    override fun initBundle(bundle: Bundle) {
        super.initBundle(bundle)
        dataType set bundle.getString(EXTRA_FRAGMENT).toString()
        StatusBar set bundle.getBoolean(STATE_BAR, true)
        reInit set bundle.getBoolean(RE_INIT,false)
    }

    @Composable
    override fun Content() {
        when (dataType.getState().value) {
            "设置" -> {
                SettingFragment(this)
            }
            "底部栏样式" -> {
                SettingStyle()
            }
            "关于" -> {
                AboutFragment(templateActivity = this)
            }
            "筛选" -> {
                ScreenFragment()
            }
            "添加明细" -> {
                AddDetailFragment(templateActivity = this)
            }
            "类别设置" -> {
                CreateDetailTypeFragment(templateActivity = this)
            }
            "渠道设置" -> {
                CreateMovDirectionFragment(templateActivity = this,false)
            }
            "对象设置" -> {
                CreateMovDirectionFragment(templateActivity = this,true)
            }
        }
    }
}