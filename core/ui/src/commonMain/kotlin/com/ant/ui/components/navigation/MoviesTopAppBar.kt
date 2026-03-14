package com.ant.ui.components.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

/**
 * Generic top app bar with a title and action button
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesTopAppBar(
    title: String,
    actionIcon: ImageVector,
    actionIconContentDescription: String,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(title) },
        actions = {
            IconButton(onClick = onActionClick) {
                Icon(
                    imageVector = actionIcon,
                    contentDescription = actionIconContentDescription
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        modifier = modifier
    )
}

@Preview
@Composable
private fun MoviesTopAppBarPreview() {
    MaterialTheme {
        MoviesTopAppBar(
            title = "Movies",
            actionIcon = Icons.Default.Search,
            actionIconContentDescription = "Search",
            onActionClick = {},
        )
    }
}
