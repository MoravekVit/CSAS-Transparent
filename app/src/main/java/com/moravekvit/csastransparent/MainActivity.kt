package com.moravekvit.csastransparent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.moravekvit.csastransparent.ui.screens.accountdetail.AccountDetail
import com.moravekvit.csastransparent.ui.screens.accountdetail.AccountDetailRoot
import com.moravekvit.csastransparent.ui.screens.accountlist.AccountsListRoot
import com.moravekvit.csastransparent.ui.theme.CSASTransparentTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CSASTransparentTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = AccountsList
                ) {
                    composable<AccountsList> {
                        AccountsListRoot(navController)
                    }
                    composable<AccountDetail> { navBackStackEntry ->
                        val accountDetail: AccountDetail = navBackStackEntry.toRoute()
                        AccountDetailRoot(accountDetail.accountNumber, navController)
                    }
                }
            }
        }
    }
}

@Serializable
object AccountsList

@Serializable
data class AccountDetail(val accountNumber: String)