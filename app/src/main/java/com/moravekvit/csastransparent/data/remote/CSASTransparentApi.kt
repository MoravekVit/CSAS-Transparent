package com.moravekvit.csastransparent.data.remote

import com.moravekvit.csastransparent.data.remote.dto.AccountDto
import com.moravekvit.csastransparent.data.remote.dto.ListOfAccountsDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CSASTransparentApi {

    @GET("transparentAccounts")
    suspend fun getPaginatedListOfAccounts(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("filter") filter: String? = null
    ): ListOfAccountsDto

    @GET("transparentAccounts/id")
    suspend fun getAccountDetail(
        @Query("id") accountNumber: String
    ): AccountDto
}