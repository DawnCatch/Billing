package com.example.billing.activitys

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import com.example.billing.ui.theme.BillingTheme
import com.example.billing.utils.RememberState
import java.lang.Exception

abstract class BaseActivity : ComponentActivity() {
    var mContext: Context? = null
    var bundle: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            mContext = this
            if (intent != null) {
                bundle = intent.extras
                if (this.bundle != null) {
                    initBundle(bundle!!)
                }
            }
            other()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        setContent {
            BillingTheme {
                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    Column {
                        if (StatusBar.getState().value) {
                            StatusBar()
                        }
                        Content()
                    }
                }
            }
        }
    }

    open fun initBundle(bundle: Bundle) {}

    open fun other() {}

    @Composable
    open fun StatusBar() {
        TopAppBar(
            title = { Text(text = "hello") },
        )
    }

    //    open fun hideStatusBar() = true
    var StatusBar: RememberState<Boolean> = RememberState(true)
    var reInit: RememberState<Boolean> = RememberState(false)

    @Composable
    abstract fun Content()

    override fun onPause() {
        super.onPause()
        Billing.saveData()
    }

    override fun onRestart() {
        super.onRestart()
        if (reInit.getState().value) {
            finish()
            startActivity(intent)
        }
    }
}