package com.kr8ne.mensMorris.ui.impl

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.kr8ne.mensMorris.R
import com.kr8ne.mensMorris.SEARCHING_ONLINE_GAME_SCREEN
import com.kr8ne.mensMorris.SIGN_UP_SCREEN
import com.kr8ne.mensMorris.api.Client
import com.kr8ne.mensMorris.api.Client.networkScope
import com.kr8ne.mensMorris.api.ServerResponse
import com.kr8ne.mensMorris.common.utils.AppTheme
import com.kr8ne.mensMorris.ui.interfaces.ScreenModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
/**
 * Represents a screen for signing in to the application.
 *
 * @param navController The navigation controller for navigating between screens.
 * @param loginValidator A function that validates the provided login.
 * @param passwordValidator A function that validates the provided password.
 */
class SignInScreen(
    /**
     * navigation controller
     */
    val navController: NavHostController,
    /**
     * Validates the provided login.
     *
     * @return True if the login is valid, false otherwise.
     */
    val loginValidator: (String) -> Boolean,
    /**
     * Validates the provided password.
     *
     * @return True if the password is valid, false otherwise.
     */
    val passwordValidator: (String) -> Boolean
) : ScreenModel {
    @Composable
    override fun InvokeRender() {
        AppTheme {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val serverResponse = remember { mutableStateOf<Result<ServerResponse>?>(null) }
                val requestInProcess = remember { mutableStateOf(false) }
                val isUsernameValid = remember { mutableStateOf(false) }
                val usernameOrEmail = remember { mutableStateOf("") }
                // TODO: FIX THIS, YOU ARE GOING TO REGRET THIS SOOOOO MUCH
                if (serverResponse.value != null) {
                    when (serverResponse.value?.getOrNull()) {
                        is ServerResponse.Success -> {
                            navController.navigate(SEARCHING_ONLINE_GAME_SCREEN)
                        }

                        is ServerResponse.WrongPasswordOrLogin -> {
                            Text(text = "Wrong password or login")
                        }

                        is ServerResponse.ServerError -> {
                            Text(text = "Server error, pls try later or report this at GitHub")
                        }

                        null -> {
                            Text(text = "Network request has failed, check your internet connection")
                        }

                        else -> {
                            error("Unknown server response")
                        }
                    }
                }
                Box {
                    Row {
                        Icon(
                            painter = painterResource(id = R.drawable.username), "your username"
                        )
                        TextField(usernameOrEmail.value, { newValue ->
                            usernameOrEmail.value = newValue
                            Client.updateUserLogin(newValue)
                            isUsernameValid.value = loginValidator(usernameOrEmail.value)
                        }, label = {
                            Row {
                                if (!isUsernameValid.value) {
                                    Text(
                                        "Your login is invalid",
                                        modifier = Modifier,
                                        color = Color.Red,
                                        fontSize = 12.sp
                                    )
                                } else {
                                    Text(
                                        "Your login is valid",
                                        modifier = Modifier,
                                        color = Color.Green,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }, placeholder = { Text("Username") })
                    }
                }
                val isPasswordValid = remember { mutableStateOf(false) }
                val password = remember { mutableStateOf("") }
                Box {
                    Row {
                        Icon(
                            painter = painterResource(id = R.drawable.password), "your password"
                        )
                        TextField(password.value, { newValue ->
                            password.value = newValue
                            Client.updateUserPassword(newValue)
                            isPasswordValid.value = passwordValidator(password.value)
                        }, label = {
                            Row {
                                if (!isPasswordValid.value) {
                                    Text(
                                        "Your password is invalid",
                                        modifier = Modifier,
                                        color = Color.Red,
                                        fontSize = 12.sp
                                    )
                                } else {
                                    Text(
                                        "Your password is valid",
                                        modifier = Modifier,
                                        color = Color.Green,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }, placeholder = { Text("Password") })
                    }
                }
                Spacer(modifier = Modifier.height(100.dp))
                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
                        requestInProcess.value = true
                        CoroutineScope(networkScope).launch {
                            serverResponse.value = Client.login()
                            requestInProcess.value = false
                        }
                    },
                    enabled = isUsernameValid.value && isPasswordValid.value && !requestInProcess.value
                ) {
                    Text("Sign in")
                }
                Spacer(modifier = Modifier.height(100.dp))
                Box {
                    Button(modifier = Modifier, onClick = {
                        navController.navigate(SIGN_UP_SCREEN)
                    }) {
                        Text("Don't have an account? Sign up")
                    }
                }
            }
        }
    }
}