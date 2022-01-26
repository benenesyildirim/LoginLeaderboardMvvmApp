package com.mvvm.loginleaderboard.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.mvvm.loginleaderboard.R
import com.mvvm.loginleaderboard.data.repository.LeaderboardRepositoryImpl
import com.mvvm.loginleaderboard.domain.repository.LeaderboardRepository
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}