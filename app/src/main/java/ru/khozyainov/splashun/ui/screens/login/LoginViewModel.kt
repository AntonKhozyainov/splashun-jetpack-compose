package ru.khozyainov.splashun.ui.screens.login

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*
import net.openid.appauth.TokenRequest
import ru.khozyainov.splashun.data.repository.auth.AuthRepository
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    private val loginIsDone = authRepository.getTokenFlow()
        .map { it != null }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    //Неблокирующий примитив для связи между отправителем (через SendChannel) и получателем (через ReceiveChannel)
    private val openAuthPageEventChannel = Channel<Intent>(Channel.BUFFERED)

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        setErrorState(throwable)
    }

    init {
        refreshState()
    }

    fun refreshState() {
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
            if (throwable != null) setErrorState(throwable)
        }
    }

    fun onAuthFailed(exception: Throwable) = setErrorState(exception)

    fun getTokenByRequest(tokenRequest: TokenRequest) = viewModelScope.launch(errorHandler) {
        authRepository.getTokenByRequest(tokenRequest)
    }

    private fun setErrorState(throwable: Throwable) {
        _uiState.value = _uiState.value.copy(
            loading = false,
            errorMessage = throwable.message.toString()
        )
    }

    data class LoginState(
        val loginIsDone: Boolean = false,
        val openAuthPageIntent: Flow<Intent>? = null,
        val loading: Boolean = false,
        val errorMessage: String = String()
    )
}