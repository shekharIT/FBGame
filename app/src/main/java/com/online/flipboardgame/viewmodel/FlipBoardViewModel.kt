package com.online.flipboardgame.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.online.flipboardgame.model.GameState
import com.online.flipboardgame.usecase.LargestRectangleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class FlipBoardViewModel @Inject constructor(
    private val largestRectangleUseCase: LargestRectangleUseCase
) : ViewModel() {

    private val _gameState = MutableStateFlow<GameState>(GameState.Idle)
    open val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private var grid = List(15) { List(15) { false } }

    init {
        updateGameState()
    }

    open fun toggleCell(row: Int, col: Int) {
        grid = grid.mapIndexed { r, rows ->
            rows.mapIndexed { c, cell ->
                if (r == row && c == col) !cell else cell
            }
        }
        println("Toggled cell at ($row, $col) to ${grid[row][col]}") // Debug print
        updateGameState()
    }

    private fun updateGameState() {
        viewModelScope.launch {
            largestRectangleUseCase.calculateLargestRectangle(grid).collect { rectangle ->
                println("Updating game state with rectangle: $rectangle") // Debug print
                _gameState.value = GameState.Running(grid, rectangle)
            }
        }
    }
}