package com.pperotti.android.moviescatalogapp.presentation.di

import com.pperotti.android.moviescatalogapp.data.movie.MovieRepository
import com.pperotti.android.moviescatalogapp.presentation.details.DetailsViewModel
import com.pperotti.android.moviescatalogapp.presentation.main.MainViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * This file contains the Hilt module definition for all dependencies
 * used from presentation layer.
 */
@Module
@InstallIn(SingletonComponent::class)
class PresentationModule {

    @Provides
    @Singleton
    fun provideMainViewModel(movieRepository: MovieRepository): MainViewModel {
        return MainViewModel(movieRepository)
    }

    @Provides
    fun provideDetailsViewModel(movieRepository: MovieRepository): DetailsViewModel {
        return DetailsViewModel(movieRepository)
    }
}
