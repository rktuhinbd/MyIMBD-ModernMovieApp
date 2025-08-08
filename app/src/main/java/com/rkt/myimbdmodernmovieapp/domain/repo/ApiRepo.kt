package com.rkt.myimbdmodernmovieapp.domain.repo

import com.rkt.myimbdmodernmovieapp.data.remote.MoviesResponse
import com.rkt.myimbdmodernmovieapp.model.MoviesEntity
import retrofit2.Response

interface ApiRepo {
    suspend fun getMovieList(): Response<MoviesResponse>
    suspend fun getMovies(): List<MoviesEntity>
    suspend fun toggleWishlist(id: Int, isFavorite: Boolean)
    suspend fun getWishlist(): List<MoviesEntity>
}