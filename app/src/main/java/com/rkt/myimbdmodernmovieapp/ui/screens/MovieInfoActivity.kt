package com.rkt.myimbdmodernmovieapp.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rkt.myimbdmodernmovieapp.R
import com.rkt.myimbdmodernmovieapp.ui.theme.MyIMBDModernMovieAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieInfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyIMBDModernMovieAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MovieInfoScreen(
                        name = "Android",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        finish()
                    }
                }
            }
        }
    }
}

@Composable
fun MovieInfoScreen(name: String, modifier: Modifier = Modifier, onBackClick: () -> Unit) {

    var isFavorite by remember {
        mutableStateOf(false)
    }

    Column(modifier = modifier) {

        IconButton(
            onClick = onBackClick,
            modifier = Modifier.size(48.dp) // default Material size
        ) {
            Icon(
                imageVector = Icons.Outlined.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(0.5f),
                text = "$name",
                style = MaterialTheme.typography.headlineLarge
            )

            IconButton(
                onClick = {
                    isFavorite = !isFavorite
                },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    painter = painterResource(
                        if (isFavorite)
                            R.drawable.ic_favorite_filled_24
                        else
                            R.drawable.ic_favorite_outlined_24
                    ),
                    tint = Color(0xFFE53935),
                    contentDescription = null
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "1992 | Drama, Crime | 142 min",
            style = TextStyle(
                fontSize = 16.sp,
                color = Color.DarkGray,
                fontWeight = FontWeight(400)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp),
            painter = painterResource(R.drawable.ic_film_reel),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
            style = TextStyle(
                fontSize = 16.sp,
                color = Color.DarkGray,
                fontWeight = FontWeight(400)
            )
        )

        Spacer(
            modifier = Modifier
                .padding(16.dp)
                .height(1.dp)
                .fillMaxWidth()
                .background(Color.LightGray)
        )

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.DarkGray, fontWeight = FontWeight(500))) {
                    append("Director: ")
                }
                withStyle(style = SpanStyle(color = Color.Blue)) {
                    append("Frank Darabont")
                }
            },
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(
            modifier = Modifier
                .padding(16.dp)
                .height(1.dp)
                .fillMaxWidth()
                .background(Color.LightGray)
        )

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.DarkGray, fontWeight = FontWeight(500))) {
                    append("Casts: ")
                }
                withStyle(style = SpanStyle(color = Color.Blue, fontSize = 18.sp)) {
                    append("Tim Robbins, Morgan Freeman, Bob Gunton, William Sadler")
                }
            },
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MovieInfoScreenPreview() {
    MyIMBDModernMovieAppTheme {
        MovieInfoScreen(
            name = "Shawshank Redemption",
            modifier = Modifier.fillMaxSize()
        ) {

        }
    }
}