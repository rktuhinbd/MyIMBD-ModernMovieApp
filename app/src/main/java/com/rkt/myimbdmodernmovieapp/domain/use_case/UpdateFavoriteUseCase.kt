package com.rkt.myimbdmodernmovieapp.domain.use_case


import com.rkt.myimbdmodernmovieapp.base.ErrorHandler
import com.rkt.myimbdmodernmovieapp.base.ResponseHandler
import com.rkt.myimbdmodernmovieapp.data.local.dao.RoomDao
import com.rkt.myimbdmodernmovieapp.model.GenreEntity
import com.rkt.myimbdmodernmovieapp.model.MovieAndGenreResult
import com.rkt.myimbdmodernmovieapp.model.MoviesEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class UpdateFavoriteUseCase @Inject constructor(
    private val dao: RoomDao
) {
    suspend operator fun invoke(id: Int, isFavorite: Boolean): Boolean {
        return dao.updateFavorite(id, isFavorite) > 0
    }
}