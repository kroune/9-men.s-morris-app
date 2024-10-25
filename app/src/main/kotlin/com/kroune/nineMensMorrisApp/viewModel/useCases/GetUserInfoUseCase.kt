package com.kroune.nineMensMorrisApp.viewModel.useCases

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.kroune.nineMensMorrisApp.data.remote.account.AccountInfoRepositoryI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class GetUserInfoUseCase(
    val scope: CoroutineScope,
    val accountInfoRepository: AccountInfoRepositoryI
) {

    val accountName: MutableState<String?> = mutableStateOf(null)
    val pictureByteArray: MutableState<ByteArray?> = mutableStateOf(null)
    val accountRating: MutableState<Long?> = mutableStateOf(null)

    fun getInfo(id: Long) {
        scope.launch {
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
}