package com.quoteexplorer.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.quoteexplorer.data.repository.QuoteRepository
import com.quoteexplorer.model.Quote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val quotes: List<Quote> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val page: Int = 1
)

class HomeViewModel(private val repository: QuoteRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private var allQuotesCache: List<Quote> = emptyList()

    // Map user categories to keywords for filtering
    val categories = listOf(
        "All", "Inspirational", "Life", "Love", "Happiness", "Sadness", 
        "Work", "Education", "Spiritual", "Historical", "Art", 
        "Humor", "Nature", "Sports", "Self-Improvement", "Cultural"
    )

    private val categoryKeywords = mapOf(
        "Inspirational" to listOf("success", "dream", "goal", "courage", "positive", "inspire", "believe"),
        "Life" to listOf("life", "live", "growth", "wisdom", "change"),
        "Love" to listOf("love", "heart", "friend", "family", "marriage"),
        "Happiness" to listOf("joy", "happy", "laugh", "optimism"),
        "Sadness" to listOf("loss", "grief", "pain", "miss", "sad"),
        "Work" to listOf("work", "business", "leader", "team"),
        "Education" to listOf("learn", "knowledge", "teacher", "student", "study"),
        "Spiritual" to listOf("god", "faith", "prayer", "soul", "spirit"),
        "Historical" to listOf("freedom", "justice", "war", "peace", "history"),
        "Art" to listOf("art", "music", "write", "paint", "create"),
        "Humor" to listOf("funny", "joke", "laugh", "wit"),
        "Nature" to listOf("nature", "earth", "world", "season", "animal"),
        "Sports" to listOf("sport", "game", "win", "run", "play"),
        "Self-Improvement" to listOf("habit", "grow", "better", "mind"),
        "Cultural" to listOf("culture", "book", "read", "poet")
    )

    // Observe favorites from DB
    val favorites = repository.getFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadQuotes() // Load initial page / all quotes
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
        filterQuotes(category)
    }

    private fun filterQuotes(category: String) {
        if (category == "All") {
            _uiState.update { it.copy(quotes = allQuotesCache, isLoading = false) }
        } else {
            val keywords = categoryKeywords[category] ?: emptyList()
            val filtered = allQuotesCache.filter { quote ->
                keywords.any { k -> quote.quote.contains(k, ignoreCase = true) || quote.author.contains(k, ignoreCase = true) }
            }
            _uiState.update { it.copy(quotes = filtered, isLoading = false) }
        }
    }

    fun loadQuotes() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                // Fetch ALL quotes once for client-side filtering
                if (allQuotesCache.isEmpty()) {
                    val allQuotes = repository.getAllQuotes()
                    allQuotesCache = allQuotes
                }
                
                // Apply current filter
                filterQuotes(_selectedCategory.value)
                
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun toggleFavorite(quote: Quote) {
        viewModelScope.launch {
            val isFavorite = favorites.value.any { it.id == quote.id }
            if (isFavorite) {
                repository.removeFavorite(quote.id)
            } else {
                repository.addFavorite(quote)
            }
        }
    }

    fun refresh() {
        allQuotesCache = emptyList()
        loadQuotes()
    }
}

class HomeViewModelFactory(private val repository: QuoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
