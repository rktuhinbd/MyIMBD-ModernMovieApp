package com.rkt.myimbdmodernmovieapp.domain.repo

import com.rkt.myimbdmodernmovieapp.data.dto.MoviesResponse
import com.rkt.myimbdmodernmovieapp.model.MoviesEntity
import retrofit2.Response

interface ApiRepo {
    suspend fun getMovieList(): Response<MoviesResponse>
    suspend fun getMovies(): List<MoviesEntity>
    suspend fun syncRemoteData()
    suspend fun toggleWishlist(id: Int, isFavorite: Boolean)
}