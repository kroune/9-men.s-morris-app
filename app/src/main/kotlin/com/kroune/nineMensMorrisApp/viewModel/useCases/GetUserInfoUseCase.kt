package com.kroune.nineMensMorrisApp.viewModel.useCases

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.kroune.nineMensMorrisApp.data.remote.account.AccountInfoRepositoryI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * gets all info about player
 */
class GetUserInfoUseCase(
    private val scope: CoroutineScope,
    private val accountInfoRepository: AccountInfoRepositoryI
) {
    /**
     * name of the account
     * null while still loading
     */
    val accountName: MutableState<String?> = mutableStateOf(null)

    /**
     * picture byte array
     * null while still loading
     */
    val pictureByteArray: MutableState<ByteArray?> = mutableStateOf(null)

    /**
     * player rating
     * null while still loading
     */
    val accountRating: MutableState<Long?> = mutableStateOf(null)

    /**
     * gets info for user
     * @param id = id of the user
     */
    fun getInfo(id: Long) {
        scope.launch {
            accountName.value =
                accountInfoRepository.getAccountNameById(
                    id,
                    accountInfoRepository.jwtTokenState.value!!
                ).getOrThrow()
        }
        scope.launch {
            pictureByteArray.value =
                accountInfoRepository.getAccountPictureById(
                    id,
                    accountInfoRepository.jwtTokenState.value!!
                ).getOrThrow()
        }
        scope.launch {
            accountRating.value =
                accountInfoRepository.getAccountRatingById(
                    id,
                    accountInfoRepository.jwtTokenState.value!!
                ).getOrThrow()
        }
    }
}
