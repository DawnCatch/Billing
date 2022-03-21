package com.example.billing.utils.datas

import com.example.billing.utils.RememberState
import com.google.gson.annotations.Expose

data class UserState(
    @Expose val name: RememberState<String> = RememberState("用户"),
    @Expose val details: MutableList<DetailState> = mutableListOf()
)