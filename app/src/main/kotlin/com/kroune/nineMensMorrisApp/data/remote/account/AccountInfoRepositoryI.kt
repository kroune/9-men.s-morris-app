package com.kroune.nineMensMorrisApp.data.remote.account

import kotlinx.coroutines.flow.MutableStateFlow

/**
 * interface for account repository
 * provides information about account
 */
interface AccountInfoRepositoryI {
    /**
     * unique identifier of the account
     * null = account id is loading
     * -1 = loading failed
     */
    val accountIdState: MutableStateFlow<Long?>

    /**
     * updates account id
     * this function should be used when updating [accountIdState]
     */
    fun updateAccountIdState(value: Long?)

    /**
     * Jwt token provided by the server
     */
    val jwtTokenState: MutableStateFlow<String?>

    /**
     * updates account jwt token
     * this function should be used when updating [jwtTokenState]
     */
    fun updateJwtTokenState(value: String?)

    /**
     * @return account rating by it's id
     */
    suspend fun getAccountRatingById(id: Long, jwtToken: String): Result<Long?>

    /**
     * @return account login (name) by it's id
     */
    suspend fun getAccountDateById(id: Long, jwtToken: String): Result<Triple<Int, Int, Int>?>

    /**
     * @return account login (name) by it's id
     */
    suspend fun getAccountNameById(id: Long, jwtToken: String): Result<String?>

    /**
     * @return account picture by it's id
     */
    suspend fun getAccountPictureById(id: Long, jwtToken: String): Result<ByteArray>

    /**
     * @return account id by it's jwtToken
     * it is null if this account isn't valid
     */
    suspend fun getIdByJwtToken(jwtToken: String): Result<Long>

    /**
     * @return top 10 users in the leaderboard in descending order (or less if there is < 10 players at all)
     */
    suspend fun getLeaderBoard(): Result<List<Long>>

    /**
     * logs out of the account
     */
    fun logout()
}
