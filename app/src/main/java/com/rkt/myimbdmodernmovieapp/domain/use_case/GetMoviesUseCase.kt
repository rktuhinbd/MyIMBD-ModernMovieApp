package com.rkt.myimbdmodernmovieapp.domain.use_case


import com.rkt.myimbdmodernmovieapp.base.ErrorHandler
import com.rkt.myimbdmodernmovieapp.base.ResponseHandler
import com.rkt.myimbdmodernmovieapp.domain.repo.ApiRepo
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(
    private val repo: ApiRepo
) {
    operator fun invoke() = flow {
        try {
            emit(ResponseHandler.Loading())
            val response = repo.getMovieList()
            emit(ResponseHandler.Success(response))
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