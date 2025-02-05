package com.moravekvit.csastransparent.ui.screens.accountdetail

import android.content.ClipData
import android.content.Context.CLIPBOARD_SERVICE
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.moravekvit.csastransparent.R
import com.moravekvit.csastransparent.domain.Account
import com.moravekvit.csastransparent.ui.theme.CSASTransparentTheme
import com.moravekvit.csastransparent.ui.theme.GeorgeBlue
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun AccountDetailRoot(
    accountNumber: String,
    navController: NavController,
    viewModel: AccountDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AccountDetail(
        isLoading = state.isLoading,
        account = state.account,
        onTryAgainClicked = {
            viewModel.getAccountDetail()
        }
    )
}

@Composable
fun AccountDetail(
    isLoading: Boolean,
    account: Account?,
    onTryAgainClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading_animation))

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = GeorgeBlue)
    ) {
        if (isLoading) {
            LottieAnimation(
                composition,
                iterations = LottieConstants.IterateForever,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .align(Alignment.Center)
            )
        } else {
            if (account == null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .align(Alignment.Center)
                ) {
                    Text(
                        "Nepodařilo se načíst detail účtu",
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        fontSize = 20.sp
                    )
                    Button(onClick = {
                        onTryAgainClicked()
                    }) { Text("Zkusit znovu") }
                }
            } else {
                val clipboardManager =
                    LocalContext.current.getSystemService(CLIPBOARD_SERVICE) as android.content.ClipboardManager
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                        .align(Alignment.Center)
                        .fillMaxWidth()
                ) {
                    Text(text = account.name, fontWeight = FontWeight.Medium)
                    Text(
                        text = "${account.accountNumber}/${account.bankCode}",
                        modifier = Modifier.clickable {
                            clipboardManager.setPrimaryClip(
                                ClipData.newPlainText(
                                    "Bankovní účet",
                                    "${account.accountNumber}/${account.bankCode}"
                                )
                            )
                        }
                    )
                    Text(
                        text = "IBAN: ${account.iban}",
                        modifier = Modifier.clickable {
                            clipboardManager.setPrimaryClip(
                                ClipData.newPlainText(
                                    "Bankovní účet",
                                    "${account.accountNumber}/${account.bankCode}"
                                )
                            )
                        })
                    Text(text = "Zůstatek na účtu: ${account.balance} ${account.currency}")
                    Text(
                        text = "Transparentní od ${
                            account.transparencyFrom.format(
                                DateTimeFormatter.ofPattern("MM/y")
                            )
                        } do ${
                            account.transparencyTo.format(
                                DateTimeFormatter.ofPattern("MM/y")
                            )
                        }"
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AccountDetailPreview() {
    CSASTransparentTheme {
        AccountDetail(
            isLoading = false,
            account = Account(
                accountNumber = "000-123456789",
                bankCode = "1234",
                transparencyFrom = LocalDateTime.now(),
                transparencyTo = LocalDateTime.now(),
                publicationTo = LocalDateTime.now(),
                actualizationDate = LocalDateTime.now(),
                balance = 123456.7,
                currency = "CZK",
                name = "Jan Novák",
                description = "popis",
                note = "Poznámka",
                iban = "CKZ-00001225",
                statements = listOf("Statement")
            ),
            onTryAgainClicked = {},

            )
    }
}