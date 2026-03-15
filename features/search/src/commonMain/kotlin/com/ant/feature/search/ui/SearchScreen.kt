package com.ant.feature.search.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ant.feature.search.SearchTab
import com.ant.feature.search.SearchUiState
import com.ant.models.entities.MovieData
import com.ant.ui.components.ErrorState
import com.ant.ui.components.LoadingState
import com.ant.ui.components.MovieCard
import com.ant.ui.components.TvShowCard
import com.ant.resources.Res
import com.ant.resources.clear_search
import com.ant.resources.navigate_back
import com.ant.resources.search
import com.ant.resources.search_no_movies
import com.ant.resources.search_no_tv_shows
import com.ant.resources.search_placeholder
import com.ant.resources.search_prompt
import com.ant.resources.search_tab_movies
import com.ant.resources.search_tab_tv_shows
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    uiState: SearchUiState,
    onQueryChange: (String) -> Unit,
    onTabChange: (SearchTab) -> Unit,
    onMovieClick: (movieId: Long) -> Unit,
    onTvShowClick: (tvShowId: Long) -> Unit,
    onClearSearch: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.search)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.navigate_back),
                        )
                    }
                },
            )
        },
        modifier = modifier,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search bar
            TextField(
                value = uiState.query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                placeholder = { Text(stringResource(Res.string.search_placeholder)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                    )
                },
                trailingIcon = {
                    if (uiState.query.isNotEmpty()) {
                        IconButton(onClick = onClearSearch) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = stringResource(Res.string.clear_search),
                            )
                        }
                    }
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
            )

            if (uiState.query.isNotEmpty()) {
                // Tabs
                TabRow(selectedTabIndex = uiState.selectedTab.ordinal) {
                    Tab(
                        selected = uiState.selectedTab == SearchTab.MOVIES,
                        onClick = { onTabChange(SearchTab.MOVIES) },
                        text = { Text(stringResource(Res.string.search_tab_movies, uiState.movieResults.size)) },
                    )
                    Tab(
                        selected = uiState.selectedTab == SearchTab.TV_SHOWS,
                        onClick = { onTabChange(SearchTab.TV_SHOWS) },
                        text = { Text(stringResource(Res.string.search_tab_tv_shows, uiState.tvShowResults.size)) },
                    )
                }

                // Content
                when {
                    uiState.isSearching -> {
                        LoadingState(modifier = Modifier.fillMaxSize())
                    }

                    uiState.error != null -> {
                        ErrorState(
                            error = uiState.error,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }

                    else -> {
                        when (uiState.selectedTab) {
                            SearchTab.MOVIES -> {
                                if (uiState.movieResults.isEmpty()) {
                                    EmptyState(
                                        message = stringResource(Res.string.search_no_movies, uiState.query),
                                        modifier = Modifier.fillMaxSize(),
                                    )
                                } else {
                                    LazyVerticalGrid(
                                        columns = GridCells.Fixed(2),
                                        contentPadding = PaddingValues(16.dp),
                                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(16.dp),
                                        modifier = Modifier.fillMaxSize(),
                                    ) {
                                        items(
                                            count = uiState.movieResults.size,
                                            key = { uiState.movieResults[it].id },
                                        ) { index ->
                                            val movie = uiState.movieResults[index]
                                            MovieCard(
                                                movie = movie,
                                                onClick = { onMovieClick(movie.id) },
                                            )
                                        }
                                    }
                                }
                            }

                            SearchTab.TV_SHOWS -> {
                                if (uiState.tvShowResults.isEmpty()) {
                                    EmptyState(
                                        message = stringResource(Res.string.search_no_tv_shows, uiState.query),
                                        modifier = Modifier.fillMaxSize(),
                                    )
                                } else {
                                    LazyVerticalGrid(
                                        columns = GridCells.Fixed(2),
                                        contentPadding = PaddingValues(16.dp),
                                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(16.dp),
                                        modifier = Modifier.fillMaxSize(),
                                    ) {
                                        items(
                                            count = uiState.tvShowResults.size,
                                            key = { uiState.tvShowResults[it].id },
                                        ) { index ->
                                            val tvShow = uiState.tvShowResults[index]
                                            TvShowCard(
                                                tvShow = tvShow,
                                                onClick = { onTvShowClick(tvShow.id) },
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // Empty state - show prompt
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(Res.string.search_prompt),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(32.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyState(
    message: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
private fun SearchScreenContentPreview() {
    MaterialTheme {
        SearchScreen(
            uiState = SearchUiState(
                query = "Inception",
                movieResults = listOf(
                    MovieData(id = 1, name = "Inception"),
                    MovieData(id = 2, name = "Interstellar"),
                ),
                selectedTab = SearchTab.MOVIES,
            ),
            onQueryChange = {},
            onTabChange = {},
            onMovieClick = {},
            onTvShowClick = {},
            onClearSearch = {},
            onNavigateBack = {},
        )
    }
}

@Preview
@Composable
private fun SearchScreenEmptyQueryPreview() {
    MaterialTheme {
        SearchScreen(
            uiState = SearchUiState(),
            onQueryChange = {},
            onTabChange = {},
            onMovieClick = {},
            onTvShowClick = {},
            onClearSearch = {},
            onNavigateBack = {},
        )
    }
}

@Preview
@Composable
private fun SearchScreenLoadingPreview() {
    MaterialTheme {
        SearchScreen(
            uiState = SearchUiState(
                query = "Batman",
                isSearching = true,
            ),
            onQueryChange = {},
            onTabChange = {},
            onMovieClick = {},
            onTvShowClick = {},
            onClearSearch = {},
            onNavigateBack = {},
        )
    }
}

@Preview
@Composable
private fun SearchScreenErrorPreview() {
    MaterialTheme {
        SearchScreen(
            uiState = SearchUiState(
                query = "Batman",
                error = "Search failed. Check your connection.",
            ),
            onQueryChange = {},
            onTabChange = {},
            onMovieClick = {},
            onTvShowClick = {},
            onClearSearch = {},
            onNavigateBack = {},
        )
    }
}

@Preview
@Composable
private fun SearchScreenNoResultsPreview() {
    MaterialTheme {
        SearchScreen(
            uiState = SearchUiState(
                query = "xyznonexistent",
                movieResults = emptyList(),
                selectedTab = SearchTab.MOVIES,
            ),
            onQueryChange = {},
            onTabChange = {},
            onMovieClick = {},
            onTvShowClick = {},
            onClearSearch = {},
            onNavigateBack = {},
        )
    }
}
