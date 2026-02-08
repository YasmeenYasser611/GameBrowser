package com.example.gamebrowser.features.games.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
    viewModel: GamesViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var selectedCategory by remember { mutableStateOf("All Games") }

    val categories = listOf(
        "All Games",
        "Popular picks",
        "Epic adventures",
        "Realistic sports",
        "New releases",
        "Top rated"
    )

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
                    categories = categories,
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it },
                    featuredGames = successState.featuredGames,
                    newReleases = successState.newReleases,
                    popularGames = successState.popular,
                    topRated = successState.topRated,
                    allGames = successState.allGames
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
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun SuccessContent(
    paddingValues: PaddingValues,
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    featuredGames: List<com.example.gamebrowser.data.model.GameDto>,
    newReleases: List<com.example.gamebrowser.data.model.GameDto>,
    popularGames: List<com.example.gamebrowser.data.model.GameDto>,
    topRated: List<com.example.gamebrowser.data.model.GameDto>,
    allGames: List<com.example.gamebrowser.data.model.GameDto>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Hero Section
        item {
            HeroSection()
        }

        // Category Chips
        item {
            CategoryFilterRow(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = onCategorySelected
            )
        }

        // Featured Games
        if (featuredGames.isNotEmpty()) {
            item {
                FeaturedSection(
                    title = "Featured Games",
                    games = featuredGames,
                    onGameClick = { /* TODO: Navigate to details */ }
                )
            }
        }

        // New Releases
        if (newReleases.isNotEmpty()) {
            item {
                GameSection(
                    title = "Brand new adventures",
                    games = newReleases,
                    onSeeAllClick = { /* TODO: Navigate to new releases */ },
                    onGameClick = { /* TODO: Navigate to details */ }
                )
            }
        }

        // Most Popular
        if (popularGames.isNotEmpty()) {
            item {
                PopularGamesSection(
                    title = "Most popular",
                    games = popularGames,
                    onGameClick = { /* TODO: Navigate to details */ }
                )
            }
        }

        // Top Rated
        if (topRated.isNotEmpty()) {
            item {
                GameSection(
                    title = "Top rated games",
                    games = topRated,
                    onSeeAllClick = { /* TODO: Navigate to top rated */ },
                    onGameClick = { /* TODO: Navigate to details */ }
                )
            }
        }

        // All Games
        item {
            GameSection(
                title = "Explore all games",
                games = allGames,
                showSeeAll = false,
                onGameClick = { /* TODO: Navigate to details */ }
            )
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
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
private fun CategoryFilterRow(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories.size) { index ->
            CategoryChip(
                label = categories[index],
                selected = selectedCategory == categories[index],
                onClick = { onCategorySelected(categories[index]) }
            )
        }
    }
}