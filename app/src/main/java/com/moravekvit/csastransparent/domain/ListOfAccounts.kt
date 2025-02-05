package com.moravekvit.csastransparent.domain

data class ListOfAccounts(
    val pageNumber: Int,
    val pageCount: Int,
    val pageSize: Int,
    val recordCount: Int,
    val nextPage: Int,
    val accounts: List<Account>
)