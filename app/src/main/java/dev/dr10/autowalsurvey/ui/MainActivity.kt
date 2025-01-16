package dev.dr10.autowalsurvey.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import dagger.hilt.android.AndroidEntryPoint
import dev.dr10.autowalsurvey.ui.screen.MainScreen
import dev.dr10.autowalsurvey.ui.theme.AutoWalSurveyTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color(0xFF0B0B0B).toArgb()),
            navigationBarStyle = SystemBarStyle.dark(Color(0xFF0B0B0B).toArgb())
        )
        setContent {
            AutoWalSurveyTheme {
                MainScreen()
            }
        }
    }
}