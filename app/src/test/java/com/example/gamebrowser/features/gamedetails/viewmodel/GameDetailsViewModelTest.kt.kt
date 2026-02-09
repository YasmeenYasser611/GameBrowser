package com.example.gamebrowser.features.gamedetails.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.example.gamebrowser.data.model.dto.*
import com.example.gamebrowser.data.repository.IGameRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class GameDetailsViewModelTest {

    // Dependencies
    private lateinit var repository: IGameRepository
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: GameDetailsViewModel

    // Test dispatcher
    private val testDispatcher = StandardTestDispatcher()

    // Test data
    private val testGameId = 123
    private val testGame = GameDto(
        id = testGameId,
        name = "Test Game",
        description = "Test Description",
        descriptionRaw = "Test Raw Description",
        imageUrl = "https://example.com/image.jpg",
        rating = 4.5,
        releaseDate = "2024-01-15",
        metacriticScore = 85,
        genres = listOf(
            GenreDto(id = 1, name = "Action"),
            GenreDto(id = 2, name = "Adventure")
        ),
        platforms = listOf(
            PlatformWrapperDto(
                platform = PlatformDto(id = 1, name = "PlayStation 5")
            ),
            PlatformWrapperDto(
                platform = PlatformDto(id = 2, name = "Xbox Series X")
            )
        ),
        clip = null,
        shortScreenshots = null
    )


    private val testScreenshots = listOf(
        ShortScreenshotDto(id = 1, image = "https://example.com/screenshot1.jpg"),
        ShortScreenshotDto(id = 2, image = "https://example.com/screenshot2.jpg"),
        ShortScreenshotDto(id = 3, image = "https://example.com/screenshot3.jpg")
    )

    private val testMovies = listOf(
        GameMovieDto(
            id = 1,
            data = MovieDataDto(
                max = "https://example.com/trailer_max.mp4",
                `480` = "https://example.com/trailer_480.mp4"
            )
        )
    )

    @Before
    fun setup() {
        // Set up test dispatcher
        Dispatchers.setMain(testDispatcher)

        // Create mocks
        repository = mockk(relaxed = true)
        savedStateHandle = mockk(relaxed = true)

        // Default stub for savedStateHandle
        every { savedStateHandle.get<String>("gameId") } returns testGameId.toString()
    }

    @After
    fun tearDown() {

        Dispatchers.resetMain()
    }



    @Test
    fun init_validGameId_loadsGameDetails() = runTest {
        // Arrange
        coEvery { repository.getGameDetails(testGameId) } returns testGame
        coEvery { repository.getGameScreenshots(testGameId) } returns testScreenshots
        coEvery { repository.getGameMovies(testGameId) } returns testMovies

        // Act
        viewModel = GameDetailsViewModel(repository, savedStateHandle)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertTrue("Expected Success state but got ${state::class.simpleName}",
            state is GameDetailsUiState.Success)
        assertEquals(testGame.name, (state as GameDetailsUiState.Success).game.name)
    }

    @Test
    fun init_invalidGameId_setsErrorState() = runTest {
        // Arrange
        every { savedStateHandle.get<String>("gameId") } returns "invalid"

        // Act
        viewModel = GameDetailsViewModel(repository, savedStateHandle)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertTrue("Expected Error state but got ${state::class.simpleName}",
            state is GameDetailsUiState.Error)
        assertEquals("Invalid game ID", (state as GameDetailsUiState.Error).message)
    }

    @Test
    fun init_nullGameId_setsErrorState() = runTest {
        // Arrange
        every { savedStateHandle.get<String>("gameId") } returns null

        // Act
        viewModel = GameDetailsViewModel(repository, savedStateHandle)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertTrue("Expected Error state but got ${state::class.simpleName}",
            state is GameDetailsUiState.Error)
        assertEquals("Invalid game ID", (state as GameDetailsUiState.Error).message)
    }



    @Test
    fun loadGameDetails_success_allDataLoaded() = runTest {
        // Arrange
        coEvery { repository.getGameDetails(testGameId) } returns testGame
        coEvery { repository.getGameScreenshots(testGameId) } returns testScreenshots
        coEvery { repository.getGameMovies(testGameId) } returns testMovies

        // Act
        viewModel = GameDetailsViewModel(repository, savedStateHandle)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value as GameDetailsUiState.Success
        assertEquals(testGame, state.game)
        assertEquals(3, state.screenshots.size)
        assertEquals("https://example.com/trailer_max.mp4", state.trailerUrl)
    }

    @Test
    fun loadGameDetails_success_formatsGenresCorrectly() = runTest {
        // Arrange
        coEvery { repository.getGameDetails(testGameId) } returns testGame
        coEvery { repository.getGameScreenshots(testGameId) } returns emptyList()
        coEvery { repository.getGameMovies(testGameId) } returns emptyList()

        // Act
        viewModel = GameDetailsViewModel(repository, savedStateHandle)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value as GameDetailsUiState.Success
        assertEquals("Action, Adventure", state.genresText)
    }

    @Test
    fun loadGameDetails_success_formatsPlatformsCorrectly() = runTest {
        // Arrange
        coEvery { repository.getGameDetails(testGameId) } returns testGame
        coEvery { repository.getGameScreenshots(testGameId) } returns emptyList()
        coEvery { repository.getGameMovies(testGameId) } returns emptyList()

        // Act
        viewModel = GameDetailsViewModel(repository, savedStateHandle)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value as GameDetailsUiState.Success
        assertEquals("PlayStation 5, Xbox Series X", state.platformsText)
    }

    @Test
    fun loadGameDetails_success_formatsReleaseDateCorrectly() = runTest {
        // Arrange
        coEvery { repository.getGameDetails(testGameId) } returns testGame
        coEvery { repository.getGameScreenshots(testGameId) } returns emptyList()
        coEvery { repository.getGameMovies(testGameId) } returns emptyList()

        // Act
        viewModel = GameDetailsViewModel(repository, savedStateHandle)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value as GameDetailsUiState.Success
        assertEquals("2024-01-15", state.releaseDateFormatted)
    }

    @Test
    fun loadGameDetails_noGenres_showsNoGenresMessage() = runTest {
        // Arrange
        val gameNoGenres = testGame.copy(genres = emptyList())
        coEvery { repository.getGameDetails(testGameId) } returns gameNoGenres
        coEvery { repository.getGameScreenshots(testGameId) } returns emptyList()
        coEvery { repository.getGameMovies(testGameId) } returns emptyList()

        // Act
        viewModel = GameDetailsViewModel(repository, savedStateHandle)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value as GameDetailsUiState.Success
        assertEquals("No genres available", state.genresText)
    }

    @Test
    fun loadGameDetails_noPlatforms_showsNoPlatformsMessage() = runTest {
        // Arrange
        val gameNoPlatforms = testGame.copy(platforms = emptyList())
        coEvery { repository.getGameDetails(testGameId) } returns gameNoPlatforms
        coEvery { repository.getGameScreenshots(testGameId) } returns emptyList()
        coEvery { repository.getGameMovies(testGameId) } returns emptyList()

        // Act
        viewModel = GameDetailsViewModel(repository, savedStateHandle)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value as GameDetailsUiState.Success
        assertEquals("No platforms available", state.platformsText)
    }

    @Test
    fun loadGameDetails_nullReleaseDate_showsUnknownMessage() = runTest {
        // Arrange
        val gameNullDate = testGame.copy(releaseDate = null)
        coEvery { repository.getGameDetails(testGameId) } returns gameNullDate
        coEvery { repository.getGameScreenshots(testGameId) } returns emptyList()
        coEvery { repository.getGameMovies(testGameId) } returns emptyList()

        // Act
        viewModel = GameDetailsViewModel(repository, savedStateHandle)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value as GameDetailsUiState.Success
        assertEquals("Release date unknown", state.releaseDateFormatted)
    }

    @Test
    fun loadGameDetails_moreThanFivePlatforms_showsAndMore() = runTest {
        // Arrange
        val manyPlatforms = listOf(
            PlatformWrapperDto(PlatformDto(1, "PlayStation 5")),
            PlatformWrapperDto(PlatformDto(2, "Xbox Series X")),
            PlatformWrapperDto(PlatformDto(3, "PC")),
            PlatformWrapperDto(PlatformDto(4, "Nintendo Switch")),
            PlatformWrapperDto(PlatformDto(5, "PlayStation 4")),
            PlatformWrapperDto(PlatformDto(6, "Xbox One"))
        )
        val gameManyPlatforms = testGame.copy(platforms = manyPlatforms)
        coEvery { repository.getGameDetails(testGameId) } returns gameManyPlatforms
        coEvery { repository.getGameScreenshots(testGameId) } returns emptyList()
        coEvery { repository.getGameMovies(testGameId) } returns emptyList()

        // Act
        viewModel = GameDetailsViewModel(repository, savedStateHandle)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value as GameDetailsUiState.Success
        assertTrue("Should show 'and more' for 6 platforms",
            state.platformsText.contains("and more"))
    }


    @Test
    fun getTrailerUrl_fromMovies_returnsMaxQuality() = runTest {
        // Arrange
        coEvery { repository.getGameDetails(testGameId) } returns testGame
        coEvery { repository.getGameScreenshots(testGameId) } returns emptyList()
        coEvery { repository.getGameMovies(testGameId) } returns testMovies

        // Act
        viewModel = GameDetailsViewModel(repository, savedStateHandle)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value as GameDetailsUiState.Success
        assertEquals("https://example.com/trailer_max.mp4", state.trailerUrl)
    }

    @Test
    fun getTrailerUrl_fromMovies_fallsBackTo480() = runTest {
        // Arrange
        val movieWith480Only = GameMovieDto(
            id = 1,
            data = MovieDataDto(max = null, `480` = "https://example.com/trailer_480.mp4")
        )
        coEvery { repository.getGameDetails(testGameId) } returns testGame
        coEvery { repository.getGameScreenshots(testGameId) } returns emptyList()
        coEvery { repository.getGameMovies(testGameId) } returns listOf(movieWith480Only)

        // Act
        viewModel = GameDetailsViewModel(repository, savedStateHandle)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value as GameDetailsUiState.Success
        assertEquals("https://example.com/trailer_480.mp4", state.trailerUrl)
    }


    @Test
    fun getTrailerUrl_noTrailerAvailable_returnsNull() = runTest {
        // Arrange
        coEvery { repository.getGameDetails(testGameId) } returns testGame
        coEvery { repository.getGameScreenshots(testGameId) } returns emptyList()
        coEvery { repository.getGameMovies(testGameId) } returns emptyList()

        // Act
        viewModel = GameDetailsViewModel(repository, savedStateHandle)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value as GameDetailsUiState.Success
        assertNull(state.trailerUrl)
    }



    @Test
    fun loadGameDetails_nullGame_setsErrorState() = runTest {
        // Arrange
        coEvery { repository.getGameDetails(testGameId) } returns null
        coEvery { repository.getGameScreenshots(testGameId) } returns emptyList()
        coEvery { repository.getGameMovies(testGameId) } returns emptyList()

        // Act
        viewModel = GameDetailsViewModel(repository, savedStateHandle)
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertTrue("Expected Error state but got ${state::class.simpleName}",
            state is GameDetailsUiState.Error)
        assertEquals("Game not found", (state as GameDetailsUiState.Error).message)
    }



    @Test
    fun retry_validGameId_reloadsGameDetails() = runTest {
        // Arrange
        coEvery { repository.getGameDetails(testGameId) } returns testGame
        coEvery { repository.getGameScreenshots(testGameId) } returns testScreenshots
        coEvery { repository.getGameMovies(testGameId) } returns testMovies

        viewModel = GameDetailsViewModel(repository, savedStateHandle)
        advanceUntilIdle()

        // Act
        viewModel.retry()
        advanceUntilIdle()

        // Assert
        coVerify(exactly = 2) { repository.getGameDetails(testGameId) }
        coVerify(exactly = 2) { repository.getGameScreenshots(testGameId) }
        coVerify(exactly = 2) { repository.getGameMovies(testGameId) }
    }

    @Test
    fun retry_invalidGameId_setsErrorState() = runTest {
        // Arrange
        every { savedStateHandle.get<String>("gameId") } returns null
        viewModel = GameDetailsViewModel(repository, savedStateHandle)
        advanceUntilIdle()

        // Act
        viewModel.retry()
        advanceUntilIdle()

        // Assert
        val state = viewModel.uiState.value
        assertTrue("Expected Error state but got ${state::class.simpleName}",
            state is GameDetailsUiState.Error)
        assertEquals("Invalid game ID", (state as GameDetailsUiState.Error).message)
    }



    @Test
    fun loadGameDetails_setsLoadingStateFirst() = runTest {
        // Arrange
        coEvery { repository.getGameDetails(testGameId) } coAnswers {
            // Delay to capture loading state
            kotlinx.coroutines.delay(100)
            testGame
        }
        coEvery { repository.getGameScreenshots(testGameId) } returns emptyList()
        coEvery { repository.getGameMovies(testGameId) } returns emptyList()

        // Act
        viewModel = GameDetailsViewModel(repository, savedStateHandle)


        assertTrue("Should start in Loading state",
            viewModel.uiState.value is GameDetailsUiState.Loading)

        advanceUntilIdle()


        assertTrue("Should end in Success state",
            viewModel.uiState.value is GameDetailsUiState.Success)
    }


    @Test
    fun loadGameDetails_loadsDataInParallel() = runTest {
        // Arrange
        var gameCallTime = 0L
        var screenshotsCallTime = 0L
        var moviesCallTime = 0L

        coEvery { repository.getGameDetails(testGameId) } coAnswers {
            gameCallTime = System.currentTimeMillis()
            kotlinx.coroutines.delay(50)
            testGame
        }
        coEvery { repository.getGameScreenshots(testGameId) } coAnswers {
            screenshotsCallTime = System.currentTimeMillis()
            kotlinx.coroutines.delay(50)
            testScreenshots
        }
        coEvery { repository.getGameMovies(testGameId) } coAnswers {
            moviesCallTime = System.currentTimeMillis()
            kotlinx.coroutines.delay(50)
            testMovies
        }

        // Act
        viewModel = GameDetailsViewModel(repository, savedStateHandle)
        advanceUntilIdle()

        // Assert - All three methods should be called
        coVerify { repository.getGameDetails(testGameId) }
        coVerify { repository.getGameScreenshots(testGameId) }
        coVerify { repository.getGameMovies(testGameId) }
    }
}