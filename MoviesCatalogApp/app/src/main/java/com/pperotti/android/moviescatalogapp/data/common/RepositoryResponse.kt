package com.pperotti.android.moviescatalogapp.data.common

/**
 * Generic Response Wrapper that can be reused by more than one repo.
 */
sealed class RepositoryResponse<T> {
    data class Success<T>(val result: T) : RepositoryResponse<T>()
    data class Error<T>(val message: String?, val cause: Throwable?) : RepositoryResponse<T>()
}
