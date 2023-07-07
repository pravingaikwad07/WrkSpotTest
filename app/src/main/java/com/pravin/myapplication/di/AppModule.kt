package com.pravin.myapplication.di

import android.content.Context
import com.pravin.myapplication.netowrk.ApiService
import com.pravin.myapplication.data.repo.MainRepoImpl
import com.pravin.myapplication.data.repo.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideMainRepository(@ApplicationContext context: Context, apiService: ApiService): MainRepository {
        return MainRepoImpl(context, apiService)
    }

}