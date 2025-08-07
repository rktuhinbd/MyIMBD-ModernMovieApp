package com.rkt.myimbdmodernmovieapp.model

data class MovieAndGenreResult(
    val movies: List<MoviesEntity>,
    val genres: List<GenreEntity>
)
