package com.quoteexplorer.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.quoteexplorer.ui.theme.FavoriteActive
import com.quoteexplorer.ui.theme.QuoteElevation
import com.quoteexplorer.ui.theme.QuoteSizes
import com.quoteexplorer.ui.theme.SurfaceWhite

@Composable
fun FavoriteButton(
    isFavorite: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (isFavorite) 1.2f else 1.0f,
        animationSpec = tween(300), 
        label = "scale"
    )

    Surface(
        modifier = modifier
            .size(QuoteSizes.FavoriteButtonSize)
            .scale(if (isFavorite) scale else 1f), // Only scale up on favorite
        shape = CircleShape,
        color = SurfaceWhite,
        shadowElevation = QuoteElevation.FavoriteButtonElevation,
        onClick = onToggle
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                tint = if (isFavorite) FavoriteActive else Color.Gray,
                modifier = Modifier.size(QuoteSizes.SmallIconSize)
            )
        }
    }
}
