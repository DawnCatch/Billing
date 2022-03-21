package com.example.billing.fragments

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.billing.utils.datas.Screening
import com.example.sport.ui.view.TimeSelectView

@Composable
fun ScreenFragment(screening: Screening) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = "开始日期")
        Spacer(modifier = Modifier.width(30.dp))
        TimeSelectView(start = 1,
            end = 3000,
            default = screening.startTime.year.value,
            modifier = Modifier
                .padding(start = 10.dp)
                .padding(vertical = 25.dp),
            selected = {
                screening.startTime.year set it
            })
        Spacer(modifier = Modifier.width(50.dp))
        TimeSelectView(start = 1,
            end = 12,
            default = screening.startTime.month.value,
            modifier = Modifier
                .padding(end = 10.dp)
                .padding(vertical = 25.dp),
            selected = {
                screening.startTime.month set it
            })
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = "结束日期")
        Spacer(modifier = Modifier.width(30.dp))
        TimeSelectView(start = 1,
            end = 3000,
            default = screening.endTime.year.value,
            modifier = Modifier
                .padding(start = 10.dp)
                .padding(vertical = 25.dp),
            selected = {
                screening.endTime.year set it
            })
        Spacer(modifier = Modifier.width(50.dp))
        TimeSelectView(start = 1,
            end = 12,
            default = screening.endTime.month.value,
            modifier = Modifier
                .padding(end = 10.dp)
                .padding(vertical = 25.dp),
            selected = {
                screening.endTime.month set it
            })
    }
}