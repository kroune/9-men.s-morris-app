package com.kr8ne.mensMorris.viewModel.impl.game

import androidx.navigation.NavHostController
import com.kr8ne.mensMorris.data.local.impl.game.GameWithFriendData
import com.kr8ne.mensMorris.ui.impl.game.GameWithFriendScreen
import com.kr8ne.mensMorris.ui.interfaces.ScreenModel
import com.kr8ne.mensMorris.viewModel.interfaces.ViewModelI

/**
 * game with friend model
 */
class GameWithFriendViewModel(navController: NavHostController?) : ViewModelI() {
    override val data = GameWithFriendData(navController = navController)
    override var render: ScreenModel = GameWithFriendScreen(data.gameBoard)
}