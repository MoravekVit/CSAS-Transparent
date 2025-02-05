package com.moravekvit.csastransparent.data.mappers

import com.moravekvit.csastransparent.data.remote.dto.AccountDto
import com.moravekvit.csastransparent.data.remote.dto.ListOfAccountsDto
import com.moravekvit.csastransparent.domain.Account
import com.moravekvit.csastransparent.domain.ListOfAccounts
import java.time.LocalDate
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

fun ListOfAccountsDto.toListOfAccounts(): ListOfAccounts {
    return ListOfAccounts(
        pageNumber = pageNumber,
        pageCount = pageCount,
        pageSize = pageSize,
        recordCount = recordCount,
        nextPage = nextPage,
        accounts = accounts.toListOfAccounts()
    )
}

fun List<AccountDto>?.toListOfAccounts(): List<Account> {
    val mappedList = mutableListOf<Account>()
    this?.forEach {
        mappedList.add(it.toAccount())
    }
    return mappedList
}