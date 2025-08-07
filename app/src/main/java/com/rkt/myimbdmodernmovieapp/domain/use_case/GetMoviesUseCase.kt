package com.rkt.myimbdmodernmovieapp.domain.use_case


import com.rkt.myimbdmodernmovieapp.base.ErrorHandler
import com.rkt.myimbdmodernmovieapp.base.ResponseHandler
import com.rkt.myimbdmodernmovieapp.data.local.dao.RoomDao
import com.rkt.myimbdmodernmovieapp.domain.repo.ApiRepo
import com.rkt.myimbdmodernmovieapp.model.GenreEntity
import com.rkt.myimbdmodernmovieapp.model.MovieAndGenreResult
import com.rkt.myimbdmodernmovieapp.model.MoviesEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(
    private val repo: ApiRepo,
    private val dao: RoomDao
) {
    operator fun invoke(): Flow<ResponseHandler<MovieAndGenreResult>> = flow {
        try {
            emit(ResponseHandler.Loading())

            val response = repo.getMovieList()
            val data = response.body()

            if (dao.getMovies().isEmpty()) {
                if (data != null) {
                    val mappedMovies = data.movies.map {
                        MoviesEntity(
                            actors = it.actors,
                            director = it.director,
                            genres = it.genres.joinToString(", "),
                            isFavorite = false,
                            plot = it.plot,
                            posterUrl = it.posterUrl,
                            runtime = it.runtime,
                            title = it.title,
                            year = it.year
                        )
                    }

                    val genres = data.genres.map {
                        GenreEntity(genre = it)
                    }

                    dao.insertMovies(mappedMovies)
                    dao.insertGenre(genres)
                } else {
                    emit(ResponseHandler.Error(ErrorHandler(localError = "No data found!")))
                    return@flow
                }
            }

            val movies = dao.getMovies()
            val genres = dao.getGenres()

            emit(ResponseHandler.Success(MovieAndGenreResult(movies, genres)))

        } catch (e: HttpException) {
            emit(
                ResponseHandler.Error(
                    ErrorHandler(
                        localError = (e.localizedMessage ?: "An unexpected error occurred")
                    )
                )
            )
        } catch (e: IOException) {
            emit(
                ResponseHandler.Error(
                    ErrorHandler(
                        localError = (e.localizedMessage ?: "Internet connection error occurred")
                    )
                )
            )
        } catch (e: Exception) {
            emit(
                ResponseHandler.Error(
                    ErrorHandler(
                        localError = (e.localizedMessage ?: "An unexpected error occurred")
                    )
                )
            )
        }
    }
}