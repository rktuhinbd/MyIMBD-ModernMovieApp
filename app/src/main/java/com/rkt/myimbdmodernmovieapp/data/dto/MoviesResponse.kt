package com.rkt.myimbdmodernmovieapp.data.dto


import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    @SerializedName("genres")
    var genres: List<String> = listOf(),
    @SerializedName("movies")
    var movies: List<Movies> = listOf()
)