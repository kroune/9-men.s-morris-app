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
 * welcome model
 * called when app is launched
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
            }
            leaderboardData.forEach { id ->
                launch {
                    val userInfoUseCase = GetUserInfoUseCase(this, accountInfoRepository)
                    userInfoUseCase.getInfo(id, true)
                    val player = Player(
                        accountName = userInfoUseCase.accountName.value ?: "Unknown",
                        pictureByteArray = userInfoUseCase.pictureByteArray.value ?: ByteArray(0),
                        accountRating = userInfoUseCase.accountRating.value ?: 0L
                    )
                    players.add(player)
                }
            }
        }
    }
}

