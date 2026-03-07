package com.ant.data.repositories

import com.ant.data.repositories.login.LoginUserTmDbRepository
import com.ant.data.repositories.login.LogoutUserAndClearSessionsRepository
import com.ant.domain.repositories.AuthRepository
import com.ant.models.model.UserData
import com.ant.models.request.RequestType


/**
 * Default [AuthRepository] implementation that delegates login and logout
 * operations to their respective single-responsibility repositories.
 */
class DefaultAuthRepository constructor(
    private val loginUserTmDbRepository: LoginUserTmDbRepository,
    private val logoutUserAndClearSessionsRepository: LogoutUserAndClearSessionsRepository,
) : AuthRepository {

    /** Authenticates the user with TMDb credentials and returns their profile data. */
    override suspend fun login(params: RequestType.LoginSessionRequest.WithCredentials): UserData {
        return loginUserTmDbRepository.performRequest(params)
    }

    /** Logs the user out and clears all local session data. */
    override suspend fun logout(params: RequestType.LoginSessionRequest.Logout): Boolean {
        return logoutUserAndClearSessionsRepository.performRequest(params)
    }
}
