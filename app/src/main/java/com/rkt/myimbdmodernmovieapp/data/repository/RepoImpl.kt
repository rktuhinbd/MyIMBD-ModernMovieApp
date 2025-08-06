package com.rkt.myimbdmodernmovieapp.data.repository

import com.rkt.myimbdmodernmovieapp.data.dto.MoviesResponse
import com.rkt.myimbdmodernmovieapp.data.service.ApiService
import com.rkt.myimbdmodernmovieapp.domain.repo.ApiRepo
import retrofit2.Response
import javax.inject.Inject

class RepoImpl @Inject constructor(
    private val apiService: ApiService
) : ApiRepo{

    override suspend fun getMovieList(): Response<MoviesResponse> {
        return apiService.getMovieList()
    }
}