package com.quoteexplorer.ui.screens.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.quoteexplorer.model.Quote
import com.quoteexplorer.ui.components.QuoteCard
import com.quoteexplorer.ui.theme.HeroGradientEnd
import com.quoteexplorer.ui.theme.HeroGradientStart
import com.quoteexplorer.ui.theme.QuoteShapes
import com.quoteexplorer.ui.theme.QuoteSpacing
import com.quoteexplorer.ui.theme.SurfaceWhite

@Composable

fun HomeScreen(
    viewModel: HomeViewModel,
    onQuoteClick: (Int) -> Unit,
    onFavoritesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val context = LocalContext.current

    val listState = rememberLazyListState()

    Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(
                start = QuoteSpacing.ScreenPadding,
                end = QuoteSpacing.ScreenPadding,
                top = QuoteSpacing.ScreenPadding,
                bottom = 100.dp // Space for bottom actions
            ),
            verticalArrangement = Arrangement.spacedBy(QuoteSpacing.ItemGap)
        ) {
            // Hero Section
            item {
                HeroSection()
                Spacer(modifier = Modifier.height(QuoteSpacing.SectionGap))
            }

            // Categories
            item {
                androidx.compose.foundation.lazy.LazyRow(
                    contentPadding = PaddingValues(horizontal = QuoteSpacing.ScreenPadding),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth().padding(bottom = QuoteSpacing.SectionGap)
                ) {
                    items(viewModel.categories) { category ->
                         CategoryChip(
                             category = category,
                             isSelected = category == selectedCategory,
                             onClick = { viewModel.selectCategory(category) }
                         )
                    }
                }
            }

            // Quotes List
            items(uiState.quotes) { quote ->
                val isFavorite = favorites.any { it.id == quote.id }
                QuoteCard(
                    quote = quote,
                    isFavorite = isFavorite,
                    onQuoteClick = onQuoteClick,
                    onFavoriteClick = { 
                        viewModel.toggleFavorite(quote)
                        val msg = if (isFavorite) "Removed from Favorites" else "Added to Favorites"
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                )
            }

            // Loading Indicator
            if (uiState.isLoading) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }

        // Floating Action Card for Favorites (Bottom Center)
        Card(
            onClick = onFavoritesClick,
            shape = QuoteShapes.FloatingActionCard,
            colors = androidx.compose.material3.CardDefaults.cardColors(
                containerColor = com.quoteexplorer.ui.theme.DarkCharcoal
            ),
            elevation = androidx.compose.material3.CardDefaults.cardElevation(
                defaultElevation = com.quoteexplorer.ui.theme.QuoteElevation.FloatingActionElevation
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .height(60.dp)
                .fillMaxWidth(0.6f)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = null,
                        tint = com.quoteexplorer.ui.theme.PrimaryYellow,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "My Favorites (${favorites.size})",
                        style = MaterialTheme.typography.labelLarge,
                        color = com.quoteexplorer.ui.theme.SurfaceWhite
                    )
                }
            }
        }
    }
}

@Composable
fun HeroSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(HeroGradientStart, HeroGradientEnd)
                ),
                shape = QuoteShapes.HeroCard
            )
            .padding(QuoteSpacing.CardPadding)
    ) {
        Column(
            modifier = Modifier.align(Alignment.BottomStart)
        ) {
            Text(
                text = "Quote of the Day",
                style = MaterialTheme.typography.displayMedium,
                color = SurfaceWhite
            )
            Text(
                text = "Inspiration awaits.",
                style = MaterialTheme.typography.bodyLarge,
                color = SurfaceWhite.copy(alpha = 0.9f)
            )
        }
    }
}
