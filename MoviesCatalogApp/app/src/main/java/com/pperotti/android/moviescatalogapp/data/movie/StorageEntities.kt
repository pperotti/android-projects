package com.pperotti.android.moviescatalogapp.data.movie

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Entity(tableName = "movielistresult")
class StorageMovieListResult(
    @PrimaryKey
    val page: Int,
    val totalPages: Int,
    val totalResults: Int
)

@Entity(tableName = "movies")
class StorageMovie(
    @PrimaryKey
    val id: Int,
    val page: Int,
    val adult: Boolean?,
    val backdropPath: String?,
    val genreIds: List<Int>?,
    val originalLanguage: String?,
    val originalTitle: String?,
    val overview: String?,
    val popularity: Float?,
    val posterPath: String?,
    val releaseDate: String?,
    val title: String?,
    val video: Boolean?,
    val voteAverage: Float?,
    val voteCount: Int?
)

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<StorageMovie>)

    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<StorageMovie>

    @Query("SELECT COUNT(*) FROM movies")
    suspend fun getMovieCount(): Int

    @Query("DELETE FROM movies")
    suspend fun deleteAllMovies()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieListResult(movieListResult: StorageMovieListResult)

    @Query("SELECT * FROM movielistresult")
    suspend fun getMovieListResult(): StorageMovieListResult

    @Query("DELETE FROM movielistresult")
    suspend fun deleteMovieListResult()

    @Query("SELECT COUNT(*) FROM movielistresult")
    suspend fun hasMovieListResult(): Int

}

@Database(
    entities = [
        StorageMovie::class,
        StorageMovieListResult::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(MovieTypeConverters::class)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}

fun MovieItem.toStorageMovie(page: Int): StorageMovie {
    return StorageMovie(
        id = id,
        page = page,
        adult = adult,
        backdropPath = backdropPath,
        genreIds = genreIds,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount,
    )
}

fun StorageMovie.toMovieItem(): MovieItem {
    return MovieItem(
        adult = adult,
        backdropPath = backdropPath,
        genreIds = genreIds,
        id = id,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount
    )
}

fun StorageMovieListResult.toMovieListResult(storageMovieList: List<StorageMovie>): MovieListResult {
    return MovieListResult(
        page = page,
        results = storageMovieList.map { it.toMovieItem() },
        totalPages = totalPages,
        totalResults = totalResults
    )
}

fun MovieListResult.toStorageMovieListResult(): StorageMovieListResult {
    return StorageMovieListResult(
        page = page,
        totalPages = totalPages,
        totalResults = totalResults
    )
}

