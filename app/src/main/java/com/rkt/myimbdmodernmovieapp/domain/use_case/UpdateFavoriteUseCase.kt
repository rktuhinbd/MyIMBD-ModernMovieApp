package com.rkt.myimbdmodernmovieapp.domain.use_case


import com.rkt.myimbdmodernmovieapp.data.local.dao.RoomDao
import javax.inject.Inject

class UpdateFavoriteUseCase @Inject constructor(
    private val dao: RoomDao
) {
    suspend operator fun invoke(id: Int, isFavorite: Boolean): Boolean {
        return dao.updateFavorite(id, isFavorite) > 0
    }
}