package com.rkt.myimbdmodernmovieapp.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rkt.myimbdmodernmovieapp.R
import com.rkt.myimbdmodernmovieapp.model.MoviesEntity
import com.rkt.myimbdmodernmovieapp.ui.animation.FlyToWishlistState
import com.rkt.myimbdmodernmovieapp.ui.animation.flyToWishlistSource
import com.rkt.myimbdmodernmovieapp.ui.animation.flyToWishlistTarget
import com.rkt.myimbdmodernmovieapp.ui.theme.MyIMBDModernMovieAppTheme
import com.rkt.myimbdmodernmovieapp.ui.viewmodel.ApiUiEvent
import com.rkt.myimbdmodernmovieapp.ui.viewmodel.ViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            val flyToWishlistState = remember { FlyToWishlistState() }

            MyIMBDModernMovieAppTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("MyIMBD") },
                            actions = {
                                Icon(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .flyToWishlistTarget(flyToWishlistState),
                                    painter = painterResource(R.drawable.ic_favorite_filled_24),
                                    contentDescription = "Wishlist"
                                )
                            }
                        )
                    }
                ) { innerPadding ->
                    HomeScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        onItemClicked = {
                            val intent = Intent(this, MovieInfoActivity::class.java)
                            intent.putExtra("movie", it) // movie is MoviesEntity
                            startActivity(intent)
                        },
                        flyToWishlistState = flyToWishlistState
                    )
                }
            }
        }
    }
}

@Composable
fun FlyToWishlistAnimation(flyState: FlyToWishlistState) {
    val duration = 600
    val animatableOffset = remember { Animatable(Offset.Zero, Offset.VectorConverter) }

    if (flyState.isAnimating && flyState.startOffset != null && flyState.endOffset != null) {
        LaunchedEffect(Unit) {
            animatableOffset.snapTo(flyState.startOffset!!)
            animatableOffset.animateTo(
                targetValue = flyState.endOffset!!,
                animationSpec = tween(durationMillis = duration, easing = FastOutSlowInEasing)
            )
            flyState.isAnimating = false
        }

        Box(
            Modifier
                .fillMaxSize()
                .zIndex(100f) // Bring above all content
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_favorite_filled_24),
                contentDescription = null,
                modifier = Modifier
                    .offset {
                        IntOffset(
                            animatableOffset.value.x.roundToInt(),
                            animatableOffset.value.y.roundToInt()
                        )
                    }
                    .size(24.dp),
                tint = Color.Red
            )
        }
    }
}


