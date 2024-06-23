package com.kroune.mensMorris.viewModel.impl

import com.kroune.mensMorris.data.local.impl.WelcomeData
import com.kroune.mensMorris.data.remote.auth.AuthRepositoryI
import com.kroune.mensMorris.viewModel.interfaces.ViewModelI
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * welcome model
 * called when app is launched
 */
@HiltViewModel
class WelcomeViewModel @Inject constructor(private val authRepositoryI: AuthRepositoryI) : ViewModelI() {
    override val data = WelcomeData()

    /**
     * checks if jwt token is valid
     */
    suspend fun checkJwtToken(): Result<Boolean> {
        return authRepositoryI.checkJwtToken()
    }
}