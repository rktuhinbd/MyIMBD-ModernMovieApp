package com.rkt.myimbdmodernmovieapp.di

import com.rkt.myimbdmodernmovieapp.data.service.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    fun providesAiService(@RetrofitForApi retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}