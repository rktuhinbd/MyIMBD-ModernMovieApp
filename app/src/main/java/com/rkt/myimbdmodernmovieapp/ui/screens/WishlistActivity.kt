package com.rkt.myimbdmodernmovieapp.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rkt.myimbdmodernmovieapp.R
import com.rkt.myimbdmodernmovieapp.base.UIState
import com.rkt.myimbdmodernmovieapp.model.MoviesEntity
import com.rkt.myimbdmodernmovieapp.ui.screens.ui.theme.MyIMBDModernMovieAppTheme
import com.rkt.myimbdmodernmovieapp.ui.viewmodel.ApiUiEvent
import com.rkt.myimbdmodernmovieapp.ui.viewmodel.ViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WishlistActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: ViewModel = hiltViewModel()

            LaunchedEffect(Unit) {
                viewModel.onUiEvent(ApiUiEvent.GetWishlist)
            }

            val wishlistState by viewModel.wishlistObserver.collectAsState()

            MyIMBDModernMovieAppTheme {
                WishlistScreen(
                    state = wishlistState,
                    onBack = { finish() },
                    onToggleFavorite = { movie ->
                        viewModel.onUiEvent(
                            ApiUiEvent.UpdateFavorite(
                                movie.id,
                                !movie.isFavorite
                            )
                        )
                        // Refresh the wishlist
                        viewModel.onUiEvent(ApiUiEvent.GetWishlist)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen(
    state: UIState<List<MoviesEntity>>,
    onBack: () -> Unit,
    onToggleFavorite: (MoviesEntity) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Wishlist") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (state) {
                is UIState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is UIState.Error -> {
                    Text(
                        text = state.error ?: "An error occurred.",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is UIState.Success -> {
                    val wishlist = state.data
                    if (wishlist.isNullOrEmpty()) {
                        Text(
                            text = "Your wishlist is empty.",
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                        ) {
                            items(wishlist) { movie ->
                                WishlistMovieCard(movie, onToggleFavorite)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }

                is UIState.Empty -> {
                    Text(
                        text = "No movies yet.",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}


@Composable
fun WishlistMovieCard(
    movie: MoviesEntity,
    onToggleFavorite: (MoviesEntity) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movie.posterUrl)
                    .crossfade(true)
                    .placeholder(R.drawable.ic_film_reel) // your local placeholder
                    .build(),
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(100.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = movie.genres,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { onToggleFavorite(movie) }) {
                        Icon(
                            painter = painterResource(
                                if (movie.isFavorite) R.drawable.ic_favorite_filled_24
                                else R.drawable.ic_favorite_outlined_24
                            ),
                            contentDescription = "Toggle Favorite",
                            tint = Color(0xFFE53935)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun EmptyWishlist(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.FavoriteBorder,
            contentDescription = null,
            tint = Color.LightGray,
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Your movieList is empty!",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Tap the heart icon on a movie to add it here.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }
}

// Reuse your Coil image loading composable for poster
@Composable
fun MoviePosterImage(posterUrl: String?, modifier: Modifier = Modifier) {
    AsyncImage(
        model = posterUrl,
        placeholder = painterResource(id = R.drawable.ic_film_reel),
        error = painterResource(id = R.drawable.ic_film_reel),
        contentDescription = "Movie Poster",
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyIMBDModernMovieAppTheme {

    }
}