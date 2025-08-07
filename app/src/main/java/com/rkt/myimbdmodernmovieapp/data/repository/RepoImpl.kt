package com.rkt.myimbdmodernmovieapp.data.repository

import com.rkt.myimbdmodernmovieapp.data.dto.MoviesResponse
import com.rkt.myimbdmodernmovieapp.data.local.dao.MovieDao
import com.rkt.myimbdmodernmovieapp.data.service.ApiService
import com.rkt.myimbdmodernmovieapp.domain.repo.ApiRepo
import com.rkt.myimbdmodernmovieapp.model.MoviesEntity
import retrofit2.Response
import javax.inject.Inject
import kotlin.Int

class RepoImpl @Inject constructor(
    private val apiService: ApiService,
    private val dao: MovieDao
) : ApiRepo {

    override suspend fun getMovieList(): Response<MoviesResponse> {
        return apiService.getMovieList()
    }

    override suspend fun syncRemoteData() {

        val response = apiService.getMovieList().body()

        if(response == null) return

        val mapped = response.movies.map {
            MoviesEntity(
                actors = it.actors,
                director = it.director,
                genres = it.genres.joinToString(", "),
                isFavorite = false, // this is to track if the movie is on favorite list or not
                plot = it.plot,
                posterUrl = it.posterUrl,
                runtime = it.runtime,
                title = it.title,
                year = it.year
            )
        }
        dao.insertMovies(mapped)
    }

    override suspend fun getMovies(): List<MoviesEntity> {
        return dao.getPaginatedMovies(10, 0)
    }

    override suspend fun toggleWishlist(id: Int, isFavorite: Boolean) {
        dao.updateFavorite(id = id, isFavorite = isFavorite)
    }
}