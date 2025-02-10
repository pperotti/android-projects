package com.pperotti.android.moviescatalogapp.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pperotti.android.moviescatalogapp.data.common.RepositoryResponse
import com.pperotti.android.moviescatalogapp.data.movie.MovieDetails
import com.pperotti.android.moviescatalogapp.data.movie.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    val repository: MovieRepository
) : ViewModel() {

    // A Job is required so you can cancel a running coroutine
    private var fetchJob: Job? = null

    // StateFlow to hold the UI state
    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState: StateFlow<DetailsUiState> get() = _uiState

    // Fetch Details
    fun requestDetails(id: Int) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {

            // Indicates the UI that loading should be presented
            _uiState.value = DetailsUiState.Loading

            // Retrieves the data
            when (val detailsResponse = repository.fetchMovieDetails(id)) {
                is RepositoryResponse.Success ->
                    transformSuccessResponse(detailsResponse.result)

                is RepositoryResponse.Error -> {
                    _uiState.value = DetailsUiState.Error(
                        message = detailsResponse.message
                    )
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        fetchJob?.cancel()
    }

    private fun transformSuccessResponse(movieDetails: MovieDetails) {
        val detailsUiData = DetailsUiData(
            id = movieDetails.id,
            imdbId = movieDetails.imdbId,
            homepage = movieDetails.homepage,
            overview = movieDetails.overview,
            posterPath = "https://image.tmdb.org/t/p/w200/${movieDetails.posterPath}",
            genres = movieDetails.genres?.map { DetailsUiGenre(it.id, it.name) } ?: emptyList(),
            title = movieDetails.title,
            revenue = movieDetails.revenue,
            status = movieDetails.status,
            voteAverage = movieDetails.voteAverage,
            voteCount = movieDetails.voteCount,
        )

        // Publish items tot he UI
        _uiState.value = DetailsUiState.Success(
            details = detailsUiData
        )
    }
}