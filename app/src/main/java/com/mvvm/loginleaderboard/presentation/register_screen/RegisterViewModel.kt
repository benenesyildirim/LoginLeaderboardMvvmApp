package com.mvvm.loginleaderboard.presentation.register_screen

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.mvvm.loginleaderboard.common.Constants.NICKNAME
import com.mvvm.loginleaderboard.common.Constants.POINT
import com.mvvm.loginleaderboard.common.Constants.TOKEN
import com.mvvm.loginleaderboard.common.Resource
import com.mvvm.loginleaderboard.data.remote.dto.User
import com.mvvm.loginleaderboard.domain.use_case.check_nickname.CheckNicknameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val checkNicknameUseCase: CheckNicknameUseCase,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _registerLiveData = MutableLiveData<Resource<String>>()
    val registerLiveData: LiveData<Resource<String>> get() = _registerLiveData

    fun registerUser(nickName: String) = viewModelScope.launch {
        try {
            _registerLiveData.postValue(Resource.Loading())

            checkNicknameUseCase.checkNickname(nickName).let {
                if (it.isSuccessful) {
                    _registerLiveData.postValue(Resource.Success(it.body()!!))
                    setLoggedUser(nickName, it)
                } else {
                    _registerLiveData.postValue(
                        Resource.Error(
                            it.errorBody()?.charStream()?.readText()
                                ?: "Nickname is already using please try other username"
                        )
                    )
                }
            }
        } catch (e: HttpException) {
            _registerLiveData.postValue(
                Resource.Error(
                    e.localizedMessage ?: "An unexpected error occurred"
                )
            )
        } catch (e: IOException) {
            _registerLiveData.postValue(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }

    private fun setLoggedUser(nickName: String, it: Response<String>) {
        val editor = sharedPreferences.edit()
        editor.putString(NICKNAME, nickName)
        editor.putInt(POINT, 0)
        editor.putString(TOKEN, it.body())
        editor.apply()
    }
}