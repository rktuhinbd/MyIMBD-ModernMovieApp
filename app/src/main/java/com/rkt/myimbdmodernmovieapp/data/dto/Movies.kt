package com.rkt.myimbdmodernmovieapp.data.dto


import com.google.gson.annotations.SerializedName

data class Movies(
    @SerializedName("actors")
    var actors: String = "",
    @SerializedName("director")
    var director: String = "",
    @SerializedName("genres")
    var genres: List<String> = listOf(),
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("plot")
    var plot: String = "",
    @SerializedName("posterUrl")
    var posterUrl: String = "",
    @SerializedName("runtime")
    var runtime: String = "",
    @SerializedName("title")
    var title: String = "",
    @SerializedName("year")
    var year: String = ""
)