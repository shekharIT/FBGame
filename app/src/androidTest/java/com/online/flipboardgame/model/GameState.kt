package com.online.flipboardgame.model

sealed class GameState {
    object Idle : GameState()
    data class Running(val grid: List<List<Boolean>>, val largestRectangleArea: Rectangle?) : GameState()
}