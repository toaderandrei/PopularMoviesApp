package com.ant.domain.usecases.login

import com.ant.domain.repositories.AuthRepository
import com.ant.domain.usecases.resultFlow
import com.ant.models.model.Result
import com.ant.models.model.UserData
import com.ant.models.request.RequestType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

/**
 * Logs out the user from the TMDb API and returns cleared user data.
 */
class LogoutUserAndClearSessionUseCase constructor(
    private val authRepository: AuthRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    /** @return a Flow emitting [UserData] with a null session ID after logout completes. */
    operator fun invoke(parameters: RequestType.LoginSessionRequest.Logout): Flow<Result<UserData>> {
        return resultFlow(dispatcher) {
            authRepository.logout(parameters)
            UserData(
                sessionId = null,
                username = parameters.username
            )
        }
    }
}
