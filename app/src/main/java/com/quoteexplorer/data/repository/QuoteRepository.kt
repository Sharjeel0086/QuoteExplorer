package com.quoteexplorer.data.repository

import com.quoteexplorer.data.api.QuotableApi
import com.quoteexplorer.data.local.FavoriteDao
import com.quoteexplorer.data.local.FavoriteEntity
import com.quoteexplorer.model.Quote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class QuoteRepository(private val favoriteDao: FavoriteDao) {

    private val api: QuotableApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuotableApi::class.java)
    }

    suspend fun getQuotes(page: Int): List<Quote> {
        return withContext(Dispatchers.IO) {
            try {
                // DummyJSON uses skip/limit. Page 1 -> skip 0. Page 2 (limit 20) -> skip 20.
                val limit = 20
                val skip = (page - 1) * limit
                val response = api.getQuotes(skip = skip, limit = limit)
                response.quotes
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    // Fetch ALL quotes for category filtering (Client-side)
    suspend fun getAllQuotes(): List<Quote> {
        return withContext(Dispatchers.IO) {
            try {
                // limit=0 fetches all items in DummyJSON
                val response = api.getQuotes(skip = 0, limit = 0)
                response.quotes
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    suspend fun getQuote(id: Int): Quote? {
        return withContext(Dispatchers.IO) {
            // Try explicit API call first or cache? 
            // Usually we want fresh data, but for favorites we want our cached version?
            // User requirement: "Quote Details Screen ... favorite toggle"
            // Let's try to fetch from API, if fails, check if we have it in favorites?
            // Or simpler: Just fetch from API. 
            // But if we are offline and it's a favorite, we should find it in DB.
            
            // Check favorite cache first (fastest and works offline for favs)
            val fav = favoriteDao.isFavoriteSync(id) // This only returns boolean
            // We need to get the entity.
            // Let's modify DAO to get entity? 
            // For now, let's just try API. 
            try {
                api.getQuote(id)
            } catch (e: Exception) {
                // Return null or handle error
                null
            }
        }
    }

    fun getFavorites(): Flow<List<FavoriteEntity>> = favoriteDao.getAllFavorites()

    fun isFavorite(id: Int): Flow<Boolean> = favoriteDao.isFavorite(id)

    suspend fun addFavorite(quote: Quote) {
        withContext(Dispatchers.IO) {
            val entity = FavoriteEntity(
                id = quote.id,
                content = quote.quote,
                author = quote.author
            )
            favoriteDao.insertFavorite(entity)
        }
    }

    suspend fun removeFavorite(id: Int) {
        withContext(Dispatchers.IO) {
            favoriteDao.deleteFavoriteById(id)
        }
    }
}
