package com.mvvm.loginleaderboard.data.remote.dto

data class User(
    val nickName: String,
    var point: Int,
    val token: String? = null,
    var uniqueId: String? = null
)
