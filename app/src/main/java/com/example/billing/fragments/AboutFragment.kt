package com.example.billing.fragments

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.billing.activitys.TemplateActivity
import com.example.billing.ui.view.components.DictText
import com.example.sport.ui.view.MCard

@Composable
fun AboutFragment(templateActivity: TemplateActivity) {
    MCard {
        DictText(key = "author", value = "Marshal zhou03", modifier = Modifier.clickable {
            val uri = Uri.parse("https://github.com/shadow-mark/Billing")
            val intent = Intent()
            intent.setAction("android.intent.action.VIEW")
            intent.setData(uri)
            templateActivity.startActivity(intent)
        })
    }
}