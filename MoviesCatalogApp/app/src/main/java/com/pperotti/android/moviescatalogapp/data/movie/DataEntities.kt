package com.pperotti.android.moviescatalogapp.data.movie

import com.google.gson.annotations.SerializedName

// This file contains all the data classes representing the data exposed for movies

/**
 * Represent the result of a query to TMDB api.
 *
 * <code>
 *     {
 *     "page": 1,
 *     "results": [
 *         {
 *             "adult": false,
 *             "backdrop_path": "/zOpe0eHsq0A2NvNyBbtT6sj53qV.jpg",
 *             "genre_ids": [
 *                 28,
 *                 878,
 *                 35,
 *                 10751
 *             ],
 *             "id": 939243,
 *             "original_language": "en",
 *             "original_title": "Sonic the Hedgehog 3",
 *             "overview": "Sonic, Knuckles, and Tails reunite against a powerful new adversary, Shadow, a mysterious villain with powers unlike anything they have faced before. With their abilities outmatched in every way, Team Sonic must seek out an unlikely alliance in hopes of stopping Shadow and protecting the planet.",
 *             "popularity": 4924.543,
 *             "poster_path": "/d8Ryb8AunYAuycVKDp5HpdWPKgC.jpg",
 *             "release_date": "2024-12-19",
 *             "title": "Sonic the Hedgehog 3",
 *             "video": false,
 *             "vote_average": 7.8,
 *             "vote_count": 1438
 *         },
 *         ...
 *     ],
 *     "total_pages": 48513,
 *     "total_results": 970247
 * }
 * </code>
*/
data class MovieListResult(
    val page: Int,
    val results: List<MovieItem>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)

/**
 * Represent an entry in the 'results' list.
 *
 * <code>
 *     {
 *     "adult": false,
 *     "backdrop_path": "/pqulyfkug9A7TmmRn5zrbRA8TAY.jpg",
 *     "genre_ids": [
 *         28,
 *         35
 *     ],
 *     "id": 1255788,
 *     "original_language": "fr",
 *     "original_title": "Le Jardinier",
 *     "overview": "Every year the Prime Minister has a list of all kinds of troublemakers eliminated in the name of the famous Reason of State. Serge Shuster, Special Adviser to the President of the Republic, finds himself on this list, better known as the Matignon List.  Condemned to certain death and at the heart of an implacable plot and a state secret that also put his family in danger, Serge, his wife and children have only one hope left - their gardener, Léo, who hates it when « slugs » invade his garden... Especially those that want to kill innocent families.",
 *     "popularity": 1034.435,
 *     "poster_path": "/5T9WR7vIOnHm6xhVt5zBuPbBgt1.jpg",
 *     "release_date": "2025-01-30",
 *     "title": "The Gardener",
 *     "video": false,
 *     "vote_average": 6.135,
 *     "vote_count": 37
 * }
 * </code>
 */
data class MovieItem(
    val adult: Boolean?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("genre_ids") val genreIds: List<Int>?,
    val id: Int,
    @SerializedName("original_language") val originalLanguage: String?,
    @SerializedName("original_title") val originalTitle: String?,
    val overview: String?,
    val popularity: Float?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("release_date") val releaseDate: String?,
    val title: String?,
    val video: Boolean?,
    @SerializedName("vote_average") val voteAverage: Float?,
    @SerializedName("vote_count") val voteCount: Int?
)

data class MovieCollection(
    val id: Int,
    val name: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?
)

data class MovieGenre(
    val id: Int,
    val name: String?
)

data class ProductionCompany(
    val id: Int,
    @SerializedName("logo_path") val logoPath: String?,
    val name: String?,
    @SerializedName("origin_country") val originCountry: String?
)

data class ProductionCountry(
    @SerializedName("iso?3166_1") val iso31661: String?,
    val name: String?
)

data class SpokenLanguage(
    @SerializedName("english_name") val englishName: String?,
    @SerializedName("iso_639_1") val iso6391: String?,
    val name: String?
)

/**
 * Represent the full details for a single item
 *
 * <code>
 *     {
 *     "adult": false,
 *     "backdrop_path": "/zOpe0eHsq0A2NvNyBbtT6sj53qV.jpg",
 *     "belongs_to_collection": {
 *         "id": 720879,
 *         "name": "Sonic the Hedgehog Collection",
 *         "poster_path": "/fwFWhYXj8wY6gFACtecJbg229FI.jpg",
 *         "backdrop_path": "/l5CIAdxVhhaUD3DaS4lP4AR2so9.jpg"
 *     },
 *     "budget": 122000000,
 *     "genres": [
 *         {
 *             "id": 28,
 *             "name": "Action"
 *         },
 *         .....
 *     ],
 *     "homepage": "https://www.sonicthehedgehogmovie.com",
 *     "id": 939243,
 *     "imdb_id": "tt18259086",
 *     "origin_country": [
 *         "US"
 *     ],
 *     "original_language": "en",
 *     "original_title": "Sonic the Hedgehog 3",
 *     "overview": "Sonic, Knuckles, and Tails reunite against a powerful new adversary, Shadow, a mysterious villain with powers unlike anything they have faced before. With their abilities outmatched in every way, Team Sonic must seek out an unlikely alliance in hopes of stopping Shadow and protecting the planet.",
 *     "popularity": 4924.543,
 *     "poster_path": "/d8Ryb8AunYAuycVKDp5HpdWPKgC.jpg",
 *     "production_companies": [
 *         {
 *             "id": 4,
 *             "logo_path": "/gz66EfNoYPqHTYI4q9UEN4CbHRc.png",
 *             "name": "Paramount Pictures",
 *             "origin_country": "US"
 *         },
 *         ...
 *     ],
 *     "production_countries": [
 *         {
 *             "iso_3166_1": "US",
 *             "name": "United States of America"
 *         },
 *         ...
 *     ],
 *     "release_date": "2024-12-19",
 *     "revenue": 462549154,
 *     "runtime": 110,
 *     "spoken_languages": [
 *         {
 *             "english_name": "English",
 *             "iso_639_1": "en",
 *             "name": "English"
 *         }
 *         ...
 *     ],
 *     "status": "Released",
 *     "tagline": "Try to keep up.",
 *     "title": "Sonic the Hedgehog 3",
 *     "video": false,
 *     "vote_average": 7.8,
 *     "vote_count": 1438
 * }
 * </code>
 */
data class MovieDetails(
    val adult: Boolean,
    @SerializedName("backdropPath") val backdropPath: String?,
    @SerializedName("belongs_to_collection") val belongsToCollection: MovieCollection?,
    val budget: Long?,
    val genres: List<MovieGenre>?,
    val homepage: String?,
    val id: Int,
    val imdbId: String?,
    @SerializedName("origin_country") val originCountry: List<String>?,
    @SerializedName("original_language") val originalLanguage: String?,
    @SerializedName("original_title") val originalTitle: String?,
    val overview: String?,
    val popularity: Float?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("production_companies") val productionCompanies: List<ProductionCompany>?,
    @SerializedName("production_countries") val productionCountries: List<ProductionCountry>?,
    @SerializedName("release_date") val releaseDate: String?,
    val revenue: Long,
    val runtime: Int,
    @SerializedName("spoken_languages") val spokenLanguages: List<SpokenLanguage>,
    val status: String?,
    val tagline: String?,
    val title: String?,
    val video: Boolean,
    @SerializedName("vote_average") val voteAverage: Float,
    @SerializedName("vote_count") val voteCount: Int
)
