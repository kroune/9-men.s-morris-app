package com.example.mensmorris.model.impl

import com.example.mensmorris.common.gameBoard.GameBoard
import com.example.mensmorris.common.utils.CacheUtils
import com.example.mensmorris.data.impl.GameEndData
import com.example.mensmorris.domain.ScreenModel
import com.example.mensmorris.domain.impl.GameEndScreen
import com.example.mensmorris.model.ViewModelInterface

/**
 * game end model
 */
class GameEndViewModel : ViewModelInterface {
    /**
     * our current game board
     */
    val gameBoard = GameBoard(CacheUtils.position)
    override var render: ScreenModel =
        GameEndScreen(gameBoard)
    override val data = GameEndData(gameBoard)
}