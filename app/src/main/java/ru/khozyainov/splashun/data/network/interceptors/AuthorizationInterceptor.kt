package ru.khozyainov.splashun.data.network.interceptors

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import ru.khozyainov.splashun.data.repository.auth.AuthRepository
import javax.inject.Inject

class AuthorizationInterceptor @Inject constructor(
    private val authRepository: AuthRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        //Из цепочки зпросов получаем который пришел к этому интерцептору
        //модифицируем и оправляем в цепочку запросов
        val originalRequest = chain.request()

        val modifiedRequest = originalRequest.newBuilder()
            .apply {
                val token = runBlocking {
                    authRepository.getTokenFlow().first()
                }
                if (token != null) {
                    //header("Accept-Version", "v1")
                    header("Authorization", "Bearer $token")
                }
            }
            .build()

        return chain.proceed(modifiedRequest)
    }

}
