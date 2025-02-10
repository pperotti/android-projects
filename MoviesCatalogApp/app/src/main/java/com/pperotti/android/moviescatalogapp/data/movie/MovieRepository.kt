package com.pperotti.android.moviescatalogapp.data.movie

import android.util.Log
import com.pperotti.android.moviescatalogapp.data.common.RepositoryResponse
import com.pperotti.android.moviescatalogapp.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository used to interact with Data Layer. It should offer its clients the ability to retrieve
 * the listing of movies along with the details of each movie
 */
interface MovieRepository {

    /**
     * Request the repository to retrieve information about movies from
     * the network or local storage.
     *
     * @return MovieListResult encapsulated inside a RepositoryResponse
     */
    suspend fun fetchMovieList(): RepositoryResponse<MovieListResult>

    /**
     * Retrieves from the network the details for the movie with the specified id.
     *
     * @param id The Int value that identifies the movie.
     *
     * @return MovieDetails encapsulated inside a RepositoryResponse
     */
    suspend fun fetchMovieDetails(id: Int): RepositoryResponse<MovieDetails>
}

// Default implementation
@Singleton
class DefaultMovieRepository @Inject constructor(
    val localDataSource: MovieLocalDataSource,
    val remoteDataSource: MovieRemoteDataSource,
    @IoDispatcher val dispatcher: CoroutineDispatcher
) : MovieRepository {

    override suspend fun fetchMovieList(): RepositoryResponse<MovieListResult> {
        return withContext(dispatcher) {
            try {
                if (!localDataSource.hasMovieListResult()) {
                    val movieResultList = remoteDataSource.fetchMovieList()
                    localDataSource.saveMovieListResult(movieResultList)
                }
                RepositoryResponse.Success(localDataSource.getMovieListResult())
            } catch (e: Exception) {
                e.printStackTrace()
                RepositoryResponse.Error(e.localizedMessage, e.cause)
            }
        }
    }

    override suspend fun fetchMovieDetails(id: Int): RepositoryResponse<MovieDetails> {
        return withContext(dispatcher) {
            try {
                RepositoryResponse.Success(remoteDataSource.fetchMovieDetails(id))
            } catch (e: Exception) {
                e.printStackTrace()
                RepositoryResponse.Error(e.localizedMessage, e.cause)
            }
        }
    }
}
