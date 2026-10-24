package com.ihegyi.alexiguitartuner

import android.os.Bundle
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.ihegyi.alexiguitartuner.R.id
import com.ihegyi.alexiguitartuner.commons.presentation.ui.AlexiApp
import com.ihegyi.alexiguitartuner.commons.presentation.ui.AlexiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var navigationRequest by mutableStateOf<Pair<Int, String>?>(null)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        handleNavigationIntent(intent)
        setContent { AlexiTheme { AlexiApp(navigationRequest) } }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleNavigationIntent(intent)
    }

    private fun handleNavigationIntent(intent: Intent) {
        if (intent.getIntArrayExtra("android-support-nav:controller:deepLinkIds")?.contains(id.navigation_metronome) == true) {
            navigationRequest = Pair((navigationRequest?.first ?: 0) + 1, "metronome")
        }
    }
}
