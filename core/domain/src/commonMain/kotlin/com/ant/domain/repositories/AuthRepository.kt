package com.ant.domain.repositories

import com.ant.models.model.UserData
import com.ant.models.request.RequestType

/**
 * Repository for user authentication operations against the TMDb API.
 */
interface AuthRepository {
    /** Authenticates the user with TMDb credentials and returns the user profile data. */
    suspend fun login(params: RequestType.LoginSessionRequest.WithCredentials): UserData

    /** Logs out the user and invalidates the current TMDb session. */
    suspend fun logout(params: RequestType.LoginSessionRequest.Logout): Boolean
}
