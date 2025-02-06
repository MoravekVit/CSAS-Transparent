package com.moravekvit.csastransparent.data

import com.moravekvit.csastransparent.data.mappers.toAccount
import com.moravekvit.csastransparent.data.mappers.toListOfAccounts
import com.moravekvit.csastransparent.data.remote.CSASTransparentApi
import com.moravekvit.csastransparent.domain.Account
import com.moravekvit.csastransparent.domain.PagingResponse
import com.moravekvit.csastransparent.domain.NetworkError
import com.moravekvit.csastransparent.domain.Result
import com.moravekvit.csastransparent.repository.AccountsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

class AccountsRepositoryImpl(
    private val api: CSASTransparentApi
) : AccountsRepository {

    override suspend fun getListOfAccounts(
        page: Int,
        size: Int,
        filter: String?
    ): Result<PagingResponse, NetworkError> {
        return try {
            val response = withContext(Dispatchers.IO) {
                api.getPaginatedListOfAccounts(page, size, filter)
            }
            Result.Success(response.toListOfAccounts())
        } catch (e: Exception) {
            coroutineContext.ensureActive()
            Result.Error(NetworkError.DEFAULT_ERROR)
        }

    }

    override suspend fun getAccountDetail(accountNumber: String): Result<Account, NetworkError> {
        return try {
            val response = withContext(Dispatchers.IO) {
                api.getAccountDetail(accountNumber)
            }
            Result.Success(response.toAccount())
        } catch (e: Exception) {
            coroutineContext.ensureActive()
            Result.Error(NetworkError.DEFAULT_ERROR)
        }
    }


}