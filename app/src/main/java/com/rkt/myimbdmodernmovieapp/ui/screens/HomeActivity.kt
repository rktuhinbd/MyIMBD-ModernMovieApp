package com.rkt.myimbdmodernmovieapp.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rkt.myimbdmodernmovieapp.R
import com.rkt.myimbdmodernmovieapp.base.UIState
import com.rkt.myimbdmodernmovieapp.model.MoviesEntity
import com.rkt.myimbdmodernmovieapp.ui.theme.MyIMBDModernMovieAppTheme
import com.rkt.myimbdmodernmovieapp.ui.viewmodel.ApiUiEvent
import com.rkt.myimbdmodernmovieapp.ui.viewmodel.ViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
fun HomeScreen(
    modifier: Modifier,
    viewModel: ViewModel = hiltViewModel(), // <-- Inject ViewModel,
    onItemClicked: () -> Unit
) {

    LaunchedEffect(key1 = Unit) {
        viewModel.onUiEvent(event = ApiUiEvent.GetMovieList)
    }

    val movieListState by viewModel.movieListObserver.collectAsStateWithLifecycle()

    var searchQuery by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf("All") }
    var viewAsGrid by remember { mutableStateOf(true) }
    var sortAscending by remember { mutableStateOf(true) }

    var genres = remember { movieListState.data?.genres ?: emptyList() }
    var allMovies = remember { movieListState.data?.movies ?: emptyList() }

    when (movieListState) {
        is UIState.Loading -> {

        }

        is UIState.Success -> {
            genres = remember { movieListState.data?.genres ?: emptyList() }
            allMovies = remember { movieListState.data?.movies ?: emptyList() }
        }

        is UIState.Error -> {

        }

        else -> {

        }
    }

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

    Column(modifier = modifier.padding(16.dp)) {

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
                    MovieGridItem(movie, onItemClicked)
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filteredMovies) { movie ->
                    MovieListUI(movie, onItemClicked)
                }
            }
        }
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
fun MovieListUI(movie: MoviesEntity, onItemClicked: () -> Unit) {
    var isFavorite by remember { mutableStateOf(movie.isFavorite) }

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

        Column(modifier = Modifier.weight(1f)) {
            Text(text = movie.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            MovieDescription(movie)
        }

        Spacer(modifier = Modifier.width(16.dp))

        IconButton(onClick = { isFavorite = !isFavorite }) {
            Icon(
                painter = painterResource(
                    if (isFavorite) R.drawable.ic_favorite_filled_24
                    else R.drawable.ic_favorite_outlined_24
                ),
                tint = Color(0xFFE53935),
                contentDescription = null
            )
        }
    }
}


@Composable
fun MovieGridItem(movie: MoviesEntity, onItemClicked: () -> Unit) {
    var isFavorite by remember { mutableStateOf(movie.isFavorite) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClicked() }
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
                onClick = { isFavorite = !isFavorite },
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isFavorite) R.drawable.ic_favorite_filled_24
                        else R.drawable.ic_favorite_outlined_24
                    ),
                    tint = Color(0xFFE53935),
                    contentDescription = "Favorite"
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


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(modifier = Modifier.fillMaxSize()) {

    }
}