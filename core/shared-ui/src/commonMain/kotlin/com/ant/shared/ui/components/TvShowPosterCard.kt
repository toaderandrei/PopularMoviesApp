package com.ant.shared.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.ant.models.entities.TvShow
import com.ant.shared.ui.Res
import com.ant.shared.ui.drawable.error_poster_image
import com.ant.shared.ui.drawable.placeholder_movie_item_image
import org.jetbrains.compose.resources.painterResource

private const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

@Composable
fun TvShowPosterCard(
    tvShow: TvShow,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        AsyncImage(
            model = tvShow.posterPath?.let { "$TMDB_IMAGE_BASE_URL$it" },
            contentDescription = tvShow.name,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(Res.drawable.placeholder_movie_item_image),
            error = painterResource(Res.drawable.error_poster_image),
        )
    }
}
