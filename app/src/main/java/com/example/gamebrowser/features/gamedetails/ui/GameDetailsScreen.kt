package com.example.gamebrowser.features.gamedetails.ui

import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.gamebrowser.features.gamedetails.viewmodel.GameDetailsUiState
import com.example.gamebrowser.features.gamedetails.viewmodel.GameDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailsScreen(
    onBackClick: () -> Unit,
    viewModel: GameDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Game Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        when (state) {
            is GameDetailsUiState.Loading -> {
                LoadingState(paddingValues)
            }

            is GameDetailsUiState.Success -> {
                val successState = state as GameDetailsUiState.Success
                SuccessContent(
                    paddingValues = paddingValues,
                    gameName = successState.game.name,
                    imageUrl = successState.game.imageUrl,
                    rating = successState.game.rating,
                    releaseDate = successState.releaseDateFormatted,
                    description = successState.game.descriptionRaw ?: successState.game.description,
                    genres = successState.genresText,
                    platforms = successState.platformsText,
                    metacriticScore = successState.game.metacriticScore,
                    screenshots = successState.screenshots,
                    trailerUrl = successState.trailerUrl
                )
            }

            is GameDetailsUiState.Error -> {
                ErrorState(
                    paddingValues = paddingValues,
                    message = (state as GameDetailsUiState.Error).message,
                    onRetry = { viewModel.retry() },
                    onBack = onBackClick
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
    gameName: String,
    imageUrl: String?,
    rating: Double,
    releaseDate: String,
    description: String?,
    genres: String,
    platforms: String,
    metacriticScore: Int?,
    screenshots: List<String>,
    trailerUrl: String?
) {
    var selectedImage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
    ) {
        // Hero Image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = gameName,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            ),
                            startY = 200f
                        )
                    )
            )

            // Title overlay
            Text(
                text = gameName,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            )
        }

        // Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatCard(
                    label = "Rating",
                    value = "⭐ ${String.format("%.1f", rating)}",
                    modifier = Modifier.weight(1f)
                )

                if (metacriticScore != null && metacriticScore > 0) {
                    StatCard(
                        label = "Metacritic",
                        value = metacriticScore.toString(),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Trailer Section - Auto-plays if trailer exists
            trailerUrl?.let { url ->
                TrailerSection(trailerUrl = url)
            }

            // Screenshots Section
            if (screenshots.isNotEmpty()) {
                ScreenshotsSection(
                    screenshots = screenshots,
                    onScreenshotClick = { selectedImage = it }
                )
            } else {
                InfoCard(
                    title = "Screenshots",
                    content = "No screenshots available for this game"
                )
            }

            // Release Date
            InfoSection(
                title = "Release Date",
                content = releaseDate
            )

            // Genres
            InfoSection(
                title = "Genres",
                content = genres
            )

            // Platforms
            InfoSection(
                title = "Platforms",
                content = platforms
            )

            // Description
            if (!description.isNullOrBlank()) {
                InfoSection(
                    title = "About",
                    content = description
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Image viewer dialog
    selectedImage?.let { imageUrl ->
        Dialog(onDismissRequest = { selectedImage = null }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Screenshot",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedImage = null },
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

@Composable
private fun TrailerSection(trailerUrl: String) {
    var showVideo by remember { mutableStateOf(true) }
    var isPlaying by remember { mutableStateOf(true) }
    var videoView by remember { mutableStateOf<VideoView?>(null) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Trailer",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        if (showVideo) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                AndroidView(
                    factory = { context ->
                        VideoView(context).apply {
                            setVideoURI(Uri.parse(trailerUrl))
                            setOnPreparedListener { mp ->
                                mp.isLooping = true // Loop the video like YouTube
                                mp.start() // Auto-play
                                isPlaying = true
                            }
                            setOnErrorListener { _, _, _ ->
                                showVideo = false
                                true
                            }
                            videoView = this
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                // Transparent clickable overlay to capture taps
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            // Toggle play/pause on click
                            videoView?.let { vv ->
                                if (isPlaying) {
                                    vv.pause()
                                    isPlaying = false
                                } else {
                                    vv.start()
                                    isPlaying = true
                                }
                            }
                        }
                )

                // Show play icon overlay when paused
                if (!isPlaying) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            modifier = Modifier
                                .size(64.dp)
                                .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                .padding(12.dp),
                            tint = Color.White
                        )
                    }
                }

                // Close button overlay in top-right corner
                IconButton(
                    onClick = {
                        videoView?.stopPlayback()
                        showVideo = false
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close trailer",
                        tint = Color.White,
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                            .padding(6.dp)
                    )
                }
            }
        } else {
            // Show play button if user closed the video
            PlayButtonCard(
                onClick = {
                    showVideo = true
                    isPlaying = true
                }
            )
        }
    }
}

@Composable
private fun PlayButtonCard(
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play trailer",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Tap to watch trailer",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun InfoCard(
    title: String,
    content: String
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun ScreenshotsSection(
    screenshots: List<String>,
    onScreenshotClick: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Screenshots (${screenshots.size})",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(screenshots) { screenshot ->
                Card(
                    modifier = Modifier
                        .width(200.dp)
                        .height(120.dp)
                        .clickable { onScreenshotClick(screenshot) },
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    )
                ) {
                    AsyncImage(
                        model = screenshot,
                        contentDescription = "Screenshot",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun InfoSection(
    title: String,
    content: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = content,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ErrorState(
    paddingValues: PaddingValues,
    message: String,
    onRetry: () -> Unit,
    onBack: () -> Unit
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
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(onClick = onBack) {
                    Text("Go Back")
                }
                Button(onClick = onRetry) {
                    Text("Try Again")
                }
            }
        }
    }
}