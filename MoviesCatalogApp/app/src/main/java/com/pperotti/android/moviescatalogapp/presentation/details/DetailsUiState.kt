package com.pperotti.android.moviescatalogapp.presentation.details

// State used for the UI drawing the details
sealed class DetailsUiState() {
    object Loading : DetailsUiState()
    data class Success(val details: DetailsUiData) : DetailsUiState()
    data class Error(val message: String?) : DetailsUiState()
}

// Data Container with the information relevant to the UI
data class DetailsUiData(
    val id: Int,
    val imdbId: String?,
    val homepage: String?,
    val overview: String?,
    val posterPath: String?,
    val genres: List<DetailsUiGenre>,
    val title: String?,
    val revenue: Long,
    val status: String?,
    val voteAverage: Float?,
    val voteCount: Int
)

// Data Container for genre used by the UI
data class DetailsUiGenre(
    val id: Int,
    val name: String?
)
