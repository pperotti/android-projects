package com.pperotti.android.moviescatalogapp.presentation.details

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.pperotti.android.moviescatalogapp.R
import com.pperotti.android.moviescatalogapp.presentation.common.ErrorContent
import com.pperotti.android.moviescatalogapp.presentation.common.LoadingContent
import com.pperotti.android.moviescatalogapp.presentation.common.MessageItemComposable
import com.pperotti.android.moviescatalogapp.presentation.common.TextWithIconRowComposable

/**
 * Show the details of the movie or an error
 */
@Composable
fun DetailsScreen(
    id: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    detailsViewModel: DetailsViewModel = hiltViewModel()
) {
    val key: Int by remember { mutableIntStateOf(id) }
    LaunchedEffect(key) {
        detailsViewModel.requestDetails(id)
    }

    // Collect Data from viewModel
    detailsViewModel.uiState.collectAsState().value.let { state ->
        DrawScreenContent(state, modifier, onBack)
    }
}

@Composable
fun DrawScreenContent(
    uiState: DetailsUiState,
    modifier: Modifier,
    onBack: () -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            DetailsScreenTopAppBar(
                onBack = onBack,
                modifier = modifier
            )
        }
    ) { paddingValues ->
        when (uiState) {
            is DetailsUiState.Loading -> LoadingContent(modifier)
            is DetailsUiState.Success -> SuccessContent(uiState, paddingValues, modifier)
            is DetailsUiState.Error -> ErrorContent(uiState.message, modifier)
        }
    }
}

@Composable
fun SuccessContent(
    uiState: DetailsUiState.Success,
    paddingValues: PaddingValues,
    modifier: Modifier
) {
    // Draw Content Depending on the Orientation
    val configuration = LocalConfiguration.current
    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        LandscapeSuccessContent(uiState, paddingValues, modifier)
    } else {
        PortraitSuccessContent(uiState, paddingValues, modifier)
    }
}

@Composable
fun LandscapeSuccessContent(
    uiState: DetailsUiState.Success,
    paddingValues: PaddingValues,
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues = paddingValues)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(uiState.details.posterPath)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(id = R.string.top_bar_back_button_description),
            contentScale = ContentScale.FillHeight,
            modifier = modifier
                .fillMaxWidth(fraction = 0.3f)
                .fillMaxHeight()
        )
        Column(
            modifier = modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            DrawDetailsContent(uiState, modifier)
        }
    }
}

@Composable
fun PortraitSuccessContent(
    uiState: DetailsUiState.Success,
    paddingValues: PaddingValues,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(paddingValues = paddingValues)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(uiState.details.posterPath)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(id = R.string.top_bar_back_button_description),
            contentScale = ContentScale.FillHeight,
            modifier = modifier
                .fillMaxWidth()
                .height(300.dp)
        )
        DrawDetailsContent(uiState, modifier)
    }
}

@Composable
fun DrawDetailsContent(uiState: DetailsUiState.Success, modifier: Modifier) {
    Row(modifier = modifier.fillMaxWidth()) {
        Text(
            text = uiState.details.title ?: "-",
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(PaddingValues(horizontal = 8.dp, vertical = 16.dp))
        )
    }
    Spacer(
        modifier = Modifier
            .padding(PaddingValues(horizontal = 8.dp, vertical = 4.dp))
            .background(color = MaterialTheme.colorScheme.primary)
            .fillMaxWidth()
            .height(2.dp)
    )
    MessageItemComposable(R.string.details_imdb_id, uiState.details.imdbId ?: "-")
    MessageItemComposable(R.string.details_homepage, uiState.details.homepage ?: "-")
    MessageItemComposable(R.string.details_overview, uiState.details.overview ?: "-")
    MessageItemComposable(R.string.details_revenue, uiState.details.revenue.toString())
    MessageItemComposable(R.string.details_status, uiState.details.status ?: "-")
    MessageItemComposable(R.string.details_vote_average, uiState.details.voteAverage.toString())
    MessageItemComposable(R.string.details_vote_count, uiState.details.voteCount.toString())
    Spacer(
        modifier = Modifier
            .padding(PaddingValues(horizontal = 8.dp, vertical = 4.dp))
            .background(color = MaterialTheme.colorScheme.secondary)
            .fillMaxWidth()
            .height(1.dp)
    )
    MessageItemComposable(R.string.details_genres, "")
    uiState.details.genres.forEach {
        TextWithIconRowComposable(it.name ?: "-")
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreenTopAppBar(
    onBack: () -> Unit,
    modifier: Modifier
) {
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = onBack
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.top_bar_back_button_description)
                )
            }
        },
        title = {
            Box(
                modifier = modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.movie_details_top_bar_title),
                    modifier = modifier,
                    fontSize = 20.sp
                )
            }
        },
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            scrolledContainerColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        modifier = modifier.fillMaxWidth()
    )
}
