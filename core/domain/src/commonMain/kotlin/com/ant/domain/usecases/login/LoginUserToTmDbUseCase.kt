package com.ant.domain.usecases.login

import com.ant.domain.repositories.AuthRepository
import com.ant.domain.usecases.resultFlow
import com.ant.models.model.Result
import com.ant.models.model.UserData
import com.ant.models.request.RequestType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

/**
 * Authenticates the user against the TMDb API using credentials.
 *
 * This is a low-level use case that only performs the API call. For login with
 * session persistence, use [LoginUserAndSaveSessionUseCase] instead.
 */
class LoginUserToTmDbUseCase constructor(
    private val repository: AuthRepository,
    private val dispatcher: CoroutineDispatcher,
) {
    /** @return a Flow emitting [UserData] from the TMDb authentication response. */
    operator fun invoke(parameters: RequestType.LoginSessionRequest.WithCredentials): Flow<Result<UserData>> {
        return resultFlow(dispatcher) {
            repository.login(parameters)
        }
    }
}