@Composable
fun HomeScreen(
    modifier: Modifier,
    viewModel: ViewModel = hiltViewModel(), // <-- Inject ViewModel
    flyToWishlistState: FlyToWishlistState,
    onItemClicked: (MoviesEntity) -> Unit
) {

    LaunchedEffect(key1 = Unit) {
        viewModel.onUiEvent(event = ApiUiEvent.GetMovieList)
    }

    val movieListState by viewModel.movieListObserver.collectAsStateWithLifecycle()

    var searchQuery by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf("All") }
    var viewAsGrid by remember { mutableStateOf(true) }
    var sortAscending by remember { mutableStateOf(true) }

    val genres = movieListState.data?.genres ?: emptyList()
    val allMovies = movieListState.data?.movies ?: emptyList()

    val filteredMovies = allMovies
        .filter {
            // Convert comma-separated genres to a list and trim whitespace
            val genreList = it.genres.split(",").map { genre -> genre.trim() }

            (selectedGenre == "All" || genreList.any { genre ->
                genre.equals(selectedGenre, ignoreCase = true)
            }) && it.title.contains(searchQuery, ignoreCase = true)
        }
        .sortedBy {
            if (sortAscending) it.year.toInt() else -it.year.toInt()
        }

    Box(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {

            // 🔍 Search
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search movies...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
            )

            // 🧰 Filter & Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Genre Dropdown
                var expanded by remember { mutableStateOf(false) }

                Box {
                    OutlinedButton(onClick = { expanded = true }) {
                        Text(text = selectedGenre)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }

                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        genres.forEach { genre ->
                            DropdownMenuItem(
                                text = { Text(genre.genre) },
                                onClick = {
                                    selectedGenre = genre.genre
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                ViewToggleSwitch(
                    viewAsGrid = viewAsGrid,
                    onToggle = { viewAsGrid = it }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 🎬 Display Movies as List or Grid
            if (viewAsGrid) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(filteredMovies) { movie ->
                        MovieGridUI(
                            movie,
                            flyToWishlistState = flyToWishlistState,
                            onItemClicked = onItemClicked,
                            onFavoriteClicked = {
                                viewModel.onUiEvent(
                                    ApiUiEvent.UpdateFavorite(
                                        movie.id,
                                        !movie.isFavorite
                                    )
                                )
                            })
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(filteredMovies) { movie ->
                        MovieListUI(
                            movie,
                            flyToWishlistState = flyToWishlistState,
                            onItemClicked,
                            onFavoriteClicked = {
                                viewModel.onUiEvent(
                                    ApiUiEvent.UpdateFavorite(
                                        movie.id,
                                        !movie.isFavorite
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }

        FlyToWishlistAnimation(flyToWishlistState)
    }
}

@Composable
fun ViewToggleSwitch(
    viewAsGrid: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .wrapContentWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(50))
    ) {
        val buttonModifier = Modifier.clip(RoundedCornerShape(50))

        Button(
            onClick = { onToggle(false) },
            modifier = buttonModifier,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (!viewAsGrid) MaterialTheme.colorScheme.primary else Color.Transparent,
                contentColor = if (!viewAsGrid) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            ),
            elevation = null
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(R.drawable.ic_list_view),
                contentDescription = "List View"
            )
        }

        Button(
            onClick = { onToggle(true) },
            modifier = buttonModifier,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (viewAsGrid) MaterialTheme.colorScheme.primary else Color.Transparent,
                contentColor = if (viewAsGrid) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            ),
            elevation = null
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(R.drawable.ic_grid_view),
                contentDescription = "Grid View"
            )
        }
    }
}


@Composable
fun MovieListUI(
    movie: MoviesEntity,
    flyToWishlistState: FlyToWishlistState,
    onItemClicked: (MoviesEntity) -> Unit,
    onFavoriteClicked: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    onItemClicked.invoke(movie)
                }
            )
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

        Column(modifier = Modifier.weight(1f)) {
            Text(text = movie.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            MovieDescription(movie)
        }

        Spacer(modifier = Modifier.width(16.dp))

        IconButton(
            onClick = {
                flyToWishlistState.isAnimating = true
                onFavoriteClicked()
            },
            modifier = Modifier
                .flyToWishlistSource(flyToWishlistState)
        ) {
            Icon(
                painter = painterResource(
                    if (movie.isFavorite) R.drawable.ic_favorite_filled_24
                    else R.drawable.ic_favorite_outlined_24
                ),
                tint = Color(0xFFE53935),
                contentDescription = null
            )
        }
    }
}


@Composable
fun MovieGridUI(
    movie: MoviesEntity,
    flyToWishlistState: FlyToWishlistState,
    onItemClicked: (MoviesEntity) -> Unit,
    onFavoriteClicked: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    onItemClicked.invoke(movie)
                }
            )
            .padding(4.dp),
        elevation = CardDefaults.cardElevation()
    ) {

        Box(
            modifier = Modifier.padding(8.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_film_reel),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(bottom = 8.dp),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = movie.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    maxLines = 2,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                MovieDescription(movie)
            }

            IconButton(
                onClick = {
                    flyToWishlistState.isAnimating = true
                    onFavoriteClicked()
                },
                modifier = Modifier
                    .flyToWishlistSource(flyToWishlistState)
            ) {
                Icon(
                    painter = painterResource(
                        if (movie.isFavorite) R.drawable.ic_favorite_filled_24
                        else R.drawable.ic_favorite_outlined_24
                    ),
                    tint = Color(0xFFE53935),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun MovieDescription(movie: MoviesEntity) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(movie.year, fontSize = 12.sp, color = Color.Gray)
        Text(movie.runtime, fontSize = 12.sp, color = Color.Gray)
        Text(movie.genres, fontSize = 12.sp, color = Color.Gray)
    }
}


/*
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(modifier = Modifier.fillMaxSize(), onItemClicked = {})
}*/
