package com.rkt.myimbdmodernmovieapp.data.repository

import com.rkt.myimbdmodernmovieapp.data.remote.MoviesResponse
import com.rkt.myimbdmodernmovieapp.data.local.dao.MovieDao
import com.rkt.myimbdmodernmovieapp.data.service.ApiService
import com.rkt.myimbdmodernmovieapp.domain.repo.ApiRepo
import com.rkt.myimbdmodernmovieapp.model.MoviesEntity
import retrofit2.Response
import javax.inject.Inject

class RepoImpl @Inject constructor(
    private val apiService: ApiService,
    private val dao: MovieDao
) : ApiRepo {

    override suspend fun getMovieList(): Response<MoviesResponse> {
        return apiService.getMovieList()
    }

    override suspend fun getMovies(): List<MoviesEntity> {
        return dao.getPaginatedMovies(10, 0)
    }

    override suspend fun toggleWishlist(id: Int, isFavorite: Boolean) {
        dao.updateFavorite(id = id, isFavorite = isFavorite)
    }
}