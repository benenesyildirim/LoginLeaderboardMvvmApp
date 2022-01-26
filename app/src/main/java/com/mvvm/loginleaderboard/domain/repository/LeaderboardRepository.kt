package com.mvvm.loginleaderboard.domain.repository

import com.mvvm.loginleaderboard.data.remote.dto.User
import retrofit2.Response

interface LeaderboardRepository {
    //TODO Query body falan kaldırldı.
    suspend fun checkNickName(nickName: String): Response<String>

    suspend fun getLeaderboard(token: String): Response<MutableList<User>>

    suspend fun getAlUsers(token: String): Response<MutableList<User>>

    suspend fun updateUser(user: User): Response<String>
}