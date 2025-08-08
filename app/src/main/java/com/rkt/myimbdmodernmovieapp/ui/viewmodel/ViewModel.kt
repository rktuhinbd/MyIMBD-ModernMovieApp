package com.rkt.myimbdmodernmovieapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rkt.myimbdmodernmovieapp.base.ResponseHandler
import com.rkt.myimbdmodernmovieapp.base.UIState
import com.rkt.myimbdmodernmovieapp.domain.use_case.GetMoviesUseCase
import com.rkt.myimbdmodernmovieapp.domain.use_case.GetPaginatedMoviesUseCase
import com.rkt.myimbdmodernmovieapp.domain.use_case.GetWishlistUseCase
import com.rkt.myimbdmodernmovieapp.domain.use_case.UpdateFavoriteUseCase
import com.rkt.myimbdmodernmovieapp.model.GenreEntity
import com.rkt.myimbdmodernmovieapp.model.MovieAndGenreResult
import com.rkt.myimbdmodernmovieapp.model.MoviesEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val updateFavoriteUseCase: UpdateFavoriteUseCase,
    private val getWishlistUseCase: GetWishlistUseCase,
    private val getPaginatedMoviesUseCase: GetPaginatedMoviesUseCase
) : ViewModel() {

    private val tag = "ViewModel"

    private val pageSize = 10
    private var currentPage = 0
    private var isLoadingMore = false
    private var allDataLoaded = false


    fun onUiEvent(event: ApiUiEvent) {
        when (event) {
            is ApiUiEvent.GetMovieList -> getMovieList()
            is ApiUiEvent.UpdateFavorite -> updateFavorite(event.id, event.isFavorite)
            is ApiUiEvent.GetWishlist -> getWishlist()
        }
    }

    private val _wishlistObserver = MutableStateFlow<UIState<List<MoviesEntity>>>(UIState.Empty())
    val wishlistObserver = _wishlistObserver.asStateFlow()

    fun getWishlist() {
        viewModelScope.launch {
            _wishlistObserver.value = UIState.Loading()
            try {
                val wishlist = getWishlistUseCase()
                _wishlistObserver.value = UIState.Success(wishlist)
            } catch (e: Exception) {
                _wishlistObserver.value =
                    UIState.Error("Failed to load wishlist: ${e.localizedMessage}")
            }
        }
    }

    private val _movieListObserver = MutableStateFlow<UIState<MovieAndGenreResult>>(UIState.Loading())
    val movieListObserver = _movieListObserver.asStateFlow()


    fun loadNextPage() {
        if (isLoadingMore || allDataLoaded) return

        isLoadingMore = true

        viewModelScope.launch {
            try {
                val newMovies = getPaginatedMoviesUseCase(pageSize, currentPage * pageSize)

                if (newMovies.isEmpty()) {
                    allDataLoaded = true
                    return@launch
                }

                val currentGenres = (_movieListObserver.value as? UIState.Success)?.data?.genres
                    ?: getGenres()

                val currentMovies = (_movieListObserver.value as? UIState.Success)?.data?.movies
                    ?: emptyList()

                val combined = currentMovies + newMovies

                _movieListObserver.value = UIState.Success(
                    MovieAndGenreResult(combined, currentGenres)
                )

                currentPage++
            } catch (e: Exception) {
                Log.e("ViewModel", "Pagination error: ${e.localizedMessage}")
            } finally {
                isLoadingMore = false
            }
        }
    }

    private suspend fun getGenres(): List<GenreEntity> {
        return try {
            getMoviesUseCase()
                .firstOrNull { it is ResponseHandler.Success }
                ?.let { (it as ResponseHandler.Success).data?.genres } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }



    private fun getMovieList() {
        getMoviesUseCase().onEach { response ->
            when (response) {
                is ResponseHandler.Loading -> {
                    _movieListObserver.value = UIState.Loading()
                    Log.d(tag, "Loading :: get_movies")
                }

                is ResponseHandler.Success -> {
                    // Extract data from the Response body
                    val dataResponse =
                        response.data // This should extract the actual response body

                    if (dataResponse?.movies != null) {
                        _movieListObserver.value = UIState.Success(dataResponse)
                    }
                }

                is ResponseHandler.Error -> {
                    _movieListObserver.value = UIState.Error(response.error?.msgWithCode ?: "")
                    Log.e(tag, "Error :: get_movies -> ${response.error?.msgWithCode}")
                }

                else -> Unit
            }
        }.launchIn(viewModelScope)
    }

    private fun updateFavorite(id: Int, favorite: Boolean) {
        viewModelScope.launch {
            try {
                val success = updateFavoriteUseCase(id, favorite)
                if (success) {
                    Log.d(tag, "Favorite updated successfully for movie id = $id")

                    // ✅ Update the single movie in current state without refetching everything
                    val currentState = _movieListObserver.value
                    if (currentState is UIState.Success) {
                        val currentData = currentState.data

                        if (currentData == null) return@launch

                        val updatedMovies = currentData.movies.map { movie ->
                            if (movie.id == id) movie.copy(isFavorite = favorite)
                            else movie
                        }

                        _movieListObserver.value = UIState.Success(
                            currentData.copy(movies = updatedMovies)
                        )
                    }
                } else {
                    Log.w(tag, "Favorite update failed for movie id = $id")
                }
            } catch (e: Exception) {
                Log.e(tag, "Error updating favorite: ${e.localizedMessage}")
            }
        }
    }
}

sealed class ApiUiEvent {
    data object GetMovieList : ApiUiEvent()
    data class UpdateFavorite(val id: Int, val isFavorite: Boolean) : ApiUiEvent()
    data object GetWishlist : ApiUiEvent()
}