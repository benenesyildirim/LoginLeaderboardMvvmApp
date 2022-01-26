package com.mvvm.loginleaderboard.di

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.GsonBuilder
import com.mvvm.loginleaderboard.common.Constants.BASE_URL
import com.mvvm.loginleaderboard.common.Constants.LOGGED_USER
import com.mvvm.loginleaderboard.data.remote.LeaderboardPaprikaApi
import com.mvvm.loginleaderboard.data.repository.LeaderboardRepositoryImpl
import com.mvvm.loginleaderboard.domain.repository.LeaderboardRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePaprikaApi(): LeaderboardPaprikaApi {
        val gson = GsonBuilder().setLenient().create()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(LeaderboardPaprikaApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRepository(api: LeaderboardPaprikaApi): LeaderboardRepository{
        return LeaderboardRepositoryImpl(api)
    }

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(LOGGED_USER, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideFirestore() = FirebaseFirestore.getInstance()
}