package com.rkt.myimbdmodernmovieapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rkt.myimbdmodernmovieapp.data.local.db.GenreTable
import com.rkt.myimbdmodernmovieapp.data.local.db.MoviesTable
import com.rkt.myimbdmodernmovieapp.model.GenreEntity
import com.rkt.myimbdmodernmovieapp.model.MoviesEntity

@Dao
interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MoviesEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenre(movies: List<GenreEntity>)

    @Query("SELECT * FROM ${GenreTable.TABLE_NAME}")
    suspend fun getGenres(): List<GenreEntity>

    @Query("SELECT * FROM ${MoviesTable.TABLE_NAME}")
    suspend fun getMovies(): List<MoviesEntity>

    @Query("SELECT * FROM ${MoviesTable.TABLE_NAME} ORDER BY ${MoviesTable.YEAR} ASC LIMIT :limit OFFSET :offset")
    suspend fun getPaginatedMovies(limit: Int, offset: Int): List<MoviesEntity>

    @Query("SELECT * FROM ${MoviesTable.TABLE_NAME} WHERE ${MoviesTable.IS_FAVORITE} = 1")
    suspend fun getWishlist(): List<MoviesEntity>

    @Query("UPDATE ${MoviesTable.TABLE_NAME} SET ${MoviesTable.IS_FAVORITE} = :isFavorite WHERE ${MoviesTable.ID} = :id")
    suspend fun updateFavorite(id: Int, isFavorite: Boolean): Int
}
