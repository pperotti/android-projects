package com.pperotti.android.moviescatalogapp.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pperotti.android.moviescatalogapp.data.common.RepositoryResponse
import com.pperotti.android.moviescatalogapp.data.movie.MovieListResult
import com.pperotti.android.moviescatalogapp.data.movie.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val repository: MovieRepository
) : ViewModel() {

    // A Job is required so you can cancel a running coroutine
    private var fetchJob: Job? = null

    // StateFlow to hold the UI state
    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState: StateFlow<MainUiState> get() = _uiState

    // Request items from repository and convert them to UI items
    fun requestData() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {

            // Indicates the UI that loading should be presented
            _uiState.value = MainUiState.Loading

            when (val movieResponse = repository.fetchMovieList()) {
                is RepositoryResponse.Success ->
                    transformSuccessResponse(movieResponse.result)

                is RepositoryResponse.Error -> {
                    _uiState.value = MainUiState.Error(
                        movieResponse.message
                    )
                }
            }
        }
    }

    private fun transformSuccessResponse(movieListResult: MovieListResult) {
        val resultList: MutableList<MainListItemUiState> = mutableListOf()
        movieListResult.results.forEach { movie ->
            resultList.add(
                MainListItemUiState(
                    id = movie.id,
                    title = movie.title,
                    overview = movie.overview,
                    popularity = movie.popularity,
                    posterPath = "https://image.tmdb.org/t/p/original/${movie.posterPath}"
                )
            )
        }

        // Publish Items to the UI
        _uiState.value = MainUiState.Success(items = resultList)
    }
}
