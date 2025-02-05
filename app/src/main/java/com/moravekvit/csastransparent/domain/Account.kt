package com.moravekvit.csastransparent.domain

import java.time.LocalDateTime

data class Account(
    val accountNumber: String,
    val bankCode: String,
    val transparencyFrom: LocalDateTime,
    val transparencyTo: LocalDateTime,
    val publicationTo: LocalDateTime,
    val actualizationDate: LocalDateTime,
    val balance: Double,
    val currency: String?,
    val name: String,
    val description: String?,
    val note: String?,
    val iban: String,
    val statements: List<String>?
)