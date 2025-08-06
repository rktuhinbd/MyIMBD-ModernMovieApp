package com.rkt.myimbdmodernmovieapp

data class Movie(
    val title: String,
    val year: String,
    val duration: String,
    val genre: String,
    val isFavorite: Boolean = false
)
