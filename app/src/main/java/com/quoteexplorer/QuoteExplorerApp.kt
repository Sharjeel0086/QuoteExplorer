package com.quoteexplorer

import android.app.Application
import com.quoteexplorer.data.local.QuoteDatabase
import com.quoteexplorer.data.repository.QuoteRepository

class QuoteExplorerApp : Application() {
    
    // Manual DI
    val database by lazy { QuoteDatabase.getDatabase(this) }
    val repository by lazy { QuoteRepository(database.favoriteDao()) }
}
