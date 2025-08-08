package com.rkt.myimbdmodernmovieapp.domain.use_case

import com.rkt.myimbdmodernmovieapp.data.local.dao.RoomDao
import com.rkt.myimbdmodernmovieapp.model.MoviesEntity
import javax.inject.Inject

class GetPaginatedMoviesUseCase @Inject constructor(
    private val dao: RoomDao
) {
    suspend operator fun invoke(limit: Int, offset: Int): List<MoviesEntity> {
        return dao.getPaginatedMovies(limit, offset)
    }
}