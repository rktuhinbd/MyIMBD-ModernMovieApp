package com.rkt.myimbdmodernmovieapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rkt.myimbdmodernmovieapp.ui.theme.MyIMBDModernMovieAppTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyIMBDModernMovieAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        //Redirect to movie info activity
                        startActivity(
                            Intent(
                                this@HomeActivity,
                                MovieInfoActivity::class.java
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(modifier: Modifier, onItemClicked: () -> Unit) {
    Column(modifier = modifier) {
        LazyColumn {
            items(4) {
                MovieListUI() {
                    onItemClicked.invoke()
                }
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(color = Color.LightGray)
                )
            }
        }
    }
}

@Composable
fun MovieListUI(onItemClicked: () -> Unit) {

    var isFavorite by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClicked)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(80.dp),
            painter = painterResource(id = R.drawable.ic_film_reel),
            contentScale = ContentScale.Inside,
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {

            MovieTitle()

            Spacer(modifier = Modifier.height(8.dp))

            MovieDescription()
        }

        Spacer(modifier = Modifier.width(16.dp))

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
}

@Composable
fun MovieTitle() {
    Text(
        text = "The Shawshank Redemption",
        color = Color.Black,
        style = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight(600)
        )
    )
}

@Composable
fun MovieDescription() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = "1992",
            color = Color.Gray,
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight(500)
            )
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "2H 12M",
            color = Color.Gray,
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight(400)
            )
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "Drama, Crime",
            color = Color.Gray,
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight(400)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MovieListItemPreview() {
    MovieListUI() {

    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(modifier = Modifier.fillMaxSize()) {

    }
}
