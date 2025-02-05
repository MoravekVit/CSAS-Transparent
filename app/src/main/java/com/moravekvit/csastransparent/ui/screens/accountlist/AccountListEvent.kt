package com.moravekvit.csastransparent.ui.screens.accountlist

sealed interface AccountListEvent {
    data class NavigateToAccountDetail(val accountNumber: String) : AccountListEvent
    data object DataLoadFailed: AccountListEvent
}
