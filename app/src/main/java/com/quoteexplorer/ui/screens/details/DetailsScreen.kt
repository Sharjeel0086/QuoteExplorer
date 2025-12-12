package com.quoteexplorer.ui.screens.details

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.quoteexplorer.data.repository.QuoteRepository
import com.quoteexplorer.model.Quote
import com.quoteexplorer.ui.components.FavoriteButton
import com.quoteexplorer.ui.theme.QuoteElevation
import com.quoteexplorer.ui.theme.QuoteShapes
import com.quoteexplorer.ui.theme.QuoteSpacing
import com.quoteexplorer.ui.theme.SurfaceWhite
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val repository: QuoteRepository,
    private val quoteId: Int
) : ViewModel() {

    private val _quote = MutableStateFlow<Quote?>(null)
    val quote = _quote.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    // Observe if THIS quote is favorite
    val isFavorite = repository.isFavorite(quoteId)

    init {
        loadQuote()
    }

    private fun loadQuote() {
        viewModelScope.launch {
            _isLoading.value = true
            val fetched = repository.getQuote(quoteId)
            _quote.value = fetched
            _isLoading.value = false
        }
    }

    fun toggleFavorite() {
        // ... (removed empty method body or keep as logic placeholder)
    }
    
    fun setFavorite(isFav: Boolean) {
        viewModelScope.launch {
            val q = _quote.value ?: return@launch
            if (isFav) {
                // It IS a favorite, so we want to REMOVE
                repository.removeFavorite(q.id)
            } else {
                // IT IS NOT a favorite, ADD
                repository.addFavorite(q)
            }
        }
    }
}

class DetailsViewModelFactory(
    private val repository: QuoteRepository,
    private val quoteId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailsViewModel(repository, quoteId) as T
    }
}

@Composable
fun DetailsScreen(
    viewModel: DetailsViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val quote by viewModel.quote.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState(initial = false)
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(QuoteSpacing.ScreenPadding)
    ) {
        // Back Button
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            val currentQuote = quote
            if (currentQuote != null) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Card(
                        shape = QuoteShapes.HeroCard,
                        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = QuoteElevation.CardElevation),
                        modifier = Modifier.fillMaxWidth().padding(bottom = QuoteSpacing.SectionGap)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "\"${currentQuote.quote}\"",
                                style = MaterialTheme.typography.displaySmall,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "- ${currentQuote.author}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontStyle = FontStyle.Italic,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }

                    // Actions Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Copy
                        IconButton(onClick = {
                            clipboardManager.setText(AnnotatedString("${currentQuote.quote} - ${currentQuote.author}"))
                        }) {
                            Icon(Icons.Default.ContentCopy, "Copy")
                        }
                        
                        Spacer(modifier = Modifier.width(16.dp))

                        // Favorite (Big)
                        FavoriteButton(
                            isFavorite = isFavorite,
                            onToggle = { viewModel.setFavorite(isFavorite) }
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        // Share
                        IconButton(onClick = {
                            val sendIntent: Intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, "${currentQuote.quote} - ${currentQuote.author}")
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, null)
                            context.startActivity(shareIntent)
                        }) {
                            Icon(Icons.Default.Share, "Share")
                        }
                    }
                }
            } else {
                Text("Failed to load quote", modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}
