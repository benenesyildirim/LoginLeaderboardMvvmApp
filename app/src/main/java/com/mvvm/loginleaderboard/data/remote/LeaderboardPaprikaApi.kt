package com.mvvm.loginleaderboard.data.remote

import com.mvvm.loginleaderboard.common.Constants.AUTHORIZATION
import com.mvvm.loginleaderboard.common.Constants.NICKNAME
import com.mvvm.loginleaderboard.data.remote.dto.User
import retrofit2.Response
import retrofit2.http.*

interface LeaderboardPaprikaApi {

    @GET("/check-nickname") //UniqueId ve role verilebilir durumda.
    suspend fun checkNickName(@Query(NICKNAME) nickName: String): Response<String>

    @GET("/get-leaderboard")
    suspend fun getLeaderboard(@Header(AUTHORIZATION) token: String): Response<MutableList<User>>

    @GET("/get-all")
    suspend fun getAllUsers(@Header(AUTHORIZATION) token: String): Response<MutableList<User>>

    @PUT("/update-user")
    suspend fun updateUser(@Header(AUTHORIZATION) token: String, @Body user: User): Response<String>
}