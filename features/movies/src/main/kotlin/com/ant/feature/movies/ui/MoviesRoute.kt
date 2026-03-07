package com.ant.feature.movies.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.koin.compose.viewmodel.koinViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ant.feature.movies.MoviesViewModel
import com.ant.models.request.MovieType

/**
 * Route composable for Movies screen - handles ViewModel injection and state collection
 */
@Composable
fun MoviesRoute(
    onNavigateToDetails: (movieId: Long) -> Unit,
    onNavigateToCategory: (MovieType) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MoviesViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MoviesScreen(
        uiState = uiState,
        onMovieClick = onNavigateToDetails,
        onMoreClick = onNavigateToCategory,
        onRefresh = viewModel::refresh,
        modifier = modifier,
    )
}
