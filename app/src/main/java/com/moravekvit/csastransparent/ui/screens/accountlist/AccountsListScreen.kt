package com.moravekvit.csastransparent.ui.screens.accountlist

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.moravekvit.csastransparent.AccountDetail
import com.moravekvit.csastransparent.R
import com.moravekvit.csastransparent.ui.theme.CSASTransparentTheme
import com.moravekvit.csastransparent.ui.theme.DarkBlue
import com.moravekvit.csastransparent.ui.theme.GeorgeBlue
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AccountsListRoot(
    navController: NavController,
    viewModel: AccountListViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.event.collectLatest { event ->
                when (event) {
                    is AccountListEvent.NavigateToAccountDetail -> {
                        navController.navigate(AccountDetail(event.accountNumber))
                    }

                    AccountListEvent.DataLoadFailed -> {
                        Toast.makeText(
                            context,
                            "Nepodařilo se načíst data, zkontrolujte své připojení",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    AccountsList(
        filterText = state.filterText,
        onFilterTextChanged = {
            viewModel.onFilterTextChanged(it)
        },
        onSearchClicked = {
            viewModel.onSearchClicked()
        },
        isLoading = state.isLoading,
        accountsList = state.accountsList,
        onPaginationReached = {
            viewModel.loadNextItems()
        },
        onAccountClicked = {
            viewModel.onAccountClicked(accountNumber = it)
        },
        onTryAgainClicked = {
            viewModel.loadNextItems()
        }
    )
}

@Composable
fun AccountsList(
    filterText: String,
    onFilterTextChanged: (String) -> Unit,
    onSearchClicked: () -> Unit,
    isLoading: Boolean,
    accountsList: List<AccountListItem>,
    onPaginationReached: () -> Unit,
    onAccountClicked: (String) -> Unit,
    onTryAgainClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_animation))

    Surface(color = GeorgeBlue, modifier = modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .safeContentPadding()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,

                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                TextField(
                    value = filterText,
                    onValueChange = {
                        onFilterTextChanged(it)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = DarkBlue,
                        unfocusedTextColor = Color.Black,
                        focusedTextColor = Color.Black,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = DarkBlue
                    ),
                    label = {
                        Text("Vyhledat dle názvu...", color = Color.DarkGray)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )

                Button(
                    onClick = {
                        onSearchClicked()
                    },
                ) {
                    Text("Hledat")
                }
            }

            if (accountsList.isNotEmpty()) {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(accountsList.size) { index ->
                        if (index >= accountsList.size - 1) {
                            onPaginationReached()
                        }
                        AccountItem(
                            account = accountsList[index],
                            onAccountClicked = {
                                onAccountClicked(accountsList[index].accountNumber)
                            })
                    }
                    if (isLoading) {
                        item {
                            LottieAnimation(
                                composition,
                                iterations = LottieConstants.IterateForever,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxWidth(0.4f)
                            )
                        }
                    }
                }
            } else if (isLoading) {
                LottieAnimation(
                    composition,
                    iterations = LottieConstants.IterateForever,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth(0.4f)
                )
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        "Nepodařilo se načíst seznam účtů",
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        fontSize = 20.sp
                    )
                    Button(onClick = {
                        onTryAgainClicked()
                    }) { Text("Zkusit znovu") }
                }
            }


        }
    }
}

@Composable
fun AccountItem(
    account: AccountListItem,
    onAccountClicked: () -> Unit,
    modifier: Modifier = Modifier.padding(top = 4.dp)
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .clickable {
                onAccountClicked()
            }
    ) {
        Text(
            account.name,
            color = Color.Black,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 8.dp, top = 8.dp, end = 8.dp)
        )
        Text(account.accountNumber, color = Color.Black, modifier = Modifier.padding(8.dp))
        HorizontalDivider(color = DarkBlue, thickness = 2.dp)
    }
}

@Preview
@Composable
private fun AccountsListPreview() {
    CSASTransparentTheme {
        AccountsList(
            filterText = "Jan Novák",
            onFilterTextChanged = {},
            onSearchClicked = {},
            isLoading = true,
            accountsList = listOf(
                AccountListItem("Jan Novák", "0000-123456789"),
                AccountListItem("Jan Novák Sbírka", "0000-987654321")
            ),
            onPaginationReached = {},
            onAccountClicked = {},
            onTryAgainClicked = {}
        )
    }
}