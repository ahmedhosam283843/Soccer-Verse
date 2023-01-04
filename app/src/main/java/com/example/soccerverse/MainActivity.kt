package com.example.soccerverse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.soccerverse.data.remote.SoccerverseApi
import com.example.soccerverse.navigation.NavGraph
import com.example.soccerverse.navigation.Screen
import com.example.soccerverse.repository.SoccerVerseRepository
import com.example.soccerverse.ui.theme.SoccerVerseTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SoccerVerseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavGraph()
                }
            }
        }
    }
}




@Composable
fun SplashScreen(
    navController: NavController
) {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 4000
        )
    )
    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(4000L)
        navController.popBackStack()
        navController.navigate(Screen.Home.route)
    }

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.goal),
            contentDescription = "Splash Logo",
            modifier = Modifier
                .size(200.dp)
                .alpha(alphaAnim.value)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SoccerVerseTheme {
    }
}