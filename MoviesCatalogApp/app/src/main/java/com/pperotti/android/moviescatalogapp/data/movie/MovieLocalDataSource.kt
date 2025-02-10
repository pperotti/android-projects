package com.pperotti.android.moviescatalogapp.data.movie

import javax.inject.Inject
import javax.inject.Singleton

interface MovieLocalDataSource {
    /**
     * Retrieve the list of stored movies
     */
    suspend fun getMovieListResult(): MovieListResult

    /**
     * Persist the MovieListResults
     */
    suspend fun saveMovieListResult(movieListResult: MovieListResult)

    /**
     * Determine whether there is a previous stored record in the DB
     */
    suspend fun hasMovieListResult(): Boolean
}

@Singleton
class DefaultMovieLocalDataSource @Inject constructor(
    val movieDao: MovieDao
) : MovieLocalDataSource {

    //TODO: Add page as param so it can support paging in the future.
    override suspend fun getMovieListResult(): MovieListResult {
        val storageMovieListResult = movieDao.getMovieListResult()
        val movies = movieDao.getAllMovies()
        return storageMovieListResult.toMovieListResult(movies)
    }

    override suspend fun saveMovieListResult(movieListResult: MovieListResult) {
        movieDao.deleteMovieListResult()
        movieDao.deleteAllMovies()
        movieDao.insertMovieListResult(movieListResult.toStorageMovieListResult())
        movieDao.insertAll(movieListResult.results.map { it.toStorageMovie(movieListResult.page) })
    }

    override suspend fun hasMovieListResult(): Boolean {
        return movieDao.hasMovieListResult() > 0
    }

}
