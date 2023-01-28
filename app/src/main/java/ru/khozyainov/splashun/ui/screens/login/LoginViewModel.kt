package ru.khozyainov.splashun.ui.screens.login

import android.content.Intent
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*
import net.openid.appauth.TokenRequest
import ru.khozyainov.splashun.data.auth.AuthRepository
import ru.khozyainov.splashun.ui.LauncherViewModel
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

//    private val _uiState = MutableStateFlow<LoginState>(LoginState.Loading(loading = false))
//    val uiState: StateFlow<LoginState> = _uiState
//
//    private val loginIsDone = authRepository.getTokenFlow()
//        .map { it != null }.stateIn(viewModelScope, SharingStarted.Eagerly, false)
//
//    //Неблокирующий примитив для связи между отправителем (через SendChannel) и получателем (через ReceiveChannel)
//    private val openAuthPageEventChannel = Channel<Intent>(Channel.BUFFERED)
////    val openAuthPageFlow: Flow<Intent>
////        //receiveAsFlow() - получает канал как горячий поток
////        get() = openAuthPageEventChannel.receiveAsFlow()
//
//    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
//        _uiState.value = LoginState.Loading(false)
//        _uiState.value = LoginState.Error(throwable.message.toString())
//    }
//
//    init {
//        _uiState.value = LoginState.Success(loginIsDone = loginIsDone.value, openAuthPageIntent = openAuthPageEventChannel.receiveAsFlow())
//    }
//
//    //Открываем страницу авторизации
//    fun openLoginPage() {
//        _uiState.value = LoginState.Loading(true)
//        openAuthPageEventChannel.trySendBlocking(
//            authRepository.getLoginPageIntent()
//        ).onFailure { throwable ->
//            LoginState
//            _uiState.value = LoginState.Error(throwable?.message.toString())
//        }
//    }
//
//    fun onAuthFailed(exception: Throwable) = viewModelScope.launch(errorHandler) {
//        throw exception
//    }
//
//    fun getTokenByRequest(tokenRequest: TokenRequest) = viewModelScope.launch(errorHandler) {
//        authRepository.getTokenByRequest(tokenRequest)
//    }

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    private val loginIsDone = authRepository.getTokenFlow()
        .map { it != null }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    //Неблокирующий примитив для связи между отправителем (через SendChannel) и получателем (через ReceiveChannel)
    private val openAuthPageEventChannel = Channel<Intent>(Channel.BUFFERED)

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        _uiState.value = _uiState.value.copy(
            loading = false,
            errorMessage = throwable.message.toString()
        )
    }

    init {
        _uiState.value = _uiState.value.copy(
            loginIsDone = loginIsDone.value,
            openAuthPageIntent = openAuthPageEventChannel.receiveAsFlow(),
            loading = false,
            errorMessage = String()
        )
    }

    //Открываем страницу авторизации
    fun openLoginPage() {
        _uiState.value = _uiState.value.copy(loading = true)
        openAuthPageEventChannel.trySendBlocking(
            authRepository.getLoginPageIntent()
        ).onFailure { throwable ->
            _uiState.value = _uiState.value.copy(
                loading = false,
                errorMessage = throwable?.message.toString()
            )
        }
    }

    fun onAuthFailed(exception: Throwable) = viewModelScope.launch(errorHandler) {
        throw exception
    }

    fun getTokenByRequest(tokenRequest: TokenRequest) = viewModelScope.launch(errorHandler) {
        authRepository.getTokenByRequest(tokenRequest)
    }

//    sealed class LoginState {
//
//        data class Success(
//            val loginIsDone: Boolean,
//            val openAuthPageIntent: Flow<Intent>
//        ) : LoginState()
//
//        data class Loading(val loading: Boolean) : LoginState()
//        data class Error(val errorMessage: String) : LoginState()
//    }

    data class LoginState(
        val loginIsDone: Boolean = false,
        val openAuthPageIntent: Flow<Intent>? = null,
        val loading: Boolean = false,
        val errorMessage: String = String()
    )
}