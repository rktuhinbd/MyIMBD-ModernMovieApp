package com.rkt.myimbdmodernmovieapp.di

import android.content.Context
import androidx.room.Room
import com.rkt.myimbdmodernmovieapp.data.local.dao.MovieDao
import com.rkt.myimbdmodernmovieapp.data.local.db.AppDatabase
import com.rkt.myimbdmodernmovieapp.data.repository.RepoImpl
import com.rkt.myimbdmodernmovieapp.data.service.ApiService
import com.rkt.myimbdmodernmovieapp.domain.repo.ApiRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "movies.db").build()

    @Provides
    fun provideDao(db: AppDatabase): MovieDao = db.movieDao()


    @Provides
    fun providesApiRepo(apiService: ApiService, dao: MovieDao): ApiRepo {
        return RepoImpl(apiService = apiService, dao = dao)
    }

    @Provides
    fun providesApiService(@RetrofitForApi retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}