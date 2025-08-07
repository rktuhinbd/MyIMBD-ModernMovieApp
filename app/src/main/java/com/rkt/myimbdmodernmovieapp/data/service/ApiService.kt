package com.rkt.myimbdmodernmovieapp.data.service

import com.rkt.myimbdmodernmovieapp.data.remote.MoviesResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("movies-list/master/db.json")
    suspend fun getMovieList(): Response<MoviesResponse>

}