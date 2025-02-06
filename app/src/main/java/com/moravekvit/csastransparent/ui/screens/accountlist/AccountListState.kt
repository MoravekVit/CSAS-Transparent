package com.moravekvit.csastransparent.ui.screens.accountlist

data class AccountListState(
    val isLoading: Boolean = true,
    val accountsList: List<AccountListItem> = listOf(),
    val filterText: String = "",
    val endReached: Boolean = false,
    val page: Int = 0
)

sealed interface AccountListEvent {
    data class NavigateToAccountDetail(val accountNumber: String) : AccountListEvent
    data object DataLoadFailed: AccountListEvent
}

data class AccountListItem(
    val name: String,
    val accountNumber: String
)
