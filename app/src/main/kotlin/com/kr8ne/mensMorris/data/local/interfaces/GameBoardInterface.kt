package com.kr8ne.mensMorris.data.local.interfaces

import com.kr8ne.mensMorris.viewModel.impl.game.GameBoardViewModel

/**
 * interface with game board value
 */
interface GameBoardInterface {
    /**
     * our game board
     * usually used for rendering
     */
    val gameBoard: GameBoardViewModel
}