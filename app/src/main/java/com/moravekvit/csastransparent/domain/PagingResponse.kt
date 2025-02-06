package com.moravekvit.csastransparent.domain

data class PagingResponse(
    val pageNumber: Int,
    val pageCount: Int,
    val pageSize: Int,
    val recordCount: Int,
    val nextPage: Int,
    val accounts: List<Account>?
)