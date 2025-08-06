package com.rkt.myimbdmodernmovieapp.model

data class Movies(
    val actors: String,
    val director: String,
    val genres: List<String> = listOf(),
    val isFavorite: Boolean = false, // this is to track if the movie is on favorite list or not
    val plot: String,
    val posterUrl: String,
    val runtime: String,
    val title: String,
    val year: String
)