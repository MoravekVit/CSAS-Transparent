package com.moravekvit.csastransparent.data.remote.dto

import kotlinx.serialization.Serializable

import kotlinx.serialization.SerialName


@Serializable
data class PagingResponseDto(
    @SerialName("pageNumber")
    val pageNumber: Int,
    @SerialName("pageCount")
    val pageCount: Int,
    @SerialName("pageSize")
    val pageSize: Int,
    @SerialName("recordCount")
    val recordCount: Int,
    @SerialName("nextPage")
    val nextPage: Int,
    @SerialName("accounts")
    val accounts: List<AccountDto>? = null
)


