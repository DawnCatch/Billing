package com.example.billing.fragments

import androidx.compose.runtime.Composable
import com.example.billing.activitys.MainActivity
import com.example.billing.utils.datas.Type


@Composable
fun BorrowingFragment(mainActivity: MainActivity) {
    HostBaseFragment(mainActivity = mainActivity,type = Type.Borrowers)
}

@Composable
fun LendingFragment(mainActivity: MainActivity) {
    HostBaseFragment(mainActivity = mainActivity,type = Type.Lenders)
}