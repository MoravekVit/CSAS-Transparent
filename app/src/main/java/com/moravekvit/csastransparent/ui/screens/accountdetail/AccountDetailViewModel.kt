package com.moravekvit.csastransparent.ui.screens.accountdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.moravekvit.csastransparent.AccountDetail
import com.moravekvit.csastransparent.domain.Account
import com.moravekvit.csastransparent.domain.Result
import com.moravekvit.csastransparent.repository.AccountsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class AccountDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: AccountsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AccountDetailState())
    val state = _state.asStateFlow()


    private val args = savedStateHandle.toRoute<AccountDetail>()

    init {
        getAccountDetail()
    }

    fun getAccountDetail() {
        _state.update {
            it.copy(isLoading = true)
        }
        val accountNumber = args.accountNumber
        viewModelScope.launch {
            try {
                val response = repository.getAccountDetail(accountNumber)
                if (response is Result.Success) {
                    _state.update {
                        it.copy(isLoading = false, account = response.data)
                    }
                }
            } catch (_: HttpException) {
                _state.update {
                    it.copy(isLoading = false)
                }
                // Account stays null, we show the error
            }
        }
    }
}

data class AccountDetailState(
    val isLoading: Boolean = false,
    val account: Account? = null
)