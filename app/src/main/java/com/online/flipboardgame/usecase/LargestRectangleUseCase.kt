package com.online.flipboardgame.usecase

import com.online.flipboardgame.model.Rectangle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

open class LargestRectangleUseCase @Inject constructor() {

    fun calculateLargestRectangle(grid: List<List<Boolean>>): Flow<Rectangle?> = flow {
        val rows = grid.size
        val cols = grid[0].size
        val heights = IntArray(cols) { 0 }
        var maxArea = 0
        var bestRectangle: Rectangle? = null

        for (i in 0 until rows) {
            for (j in 0 until cols) {
                heights[j] = if (grid[i][j]) heights[j] + 1 else 0
            }
            val (area, rectangle) = calculateMaxRectangleInHistogram(heights, i)
            if (area > maxArea) {
                maxArea = area
                bestRectangle = rectangle
            }
        }

        emit(bestRectangle)
    }

    private fun calculateMaxRectangleInHistogram(heights: IntArray, currentRow: Int): Pair<Int, Rectangle?> {
        val stack = mutableListOf<Int>()
        var maxArea = 0
        var bestRectangle: Rectangle? = null
        var i = 0

        while (i < heights.size) {
            if (stack.isEmpty() || heights[i] >= heights[stack.last()]) {
                stack.add(i++)
            } else {
                val height = heights[stack.removeAt(stack.size - 1)]
                val width = if (stack.isEmpty()) i else i - stack.last() - 1
                val area = height * width
                if (area > maxArea) {
                    maxArea = area
                    val left = if (stack.isEmpty()) 0 else stack.last() + 1
                    val topLeft = Pair(left, currentRow - height + 1)
                    val bottomRight = Pair(left + width - 1, currentRow)
                    bestRectangle = Rectangle(topLeft, bottomRight)
                }
            }
        }

        while (stack.isNotEmpty()) {
            val height = heights[stack.removeAt(stack.size - 1)]
            val width = if (stack.isEmpty()) i else i - stack.last() - 1
            val area = height * width
            if (area > maxArea) {
                maxArea = area
                val left = if (stack.isEmpty()) 0 else stack.last() + 1
                val topLeft = Pair(left, currentRow - height + 1)
                val bottomRight = Pair(left + width - 1, currentRow)
                bestRectangle = Rectangle(topLeft, bottomRight)
            }
        }

        return Pair(maxArea, bestRectangle)
    }
}