package mx.utng.smarthealthmonitor.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.darkColorScheme
import mx.utng.smarthealthmonitor.tv.presentation.TvCatalogScreen
import mx.utng.smarthealthmonitor.tv.presentation.TvDetailScreen
import mx.utng.smarthealthmonitor.tv.presentation.TvPlaybackScreen

class TVActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartHealthTvTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "catalog") {
                    composable("catalog") {
                        TvCatalogScreen(onCardClick = { lecturaId ->
                            navController.navigate("detail/$lecturaId")
                        })
                    }
                    composable(
                        route = "detail/{lecturaId}",
                        arguments = listOf(navArgument("lecturaId") { type = NavType.IntType })
                    ) { backStack ->
                        val id = backStack.arguments?.getInt("lecturaId") ?: return@composable
                        TvDetailScreen(lecturaId = id, navController = navController)
                    }
                    composable("playback") {
                        TvPlaybackScreen(navController = navController)
                    }
                }
            }
        }
    }
}

private val TvDarkColorScheme = darkColorScheme(
    primary = Color(0xFF1B5E20),
    secondary = Color(0xFF76FF03),
    background = Color(0xFF0D1B4A),
    onBackground = Color.White
)

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SmartHealthTvTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = TvDarkColorScheme,
        content = content
    )
}
