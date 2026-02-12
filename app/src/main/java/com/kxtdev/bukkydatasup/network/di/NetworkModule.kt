package com.kxtdev.bukkydatasup.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.kxtdev.bukkydatasup.BuildConfig
import com.kxtdev.bukkydatasup.network.api.ApiService
import com.kxtdev.bukkydatasup.network.api.AuthService
import com.kxtdev.bukkydatasup.network.utils.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun okHttpCallFactory(): Call.Factory = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor()
                .apply {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                }
        ).build()

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideApiService(
        okhttpCallFactory: Call.Factory,
        networkJson: Json,
        interceptor: AuthInterceptor,
    ): ApiService = retrofitBuilder {
        it.baseUrl(BASE_URL)
            .callFactory(okhttpCallFactory)
            .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
            .client(okhttpClient(interceptor))
    }.build().create(ApiService::class.java)

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideAuthService(
        okhttpCallFactory: Call.Factory,
        networkJson: Json,
    ): AuthService = retrofitBuilder {
        it.baseUrl(BASE_URL)
            .callFactory(okhttpCallFactory)
            .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
    }.build().create(AuthService::class.java)


}

private fun retrofitBuilder(block: (Retrofit.Builder) -> Retrofit.Builder): Retrofit.Builder {
    return block(Retrofit.Builder())
}

private fun okhttpClient(interceptor: AuthInterceptor): OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(interceptor)
    .readTimeout(1, TimeUnit.MINUTES)
    .writeTimeout(1, TimeUnit.MINUTES)
    .build()