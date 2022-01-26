package com.mvvm.loginleaderboard.domain.use_case.update_user

import com.mvvm.loginleaderboard.data.remote.dto.User
import com.mvvm.loginleaderboard.domain.repository.LeaderboardRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val repository: LeaderboardRepository
) {
    suspend fun updateUser(user: User) = repository.updateUser(user)
}