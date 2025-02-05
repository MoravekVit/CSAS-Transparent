package com.moravekvit.csastransparent.repository

import com.moravekvit.csastransparent.domain.Account
import com.moravekvit.csastransparent.domain.ListOfAccounts
import com.moravekvit.csastransparent.domain.NetworkError
import com.moravekvit.csastransparent.domain.Result

interface AccountsRepository {

    suspend fun getListOfAccounts(
        page: Int,
        size: Int,
        filter: String?
    ): Result<ListOfAccounts, NetworkError>

    suspend fun getAccountDetail(
        accountNumber: String
    ): Result<Account, NetworkError>
}