package com.pperotti.android.moviescatalogapp.data.movie

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    @GET("discover/movie?language=en-US&sort_by=popularity")
    suspend fun fetchMovieList(
        @Query("include_adult") adult: Boolean,
        @Query("include_video") video: Boolean,
        @Query("page") page: Int
    ): MovieListResult

    @GET("movie/{id}")
    suspend fun fetchMovieDetails(@Path("id") id: Int): MovieDetails
}
