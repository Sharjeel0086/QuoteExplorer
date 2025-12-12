package com.quoteexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.quoteexplorer.ui.navigation.QuoteNavigation
import com.quoteexplorer.ui.theme.QuoteExplorerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuoteExplorerTheme {
                QuoteNavigation()
            }
        }
    }
}
