package com.quoteexplorer.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Shape system matching the design
val QuoteExplorerShapes = Shapes(
    // Small chips / icons - 12dp
    small = RoundedCornerShape(12.dp),
    
    // Medium cards - 16dp
    medium = RoundedCornerShape(16.dp),
    
    // Hero / large cards - 28dp
    large = RoundedCornerShape(28.dp),
    
    // Extra large for bottom sheets etc
    extraLarge = RoundedCornerShape(32.dp)
)

// Custom shape constants for more specific use
object QuoteShapes {
    val HeroCard = RoundedCornerShape(28.dp)
    val QuoteCard = RoundedCornerShape(16.dp)
    val ChipShape = RoundedCornerShape(12.dp)
    val FavoriteButton = RoundedCornerShape(50) // Circular
    val SearchBar = RoundedCornerShape(16.dp)
    val FloatingActionCard = RoundedCornerShape(20.dp)
    val BottomSheet = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
}

// Spacing constants matching the design
object QuoteSpacing {
    val ScreenPadding = 20.dp
    val ItemGap = 12.dp
    val SectionGap = 24.dp
    val CardPadding = 16.dp
    val SmallPadding = 8.dp
    val TinyPadding = 4.dp
}

// Elevation constants
object QuoteElevation {
    val CardElevation = 8.dp
    val SmallCardElevation = 4.dp
    val FloatingActionElevation = 12.dp
    val FavoriteButtonElevation = 4.dp
}

// Size constants
object QuoteSizes {
    val MinTouchTarget = 48.dp
    val FavoriteButtonSize = 36.dp
    val IconSize = 24.dp
    val SmallIconSize = 20.dp
    val HeroHeight = 200.dp
    val CardMinHeight = 120.dp
    val ChipHeight = 32.dp
}
