package com.example.gamebrowser.features.games.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gamebrowser.features.games.ui.components.*
import com.example.gamebrowser.features.games.viewmodel.GamesUiState
import com.example.gamebrowser.features.games.viewmodel.GamesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesScreen(
    onGameClick: (Int) -> Unit,
    onCategoryClick: (String, List<com.example.gamebrowser.data.model.dto.GameDto>) -> Unit = { _, _ -> },
    viewModel: GamesViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Game Store",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        when (state) {
            is GamesUiState.Loading -> {
                LoadingState(paddingValues)
            }

            is GamesUiState.Success -> {
                val successState = state as GamesUiState.Success
                SuccessContent(
                    paddingValues = paddingValues,
                    searchQuery = successState.searchQuery,
                    onSearchQueryChanged = viewModel::onSearchQueryChanged,
                    genres = successState.genres,
                    selectedGenreId = successState.selectedGenreId,
                    onGenreSelected = viewModel::onGenreSelected,
                    featuredGames = successState.featuredGames,
                    newReleases = successState.newReleases,
                    popularGames = successState.popular,
                    topRated = successState.topRated,
                    allGames = successState.filteredGames,
                    isEmpty = successState.isEmpty,
                    isLoadingMore = successState.isLoadingMore,
                    hasMorePages = successState.hasMorePages,
                    onLoadMore = viewModel::loadNextPage,
                    onGameClick = onGameClick,
                    onCategoryClick = onCategoryClick
                )
            }

            is GamesUiState.Error -> {
                ErrorState(
                    paddingValues = paddingValues,
                    message = (state as GamesUiState.Error).message,
                    onRetry = { viewModel.retry() }
                )
            }
        }
    }
}

@Composable
private fun LoadingState(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = "Loading games...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SuccessContent(
    paddingValues: PaddingValues,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    genres: List<com.example.gamebrowser.data.model.dto.GenreDto>,
    selectedGenreId: String?,
    onGenreSelected: (String?) -> Unit,
    featuredGames: List<com.example.gamebrowser.data.model.dto.GameDto>,
    newReleases: List<com.example.gamebrowser.data.model.dto.GameDto>,
    popularGames: List<com.example.gamebrowser.data.model.dto.GameDto>,
    topRated: List<com.example.gamebrowser.data.model.dto.GameDto>,
    allGames: List<com.example.gamebrowser.data.model.dto.GameDto>,
    isEmpty: Boolean,
    isLoadingMore: Boolean,
    hasMorePages: Boolean,
    onLoadMore: () -> Unit,
    onGameClick: (Int) -> Unit,
    onCategoryClick: (String, List<com.example.gamebrowser.data.model.dto.GameDto>) -> Unit
) {
    val listState = rememberLazyListState()


    LaunchedEffect(listState) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleItem >= totalItems - 3
        }.collect { shouldLoadMore ->
            if (shouldLoadMore && hasMorePages && !isLoadingMore) {
                onLoadMore()
            }
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Hero Section
        item {
            HeroSection()
        }

        // Search Bar
        item {
            SearchBar(
                query = searchQuery,
                onQueryChange = onSearchQueryChanged
            )
        }

        // Genre Filter Chips
        item {
            GenreFilterRow(
                genres = genres,
                selectedGenreId = selectedGenreId,
                onGenreSelected = onGenreSelected
            )
        }

        // Show empty state if no results
        if (isEmpty) {
            item {
                EmptyState(searchQuery = searchQuery)
            }
        } else {
            // Featured Games
            if (featuredGames.isNotEmpty()) {
                item {
                    FeaturedSection(
                        title = "Featured Games",
                        games = featuredGames,
                        onGameClick = { game -> onGameClick(game.id) }
                    )
                }
            }

            // New Releases
            if (newReleases.isNotEmpty()) {
                item {
                    GameSection(
                        title = "Brand new adventures",
                        games = newReleases,
                        onSeeAllClick = {
                            onCategoryClick("Brand new adventures", newReleases)
                        },
                        onGameClick = { game -> onGameClick(game.id) }
                    )
                }
            }

            // Most Popular
            if (popularGames.isNotEmpty()) {
                item {
                    PopularGamesSection(
                        title = "Most popular",
                        games = popularGames,
                        onGameClick = { game -> onGameClick(game.id) }
                    )
                }
            }

            // Top Rated
            if (topRated.isNotEmpty()) {
                item {
                    GameSection(
                        title = "Top rated games",
                        games = topRated,
                        onSeeAllClick = {
                            onCategoryClick("Top rated games", topRated)
                        },
                        onGameClick = { game -> onGameClick(game.id) }
                    )
                }
            }

            // All Games
            item {
                GameSection(
                    title = "Explore all games",
                    games = allGames,
                    showSeeAll = false,
                    onGameClick = { game -> onGameClick(game.id) }
                )
            }
        }

        // Loading More Indicator
        if (isLoadingMore) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Loading more games...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Bottom spacing
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ErrorState(
    paddingValues: PaddingValues,
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "😕",
                style = MaterialTheme.typography.displayMedium
            )
            Text(
                text = "Oops! Something went wrong",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Try Again")
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        placeholder = { Text("Search games...") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search"
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear"
                    )
                }
            }
        },
        singleLine = true,
        shape = MaterialTheme.shapes.large,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
        )
    )
}

@Composable
private fun HeroSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Find your perfect",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "game",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Discover thousands of games across all genres",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun GenreFilterRow(
    genres: List<com.example.gamebrowser.data.model.dto.GenreDto>,
    selectedGenreId: String?,
    onGenreSelected: (String?) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // All Games chip
        item {
            CategoryChip(
                label = "All Games",
                selected = selectedGenreId == null,
                onClick = { onGenreSelected(null) }
            )
        }

        // Genre chips
        items(genres) { genre ->
            CategoryChip(
                label = genre.name,
                selected = selectedGenreId == genre.id.toString(),
                onClick = { onGenreSelected(genre.id.toString()) }
            )
        }
    }
}

@Composable
private fun EmptyState(searchQuery: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "🔍",
                style = MaterialTheme.typography.displayLarge
            )
            Text(
                text = "No games found",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            if (searchQuery.isNotEmpty()) {
                Text(
                    text = "Try searching for \"${searchQuery.take(20)}${if(searchQuery.length > 20) "..." else ""}\"",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Try different keywords or browse all games",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            } else {
                Text(
                    text = "No games available in this category",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}