package com.quoteexplorer.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.quoteexplorer.model.Quote
import com.quoteexplorer.ui.theme.QuoteElevation
import com.quoteexplorer.ui.theme.QuoteShapes
import com.quoteexplorer.ui.theme.QuoteSpacing
import com.quoteexplorer.ui.theme.SurfaceWhite

@Composable
fun QuoteCard(
    quote: Quote,
    isFavorite: Boolean,
    onQuoteClick: (Int) -> Unit,
    onFavoriteClick: (Quote) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Card(
            onClick = { onQuoteClick(quote.id) },
            shape = QuoteShapes.QuoteCard,
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = QuoteElevation.CardElevation),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(QuoteSpacing.CardPadding)
            ) {
                // Quote Text
                Text(
                    text = "\"${quote.quote}\"",
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(QuoteSpacing.ItemGap))
                
                // Author
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "- ${quote.author}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.secondary,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }

        // Floating Favorite Button (Top Right hanging over edge or inside)
        // Design usually puts it top right. Overlapping slightly or inside.
        // Let's put it inside top-right with some padding.
        FavoriteButton(
            isFavorite = isFavorite,
            onToggle = { onFavoriteClick(quote) },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = QuoteSpacing.SmallPadding, end = QuoteSpacing.SmallPadding)
        )
    }
}
