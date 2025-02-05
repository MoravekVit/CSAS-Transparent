package com.moravekvit.csastransparent.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccountDto(
    @SerialName("accountNumber")
    val accountNumber: String,
    @SerialName("bankCode")
    val bankCode: String,
    @SerialName("transparencyFrom")
    val transparencyFrom: String,
    @SerialName("transparencyTo")
    val transparencyTo: String,
    @SerialName("publicationTo")
    val publicationTo: String,
    @SerialName("actualizationDate")
    val actualizationDate: String,
    @SerialName("balance")
    val balance: Double,
    @SerialName("currency")
    val currency: String? = null,
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String? = null,
    @SerialName("note")
    val note: String? = null,
    @SerialName("iban")
    val iban: String,
    @SerialName("statements")
    val statements: List<String>? = null
)
