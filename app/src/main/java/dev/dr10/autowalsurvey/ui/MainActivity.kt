package dev.dr10.autowalsurvey.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import dev.dr10.autowalsurvey.ui.screen.MainScreen
import dev.dr10.autowalsurvey.ui.theme.AppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(AppTheme.colors.background.toArgb()),
            navigationBarStyle = SystemBarStyle.dark(AppTheme.colors.background.toArgb())
        )
        setContent {
            splashScreen.setKeepOnScreenCondition { false }
            MainScreen()
        }
    }
}