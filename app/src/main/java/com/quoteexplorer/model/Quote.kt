package com.quoteexplorer.model

import com.google.gson.annotations.SerializedName

data class Quote(
    val id: Int,
    val quote: String,
    val author: String
)

data class QuoteResponse(
    val quotes: List<Quote>,
    val total: Int,
    val skip: Int,
    val limit: Int
)
