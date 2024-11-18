package com.kroune.nineMensMorrisApp.viewModel.impl

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.kroune.nineMensMorrisApp.data.remote.account.AccountInfoRepositoryI
import com.kroune.nineMensMorrisApp.ui.impl.Player
import com.kroune.nineMensMorrisApp.viewModel.interfaces.ViewModelI
import com.kroune.nineMensMorrisApp.viewModel.useCases.GetUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * view model
 *  @see [OnlineLeaderBoardScreen]
 */
@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val accountInfoRepository: AccountInfoRepositoryI
) : ViewModelI() {

    val players: SnapshotStateList<Player> = mutableStateListOf()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val leaderboardData = accountInfoRepository.getLeaderBoard().getOrNull()
            if (leaderboardData == null) {
                players.clear()
                return@launch
            } else {
                leaderboardData.forEach { id ->
                    launch {
                        val userInfoUseCase = GetUserInfoUseCase(this, accountInfoRepository)
                        userInfoUseCase.getInfo(id, false)
                        val player = Player(
                            accountName = userInfoUseCase.accountName,
                            pictureByteArray = userInfoUseCase.pictureByteArray,
                            accountRating = userInfoUseCase.accountRating
                        )
                        players.add(player)
                    }
                }
            }
        }
    }
}

