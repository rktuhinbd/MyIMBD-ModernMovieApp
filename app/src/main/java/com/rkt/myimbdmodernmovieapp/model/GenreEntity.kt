package com.rkt.myimbdmodernmovieapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rkt.myimbdmodernmovieapp.data.local.db.GenreTable
import com.rkt.myimbdmodernmovieapp.data.local.db.MoviesTable

@Entity(tableName = GenreTable.TABLE_NAME)
data class GenreEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val genre: String
)