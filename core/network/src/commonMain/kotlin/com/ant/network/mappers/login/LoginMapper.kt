package com.ant.network.mappers.login

import com.ant.models.model.UserData
import com.ant.network.dto.AccountDto
import com.ant.network.mappers.Mapper

/** Maps [AccountDto] to [UserData], extracting the user's display name. */
class LoginMapper : Mapper<AccountDto, UserData> {
    override suspend fun map(from: AccountDto): UserData {
        return UserData(
            username = from.name,
        )
    }
}
