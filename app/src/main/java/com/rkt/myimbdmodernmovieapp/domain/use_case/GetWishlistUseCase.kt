package com.rkt.myimbdmodernmovieapp.domain.use_case

import com.rkt.myimbdmodernmovieapp.domain.repo.ApiRepo
import com.rkt.myimbdmodernmovieapp.model.MoviesEntity
import javax.inject.Inject

class GetWishlistUseCase @Inject constructor(
    private val apiRepo: ApiRepo
) {
    suspend operator fun invoke(): List<MoviesEntity> {
        return apiRepo.getWishlist()
    }
}
