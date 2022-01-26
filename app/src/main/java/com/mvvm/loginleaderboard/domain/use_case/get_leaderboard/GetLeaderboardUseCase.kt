package com.mvvm.loginleaderboard.domain.use_case.get_leaderboard

import com.mvvm.loginleaderboard.domain.repository.LeaderboardRepository
import javax.inject.Inject

class GetLeaderboardUseCase @Inject constructor(
    private val repository: LeaderboardRepository
) {
    suspend fun getLeaderboard(token: String) = repository.getLeaderboard(token)
}