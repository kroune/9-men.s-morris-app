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

    /**
     * null = error
     * empty = loading
     * non empty = loaded
     */
    val players: SnapshotStateList<Player>? = mutableStateListOf()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val leaderboardData =
                accountInfoRepository.getLeaderBoard().getOrNull()
            if (leaderboardData == null) {
                players == null
                return@launch
            }
            leaderboardData.forEach { id ->
                launch {
                    val userInfo = GetUserInfoUseCase(this, accountInfoRepository)
                    userInfo.getInfo(id, true)
                }
            }
        }
    }
}
