package com.ant.app.ui.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ant.shared.ui.app.MainApp
import com.ant.ui.components.navigation.MoviesTopAppBar
import com.ant.design.theme.PopularMoviesTheme
import com.ant.resources.Res
import com.ant.resources.movies
import org.jetbrains.compose.resources.stringResource

class MainActivityCompose : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp()
        }
    }

    @Composable
    fun MyApp() {
        PopularMoviesTheme {
            MainApp()
        }
    }

    @Preview(
        name = "Movies Top App Bar Preview",
        showBackground = true,
        backgroundColor = 0xFFFFFF, // Optional: white background
        widthDp = 360 // Optional: set width
    )
    @Composable
    fun PreviewMoviesTopAppBar() {
        PopularMoviesTheme { // Apply your app's theme
            MoviesTopAppBar(
                title = stringResource(Res.string.movies),
                actionIcon = Icons.Default.Settings,
                actionIconContentDescription = "Settings",
                onActionClick = {},
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewMainApp() {
        PopularMoviesTheme {
            MainApp()
        }
    }
}
