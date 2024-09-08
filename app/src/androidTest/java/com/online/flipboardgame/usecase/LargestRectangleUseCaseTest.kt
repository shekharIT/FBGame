package com.online.flipboardgame.usecase

import app.cash.turbine.test
import com.online.flipboardgame.model.Rectangle
import com.online.flipboardgame.usecase.LargestRectangleUseCase
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LargestRectangleUseCaseTest {

    private lateinit var largestRectangleUseCase: LargestRectangleUseCase

    @Before
    fun setUp() {
        largestRectangleUseCase = LargestRectangleUseCase()
    }

    @Test
    fun test_calculateLargestRectangle_for_simple_2x2_grid_with_no_selected_fields() = runBlockingTest {
        val grid = listOf(
            listOf(false, false),
            listOf(false, false)
        )

        largestRectangleUseCase.calculateLargestRectangle(grid).test {
            val result = awaitItem()
            assertEquals(null, result) // No selected fields, so no rectangle
            awaitComplete()
        }
    }

    @Test
    fun test_calculateLargestRectangle_for_simple_2x2_grid_with_all_selected_fields() = runBlockingTest {
        val grid = listOf(
            listOf(true, true),
            listOf(true, true)
        )

        largestRectangleUseCase.calculateLargestRectangle(grid).test {
            val result = awaitItem()

            // Expected rectangle: covers the entire grid (topLeft(0,0) to bottomRight(1,1))
            val expectedRectangle = Rectangle(Pair(0, 0), Pair(1, 1))

            assertEquals(expectedRectangle, result)
            awaitComplete()
        }
    }

    @Test
    fun test_calculateLargestRectangle_for_3x3_grid_with_a_vertical_selection() = runBlockingTest {
        val grid = listOf(
            listOf(false, true, false),
            listOf(false, true, false),
            listOf(false, true, false)
        )

        largestRectangleUseCase.calculateLargestRectangle(grid).test {
            val result = awaitItem()

            // Expected rectangle: 1 column selected vertically (topLeft(1,0) to bottomRight(1,2))
            val expectedRectangle = Rectangle(Pair(1, 0), Pair(1, 2))

            assertEquals(expectedRectangle, result)
            awaitComplete()
        }
    }

    @Test
    fun test_calculateLargestRectangle_for_4x4_grid_with_multiple_selected_areas() = runBlockingTest {
        val grid = listOf(
            listOf(false, false, true, true),
            listOf(true, true, true, true),
            listOf(true, true, true, true),
            listOf(false, true, true, true)
        )

        largestRectangleUseCase.calculateLargestRectangle(grid).test {
            val result = awaitItem()

            // Expected largest rectangle: (topLeft(1, 1) to bottomRight(3,3))
            val expectedRectangle = Rectangle(Pair(1, 1), Pair(3, 3))

            assertEquals(expectedRectangle, result)
            awaitComplete()
        }
    }
}