package com.mvvm.loginleaderboard.presentation.leaderboard_screen

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.mvvm.loginleaderboard.common.Constants.ADMIN_TOKEN
import com.mvvm.loginleaderboard.common.Constants.TOKEN
import com.mvvm.loginleaderboard.common.Constants.USERS_COLLECTION
import com.mvvm.loginleaderboard.common.Resource
import com.mvvm.loginleaderboard.data.remote.dto.User
import com.mvvm.loginleaderboard.domain.use_case.get_all.GetAllUseCase
import com.mvvm.loginleaderboard.domain.use_case.get_leaderboard.GetLeaderboardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val leaderboardUseCase: GetLeaderboardUseCase,
    private val getAllUseCase: GetAllUseCase,
    sharedPreferences: SharedPreferences
) : ViewModel() {
    private var token: String = "Bearer " + sharedPreferences.getString(TOKEN, "")

    private val _leaderboardLiveData = MutableLiveData<Resource<MutableList<User>>>()
    val leaderboardLiveData: LiveData<Resource<MutableList<User>>> get() = _leaderboardLiveData

    fun getLeaderboard() = viewModelScope.launch {
        try {
            leaderboardUseCase.getLeaderboard(token).let {
                _leaderboardLiveData.postValue(Resource.Loading())

                if (it.isSuccessful)
                    _leaderboardLiveData.postValue(Resource.Success(it.body()!!))
                else
                    _leaderboardLiveData.postValue(
                        Resource.Error(
                            it.errorBody()?.charStream()?.readText() ?: "Please check token"
                        )
                    )
            }
        } catch (e: HttpException) {
            _leaderboardLiveData.postValue(
                Resource.Error(
                    e.localizedMessage ?: "An unexpected error occurred"
                )
            )
        } catch (e: IOException) {
            _leaderboardLiveData.postValue(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }

    fun getAll() = viewModelScope.launch {
        try {
            getAllUseCase.getAll(token).let {
                _leaderboardLiveData.postValue(Resource.Loading())

                if (it.isSuccessful)
                    _leaderboardLiveData.postValue(Resource.Success(it.body()!!))
                else
                    _leaderboardLiveData.postValue(
                        Resource.Error("Permission denied.")
                    )
            }
        } catch (e: HttpException) {
            _leaderboardLiveData.postValue(
                Resource.Error(
                    e.localizedMessage ?: "An unexpected error occurred"
                )
            )
        } catch (e: IOException) {
            _leaderboardLiveData.postValue(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }

    fun setAsAdmin() {
        token = "Bearer $ADMIN_TOKEN"
    }
}