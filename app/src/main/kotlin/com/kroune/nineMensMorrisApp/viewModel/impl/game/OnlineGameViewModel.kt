package com.kroune.nineMensMorrisApp.viewModel.impl.game

import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewModelScope
import com.kroune.nineMensMorrisApp.data.remote.account.AccountInfoRepositoryI
import com.kroune.nineMensMorrisApp.viewModel.interfaces.ViewModelI
import com.kroune.nineMensMorrisApp.viewModel.useCases.GetUserInfoUseCase
import com.kroune.nineMensMorrisApp.viewModel.useCases.OnlineGameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * welcome model
 * called when app is launched
 */
@HiltViewModel
class OnlineGameViewModel @Inject constructor(
    private val accountInfoRepository: AccountInfoRepositoryI
) : ViewModelI() {
    private val game = OnlineGameUseCase(accountInfoRepository, viewModelScope)

    /**
     * handles clicking
     */
    fun onClick(index: Int) {
        game.gameBoard.onClick(index)
    }

    /**
     * performs give up actions
     */
    fun giveUp() {
        game.giveUp()
    }

    /**
     * if the player move first
     */
    val movesFirst = game.isGreen

    /**
     * time left for move
     */
    val timeLeft = game.timeLeft

    /**
     * move hints
     */
    val moveHints = game.gameBoard.moveHints

    /**
     * selected button
     */
    val selectedButton = game.gameBoard.selectedButton

    /**
     * current position
     */
    val pos = game.gameBoard.pos

    /**
     * history of played moves
     */
    val movesHistory = game.gameBoard.movesHistory

    /**
     * game id
     */
    private var gameId: Long? = null


    private val ownAccountId = accountInfoRepository.accountIdState
    private val enemyAccountId = game.enemyId
    private val ownInfo = GetUserInfoUseCase(viewModelScope, accountInfoRepository)
    private val enemyInfo = GetUserInfoUseCase(viewModelScope, accountInfoRepository)

    /**
     * name of the account
     * null while still loading
     */
    val ownAccountName: MutableState<String?> = ownInfo.accountName

    /**
     * picture byte array
     * null while still loading
     */
    val ownPictureByteArray: MutableState<ByteArray?> = ownInfo.pictureByteArray

    /**
     * player rating
     * null while still loading
     */
    val ownAccountRating: MutableState<Long?> = ownInfo.accountRating

    /**
     * name of the account
     * null while still loading
     */
    val enemyAccountName: MutableState<String?> = enemyInfo.accountName

    /**
     * picture byte array
     * null while still loading
     */
    val enemyPictureByteArray: MutableState<ByteArray?> = enemyInfo.pictureByteArray

    /**
     * player rating
     * null while still loading
     */
    val enemyAccountRating: MutableState<Long?> = enemyInfo.accountRating

    init {
        viewModelScope.launch {
            ownAccountId.collectLatest {
                println("own account id - $it")
                if (it != null) {
                    ownInfo.getInfo(it)
                } else {
                    // it will be remove in the next refactor related to jwt tokens
                    accountInfoRepository.updateAccountIdState(
                        accountInfoRepository.getIdByJwtToken(
                            accountInfoRepository.jwtTokenState.value!!
                        ).getOrThrow()
                    )
                }
            }
        }
        viewModelScope.launch {
            enemyAccountId.collectLatest {
                println("enemy account id - $it")
                if (it != null) {
                    enemyInfo.getInfo(it)
                }
            }
        }
    }

    /**
     * returns if game has ended
     */
    val gameEnded = game.gameEnded

    /**
     * passes game it to the vm
     */
    fun setVariables(gameIdData: Long) {
        gameId = gameIdData
    }

    /**
     * starts creating the game
     * we can't put this code in init {} block, because we firstly need to
     * [setVariables] to pass gameId to vm
     * you can also do this, using assistedInject, but it is much more complicated
     * and we have to use our own vm factory
     */
    fun init() {
        game.createGameConnection(gameId!!)
    }
}
