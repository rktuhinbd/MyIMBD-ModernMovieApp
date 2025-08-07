package com.rkt.myimbdmodernmovieapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rkt.myimbdmodernmovieapp.data.local.db.MoviesTable

@Entity(tableName = MoviesTable.TABLE_NAME)
data class MoviesEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val actors: String,
    val director: String,
    val genres: String,
    val isFavorite: Boolean, // this is to track if the movie is on favorite list or not
    val plot: String,
    val posterUrl: String,
    val runtime: String,
    val title: String,
    val year: String
)