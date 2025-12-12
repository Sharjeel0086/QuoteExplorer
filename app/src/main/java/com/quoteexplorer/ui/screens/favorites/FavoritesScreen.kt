package com.quoteexplorer.ui.screens.favorites

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.quoteexplorer.data.local.FavoriteEntity
import com.quoteexplorer.data.repository.QuoteRepository
import com.quoteexplorer.model.Quote
import com.quoteexplorer.ui.components.QuoteCard
import com.quoteexplorer.ui.theme.QuoteSpacing
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(private val repository: QuoteRepository) : ViewModel() {
    val favorites = repository.getFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun removeFavorite(id: Int) {
        viewModelScope.launch {
            repository.removeFavorite(id)
        }
    }
}

class FavoritesViewModelFactory(private val repository: QuoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavoritesViewModel(repository) as T
    }
}

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val favorites by viewModel.favorites.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // App Bar / Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(QuoteSpacing.ScreenPadding)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "My Favorites",
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        if (favorites.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No favorites yet. Start adding some!")
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(QuoteSpacing.ScreenPadding),
                verticalArrangement = Arrangement.spacedBy(QuoteSpacing.ItemGap)
            ) {
                items(favorites) { entity ->
                    // Convert Entity to Quote for reuse
                    val quote = Quote(
                        id = entity.id,
                        quote = entity.content,
                        author = entity.author
                    )
                    
                    QuoteCard(
                        quote = quote,
                        isFavorite = true, // It's the favorites screen!
                        onQuoteClick = { /* Maybe go to details? */ },
                        onFavoriteClick = { 
                            viewModel.removeFavorite(entity.id)
                            Toast.makeText(context, "Removed from Favorites", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}
