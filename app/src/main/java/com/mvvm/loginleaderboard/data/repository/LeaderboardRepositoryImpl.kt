package com.mvvm.loginleaderboard.data.repository

import com.mvvm.loginleaderboard.data.remote.LeaderboardPaprikaApi
import com.mvvm.loginleaderboard.data.remote.dto.User
import com.mvvm.loginleaderboard.domain.repository.LeaderboardRepository
import retrofit2.Response
import javax.inject.Inject

class LeaderboardRepositoryImpl @Inject constructor(
    private val api: LeaderboardPaprikaApi
) : LeaderboardRepository {
    override suspend fun checkNickName(nickName: String): Response<String> = api.checkNickName(nickName)

    override suspend fun getLeaderboard(token: String): Response<MutableList<User>> = api.getLeaderboard(token)

    override suspend fun getAlUsers(token: String): Response<MutableList<User>> = api.getAllUsers(token)

    override suspend fun updateUser(user: User): Response<String> = api.updateUser(user.token!!, user)
}