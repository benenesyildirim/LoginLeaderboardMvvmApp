package com.mvvm.loginleaderboard.domain.use_case.get_all

import com.mvvm.loginleaderboard.domain.repository.LeaderboardRepository
import javax.inject.Inject

class GetAllUseCase @Inject constructor(
    private val repository: LeaderboardRepository
) {
    suspend fun getAll(token: String) = repository.getAlUsers(token)
}