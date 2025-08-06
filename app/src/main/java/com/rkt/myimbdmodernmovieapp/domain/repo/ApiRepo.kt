package com.rkt.myimbdmodernmovieapp.domain.repo

import com.rkt.myimbdmodernmovieapp.data.dto.MoviesResponse
import retrofit2.Response

interface ApiRepo {
    suspend fun getMovieList(): Response<MoviesResponse>
}