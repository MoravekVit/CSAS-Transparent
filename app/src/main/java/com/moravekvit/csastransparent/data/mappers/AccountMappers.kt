package com.moravekvit.csastransparent.data.mappers

import com.moravekvit.csastransparent.data.remote.dto.AccountDto
import com.moravekvit.csastransparent.data.remote.dto.PagingResponseDto
import com.moravekvit.csastransparent.domain.Account
import com.moravekvit.csastransparent.domain.PagingResponse
import java.time.LocalDateTime

fun AccountDto.toAccount(): Account {
    return Account(
        accountNumber = accountNumber,
        bankCode = bankCode,
        transparencyFrom = LocalDateTime.parse(transparencyFrom),
        transparencyTo = LocalDateTime.parse(transparencyTo),
        publicationTo = LocalDateTime.parse(publicationTo),
        actualizationDate = LocalDateTime.parse(actualizationDate),
        balance = balance,
        currency = currency,
        name = name,
        description = description,
        note = note,
        iban = iban,
        statements = statements,
    )
}

fun PagingResponseDto.toListOfAccounts(): PagingResponse {
    return PagingResponse(
        pageNumber = pageNumber,
        pageCount = pageCount,
        pageSize = pageSize,
        recordCount = recordCount,
        nextPage = nextPage,
        accounts = accounts.toAccountList()
    )
}

fun List<AccountDto>?.toAccountList(): List<Account>? {
    return this?.map { dto ->
        dto.toAccount()
    }
}