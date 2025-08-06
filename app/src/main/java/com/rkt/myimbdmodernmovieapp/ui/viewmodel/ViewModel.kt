package com.rkt.myimbdmodernmovieapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.rkt.myimbdmodernmovieapp.ResponseHandler
import com.rkt.myimbdmodernmovieapp.UIState
import com.rkt.myimbdmodernmovieapp.data.dto.MoviesResponse
import com.rkt.myimbdmodernmovieapp.domain.use_case.GetMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase
) : ViewModel() {

    private val tag = "ViewModel"

    fun onUiEvent(event: ApiUiEvent) {
        when (event) {
            is ApiUiEvent.GetMovieList -> getMovieList()
        }
    }

    private val _movieListObserver =
        MutableStateFlow<UIState<MoviesResponse>>(UIState.Empty())
    val movieListObserver = _movieListObserver.asStateFlow()

    private fun getMovieList() {
        getMoviesUseCase().onEach { response ->
            when (response) {
                is ResponseHandler.Loading -> {
                    _movieListObserver.value = UIState.Loading()
                    Log.d(tag, "Loading :: get_movies")
                }

                is ResponseHandler.Success -> {
                    // Extract data from the Response body
                    val apiResponse =
                        response.data?.body() // This should extract the actual response body
                    if (apiResponse != null) {
                        _movieListObserver.value = UIState.Success(apiResponse)
                    }
                    Log.d(
                        tag,
                        "Success :: movie_list -> ${
                            GsonBuilder().setPrettyPrinting().create().toJson(apiResponse)
                        }"
                    )
                }

                is ResponseHandler.Error -> {
                    _movieListObserver.value = UIState.Error(response.error?.msgWithCode ?: "")
                    Log.e(tag, "Error :: get_movies -> ${response.error?.msgWithCode}")
                }

                else -> Unit
            }
        }.launchIn(viewModelScope)
    }
}

sealed class ApiUiEvent {
    data object GetMovieList : ApiUiEvent()
}