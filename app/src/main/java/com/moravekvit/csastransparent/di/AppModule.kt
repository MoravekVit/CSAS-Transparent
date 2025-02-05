package com.moravekvit.csastransparent.di

import com.moravekvit.csastransparent.BuildConfig
import com.moravekvit.csastransparent.data.AccountsRepositoryImpl
import com.moravekvit.csastransparent.data.remote.CSASTransparentApi
import com.moravekvit.csastransparent.repository.AccountsRepository
import com.moravekvit.csastransparent.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    @Singleton
    @Provides
    fun provideAccountsRepository(
        api: CSASTransparentApi
    ): AccountsRepository {
        return AccountsRepositoryImpl(api)
    }

    /**
     * Adding the authorization token here.
     * Since it's static and used on all of the requests, there is no need for variable.
     */
    private fun okHttpClient() = OkHttpClient().newBuilder()
        .addInterceptor(
            Interceptor { chain ->
                val request: Request = chain.request()
                    .newBuilder()
                    .header("WEB-API-key", BuildConfig.API_KEY)
                    .build()
                chain.proceed(request)
            }
        )

    /**
     * Provides CSASTransparentApi for whole app.
     *
     * @return Api Interface with already set up converter, Base URL and authentication.
     */
    @Singleton
    @Provides
    fun provideCSASTransparentApi(): CSASTransparentApi {
        return Retrofit.Builder()
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json; charset=UTF8".toMediaType()
                )
            )
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient().build())
            .build().create(CSASTransparentApi::class.java)
    }
}