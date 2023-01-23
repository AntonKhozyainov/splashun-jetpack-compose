package ru.khozyainov.splashun.di

import android.app.Application
import android.net.Uri
import androidx.core.net.toUri
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.openid.appauth.*

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    //TODO
    private const val OAUTH_URL = "https://unsplash.com/oauth/authorize"
    private const val TOKEN_URL = "https://unsplash.com/oauth/token"
    private const val OAUTH_SCOPE = "public read_user write_user read_photos write_photos write_likes write_followers read_collections write_collections"
    private const val CLIENT_ID = "AHq1b4SZZH9yINyeA_0julT7NhY-GAbea7Dry3FklN0"
    private const val REDIRECT_URI = "ru.khozyainov.splashun://khozyainov.ru/callback"
    private const val LOGOUT_REDIRECT_URI = "ru.khozyainov.splashun://khozyainov.ru/logout_callback"
    private const val RESPONSE_TYPE = ResponseTypeValues.CODE

    //Для открытия страницы авторизации в CCT нужен интент.
    //Для этого получаем AuthorizationRequest на основе заполненных раньше данных в AuthConfig
    @Provides
    fun providesServiceConfiguration(): AuthorizationServiceConfiguration =
        AuthorizationServiceConfiguration(
            Uri.parse(OAUTH_URL),
            Uri.parse(TOKEN_URL),
            null,
            //Uri.parse(TOKEN_REVOKE_URL)
        )

    @Provides
    fun providesAuthRequest(
        serviceConfiguration: AuthorizationServiceConfiguration
    ): AuthorizationRequest.Builder = AuthorizationRequest.Builder(
        serviceConfiguration,
        CLIENT_ID,
        RESPONSE_TYPE,
        REDIRECT_URI.toUri()
    ).setScope(OAUTH_SCOPE)

    //Для работы с CCT и выполнения автоматических операций обмена кода на токен библиотека
    //AppAuth предоставляет сущность AuthorizationService. Эта сущность создается при входе на экран.
    //При выходе с экрана она должна очиститься.
    @Provides
    fun providesAuthorizationService(context: Application): AuthorizationService =
        AuthorizationService(context)

    @Provides
    fun providesEndSessionRequest(
        serviceConfiguration: AuthorizationServiceConfiguration
    ): EndSessionRequest {
        return EndSessionRequest.Builder(serviceConfiguration)
            .setPostLogoutRedirectUri(LOGOUT_REDIRECT_URI.toUri())
            .build()
    }

}