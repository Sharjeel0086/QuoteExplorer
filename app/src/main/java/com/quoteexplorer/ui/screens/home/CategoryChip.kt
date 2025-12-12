package com.quoteexplorer.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.quoteexplorer.ui.theme.PrimaryYellow
import com.quoteexplorer.ui.theme.QuoteShapes
import com.quoteexplorer.ui.theme.SurfaceWhite

@Composable
fun CategoryChip(
    category: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = QuoteShapes.ChipShape, // Assuming 12dp radius defined in Shape.kt or Theme.kt (User prompt said 12dp chips)
        color = if (isSelected) PrimaryYellow else SurfaceWhite,
        shadowElevation = if (isSelected) 4.dp else 2.dp
    ) {
        Text(
            text = category,
            style = MaterialTheme.typography.labelLarge,
            color = if (isSelected) Color.Black else Color.Gray,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}
