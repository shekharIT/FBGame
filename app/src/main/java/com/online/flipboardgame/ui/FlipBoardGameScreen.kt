package com.online.flipboardgame.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.online.flipboardgame.model.GameState
import com.online.flipboardgame.model.Rectangle
import com.online.flipboardgame.viewmodel.FlipBoardViewModel

@Composable
fun FlipBoardGameScreen(viewModel: FlipBoardViewModel = hiltViewModel()) {

    val gameState by viewModel.gameState.collectAsState()

    when (val currentState = gameState) {
        is GameState.Idle -> Text("Game is idle")
        is GameState.Running -> {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GridView(currentState.grid, currentState.largestRectangleArea, onCellClick = { row, col ->
                    viewModel.toggleCell(row, col)
                })
                Spacer(modifier = Modifier.height(16.dp))
                Text("Biggest Rectangle Area: ${currentState.largestRectangleArea?.let { (it.bottomRight.first - it.topLeft.first + 1) * (it.bottomRight.second - it.topLeft.second + 1) } ?: 0}", color = Color.Red)
            }
        }
    }
}

@Composable
fun GridView(grid: List<List<Boolean>>, largestRectangle: Rectangle?, onCellClick: (Int, Int) -> Unit) {
    Column {
        grid.forEachIndexed { rowIndex, row ->
            Row {
                row.forEachIndexed { colIndex, isOn ->
                    val isLargestRectangle = largestRectangle?.let {
                        rowIndex in it.topLeft.second..it.bottomRight.second &&
                                colIndex in it.topLeft.first..it.bottomRight.first
                    } ?: false
                    GridCell(isOn, isLargestRectangle, onClick = { onCellClick(rowIndex, colIndex) })
                }
            }
        }
    }
}

@Composable
fun GridCell(isOn: Boolean, isLargestRectangle: Boolean, onClick: () -> Unit) {
    val backgroundColor = when {
            isLargestRectangle -> Color.Red
            isOn -> Color.Gray
            else -> Color.LightGray
    }
    Box(
        modifier = Modifier
            .size(20.dp)
            .background(backgroundColor)
            .border(1.dp, Color.Black)
            .clickable(onClick = onClick)
    )
}