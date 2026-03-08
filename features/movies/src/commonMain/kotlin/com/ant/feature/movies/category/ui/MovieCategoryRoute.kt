package com.ant.feature.movies.category.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.runtime.collectAsState
import com.ant.feature.movies.category.MovieCategoryViewModel

@Composable
fun MovieCategoryRoute(
    onNavigateToDetails: (movieId: Long) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MovieCategoryViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    MovieCategoryScreen(
        uiState = uiState,
        onMovieClick = onNavigateToDetails,
        onNavigateBack = onNavigateBack,
        onRefresh = viewModel::refresh,
        onLoadMore = viewModel::loadNextPage,
        modifier = modifier,
    )
}
