package com.rkt.myimbdmodernmovieapp.di

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.ConnectionPool
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    @RetrofitForApi
    fun providesApiClient(builder: Retrofit.Builder): Retrofit {
        return builder
            .baseUrl("https://raw.githubusercontent.com/erik-sytnyk/")
            .build()
    }

    @Singleton
    @Provides
    fun providesRetrofitBuilder(client: OkHttpClient): Retrofit.Builder {
        return Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)

    }

    @Singleton
    @Provides
    fun providesClient(
        @Named("log_interceptor") interceptor: Interceptor
    ): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
        clientBuilder.connectTimeout(30, TimeUnit.SECONDS)
        clientBuilder.readTimeout(30, TimeUnit.SECONDS)
        clientBuilder.writeTimeout(30, TimeUnit.SECONDS)

        try {
            clientBuilder.addInterceptor(interceptor)
            clientBuilder.connectionPool(ConnectionPool(0, 5, TimeUnit.MINUTES))
            clientBuilder.protocols(listOf(Protocol.HTTP_1_1)).build()
        } catch (e: Exception) {
            Log.e("NETWORK_CACHE", "HTTP_CODE providesClient: ${e.localizedMessage}")
        }
        clientBuilder.retryOnConnectionFailure(false)
        clientBuilder.callTimeout(30, TimeUnit.SECONDS)
        return clientBuilder.build()
    }

    @Singleton
    @Provides
    @Named("log_interceptor")
    fun providesInterceptor(): Interceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor { networkLog ->
            Log.d(
                "NETWORK_CACHE",
                "log: $networkLog"
            )
        }
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        return httpLoggingInterceptor
    }

}