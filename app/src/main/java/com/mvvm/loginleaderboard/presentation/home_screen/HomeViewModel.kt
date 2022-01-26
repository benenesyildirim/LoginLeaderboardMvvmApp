package com.mvvm.loginleaderboard.presentation.home_screen

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.mvvm.loginleaderboard.common.Constants
import com.mvvm.loginleaderboard.common.Constants.ADMIN_TOKEN
import com.mvvm.loginleaderboard.common.Constants.NICKNAME
import com.mvvm.loginleaderboard.common.Constants.POINT
import com.mvvm.loginleaderboard.common.Constants.TOKEN
import com.mvvm.loginleaderboard.common.Constants.USERS_COLLECTION
import com.mvvm.loginleaderboard.common.Resource
import com.mvvm.loginleaderboard.data.remote.dto.User
import com.mvvm.loginleaderboard.domain.use_case.update_user.UpdateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel class HomeViewModel @Inject constructor(
    private val updateUserUseCase: UpdateUserUseCase, private val sharedPreferences: SharedPreferences, private val firestore: FirebaseFirestore
) : ViewModel() {
    private val _updateUserLiveData = MutableLiveData<Resource<String>>()
    val updateUserLiveData: LiveData<Resource<String>> get() = _updateUserLiveData

    fun getUserFirestore(): Task<DocumentSnapshot>? {
        val nickName = sharedPreferences.getString(NICKNAME, "")!!
        return if (nickName.isNotEmpty()) firestore.collection(USERS_COLLECTION).document(nickName).get()
        else null
    }


    fun updatePoint(point: Int) {
        val nickName = sharedPreferences.getString(NICKNAME, "")!!
        firestore.collection(USERS_COLLECTION).document(nickName).update(POINT, point)
    }

    private fun updateUserApi(user: User) = viewModelScope.launch {
        try {
            updateUserUseCase.updateUser(user).let {
                if (it.isSuccessful) _updateUserLiveData.postValue(Resource.Success(it.body()!!))
                else _updateUserLiveData.postValue(Resource.Error(it.errorBody()?.charStream()?.readText()
                    ?: "Something went wrong"))
            }
        } catch (e: HttpException) {
            _updateUserLiveData.postValue(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            _updateUserLiveData.postValue(Resource.Error("Couldn't reach server. Check your internet connection."))
        }
    }

    //TODO bunu başka bir şekilde yap.
    fun getAdmin() {
        val editor = sharedPreferences.edit()
        editor.putString(NICKNAME, "admin")
        editor.putInt(POINT, 0)
        editor.putString(TOKEN, ADMIN_TOKEN)
        editor.apply()
    }

    fun getLoggedUserSp(): User? {
        val nickName = sharedPreferences.getString(NICKNAME, null)
        val token = sharedPreferences.getString(TOKEN, null)
        val point = sharedPreferences.getInt(POINT, 0)

        return if (nickName.isNullOrEmpty() || token.isNullOrEmpty()) null
        else User(nickName, point, token)
    }
}