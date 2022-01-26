package com.mvvm.loginleaderboard.domain.use_case.check_nickname

import com.mvvm.loginleaderboard.domain.repository.LeaderboardRepository
import javax.inject.Inject

class CheckNicknameUseCase @Inject constructor(
    private val repository: LeaderboardRepository
) {
    suspend fun checkNickname(nickName: String) = repository.checkNickName(nickName)
}