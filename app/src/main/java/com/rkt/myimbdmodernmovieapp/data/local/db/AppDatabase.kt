package com.rkt.myimbdmodernmovieapp.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rkt.myimbdmodernmovieapp.data.local.dao.RoomDao
import com.rkt.myimbdmodernmovieapp.model.GenreEntity
import com.rkt.myimbdmodernmovieapp.model.MoviesEntity


@Database(entities = [MoviesEntity::class, GenreEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): RoomDao
}