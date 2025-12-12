package com.quoteexplorer.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val id: Int,
    val content: String,
    val author: String,
    val timestamp: Long = System.currentTimeMillis()
)
