package com.online.flipboardgame.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.online.flipboardgame.model.GameState
import com.online.flipboardgame.model.Rectangle
import com.online.flipboardgame.usecase.LargestRectangleUseCase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlinx.coroutines.flow.first
import org.junit.Assert.*

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class FlipBoardViewModelTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    //@Inject
    lateinit var viewModel: FlipBoardViewModel

    @Mock
    lateinit var largestRectangleUseCase: LargestRectangleUseCase

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        hiltRule.inject()
        Dispatchers.setMain(testDispatcher)
        viewModel = FlipBoardViewModel(largestRectangleUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.cleanupTestCoroutines()
    }

    @Test
    fun toggleCell_updatesGrid() = testScope.runBlockingTest {
        // Given
        val initialState = viewModel.gameState.first() as GameState.Running
        val initialGrid = initialState.grid
        assertFalse(initialGrid[0][0])

        // When
        viewModel.toggleCell(0, 0)

        // Then
        val updatedState = viewModel.gameState.first() as GameState.Running
        val updatedGrid = updatedState.grid
        assertTrue(updatedGrid[0][0])
    }

    @Test
    fun multipleToggles_updateGridCorrectly() = testScope.runBlockingTest {
        // Given
        val initialState = viewModel.gameState.first() as GameState.Running
        val initialGrid = initialState.grid

        // When
        viewModel.toggleCell(0, 0)
        viewModel.toggleCell(1, 1)
        viewModel.toggleCell(0, 0)

        // Then
        val finalState = viewModel.gameState.first() as GameState.Running
        val finalGrid = finalState.grid

        assertFalse(finalGrid[0][0])
        assertTrue(finalGrid[1][1])
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test_calculateLargestRectangle_returnsLargestRectangle() = runTest {
        // Given a 3x3 grid with some cells toggled on
        val grid = listOf(
            listOf(true, true, true),
            listOf(true, true, true),
            listOf(true, true, true)
        )

        // When calculating the largest rectangle
        val resultFlow = largestRectangleUseCase.calculateLargestRectangle(grid)

        // Then it should return the correct largest rectangle
        resultFlow.test {
            val rectangle = awaitItem()
            assertEquals(Rectangle(Pair(0, 0), Pair(2, 2)), rectangle)
            awaitComplete()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test_calculateLargestRectangle_noRectangleWhenAllCellsOff() = runTest {
        // Given a 3x3 grid with all cells turned off (false)
        val grid = listOf(
            listOf(false, false, false),
            listOf(false, false, false),
            listOf(false, false, false)
        )

        // When calculating the largest rectangle
        val resultFlow = largestRectangleUseCase.calculateLargestRectangle(grid)

        // Then it should return null, indicating no rectangle found
        resultFlow.test {
            val rectangle = awaitItem()
            assertEquals(null, rectangle)
            awaitComplete()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test_calculateLargestRectangle_returnsRectangleForSingleCell() = runTest {
        // Given a 3x3 grid with a single cell toggled on
        val grid = listOf(
            listOf(false, false, false),
            listOf(false, true, false),
            listOf(false, false, false)
        )

        // When calculating the largest rectangle
        val resultFlow = largestRectangleUseCase.calculateLargestRectangle(grid)

        // Then it should return the correct rectangle for that single cell
        resultFlow.test {
            val rectangle = awaitItem()
            assertEquals(Rectangle(Pair(1, 1), Pair(1, 1)), rectangle)
            awaitComplete()
        }
    }
}