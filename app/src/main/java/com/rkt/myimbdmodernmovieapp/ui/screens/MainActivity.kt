package com.rkt.myimbdmodernmovieapp.ui.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.rkt.myimbdmodernmovieapp.R
import com.rkt.myimbdmodernmovieapp.base.UIState
import com.rkt.myimbdmodernmovieapp.ui.theme.MyIMBDModernMovieAppTheme
import com.rkt.myimbdmodernmovieapp.ui.viewmodel.ApiUiEvent
import com.rkt.myimbdmodernmovieapp.ui.viewmodel.ViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyIMBDModernMovieAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SplashScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        //Redirect to home after splash screen loading ended
                        Log.d("MainActivity", "SplashScreen finished!")

                        startActivity(
                            Intent(
                                this@MainActivity,
                                HomeActivity::class.java
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    viewModel: ViewModel = hiltViewModel(), // <-- Inject ViewModel
    onSplashFinished: () -> Unit
) {
    val state by viewModel.movieListObserver.collectAsState()

    // Trigger API call only once
    LaunchedEffect(Unit) {
        viewModel.onUiEvent(ApiUiEvent.GetMovieList)
    }

    // Navigate after API succeeds
    LaunchedEffect(state) {
        if (state is UIState.Success) {
            delay(1500) // Optional delay for smoothness
            onSplashFinished()
        } else if (state is UIState.Error) {
            Log.e("SplashScreen", "API error: ${(state as UIState.Error)}")
            // You can also navigate or show error UI here
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        LottieAnimation()
    }
}

@Composable
fun LottieAnimation(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.anim_movie_loading))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = true
    )

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier.size(200.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(onSplashFinished = {})
}
