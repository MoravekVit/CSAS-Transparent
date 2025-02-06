package com.moravekvit.csastransparent.ui.screens.accountlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moravekvit.csastransparent.data.pagination.DefaultPaginator
import com.moravekvit.csastransparent.domain.Account
import com.moravekvit.csastransparent.repository.AccountsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountListViewModel @Inject constructor(
    private val accountsRepository: AccountsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AccountListState())
    val state = _state.asStateFlow()

    private val _event = Channel<AccountListEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    private var confirmedFilter: String = ""

    private val paginator = DefaultPaginator(
        initialKey = _state.value.page,
        onLoadUpdated = {
            _state.update {
                it.copy(isLoading = true)
            }
        },
        onRequest = { nextPage ->
            accountsRepository.getListOfAccounts(nextPage, 20, confirmedFilter)
        },
        getNextKey = { item ->
            item.nextPage
        },
        onError = { _ ->
            viewModelScope.launch {
                _event.send(AccountListEvent.DataLoadFailed)
            }
        },
        onSuccess = { item, newKey ->
            item.accounts?.let {
                _state.update {
                    it.copy(
                        accountsList = it.accountsList + accountListToAccountItemList(item.accounts),
                        page = newKey,
                        endReached = item.accounts.isEmpty()
                    )
                }
            }
        }
    )

    init {
        loadNextItems()
    }

    fun onFilterTextChanged(text: String) {
        _state.update {
            it.copy(filterText = text)
        }
    }

    fun onSearchClicked() {
        confirmedFilter = state.value.filterText
        _state.update { it.copy(accountsList = listOf()) }
        paginator.reset()
        loadNextItems()
    }

    fun loadNextItems() {
        viewModelScope.launch {
            paginator.loadNextItems()
        }
    }

    fun onAccountClicked(accountNumber: String) {
        viewModelScope.launch {
            _event.send(AccountListEvent.NavigateToAccountDetail(accountNumber))
        }
    }

    private fun accountListToAccountItemList(accountsList: List<Account>): List<AccountListItem> {
        return accountsList.map {
            AccountListItem(it.name, it.accountNumber)
        }
    }

}


