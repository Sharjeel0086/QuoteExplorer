package com.quoteexplorer.data.api

import com.quoteexplorer.model.QuoteResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface QuotableApi {
    @GET("quotes")
    suspend fun getQuotes(
        @Query("skip") skip: Int = 0,
        @Query("limit") limit: Int = 20
    ): QuoteResponse

    @GET("quotes/{id}")
    suspend fun getQuote(@retrofit2.http.Path("id") id: Int): com.quoteexplorer.model.Quote
}
