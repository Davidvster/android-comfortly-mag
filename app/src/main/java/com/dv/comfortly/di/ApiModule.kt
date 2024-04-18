package com.dv.comfortly.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.dv.comfortly.data.ApiService
import com.dv.comfortly.data.managers.UserManager
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    companion object {
        private const val HEADER_USER_ID = "UserId"
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        userManager: UserManager,
        @ApplicationContext context: Context,
    ): Retrofit {
        val userIdInterceptor =
            Interceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                requestBuilder.addHeader(HEADER_USER_ID, userManager.getUserId())
                chain.proceed(requestBuilder.build())
            }

        val client =
            OkHttpClient.Builder()
                .addInterceptor(ChuckerInterceptor(context))
                .addInterceptor(userIdInterceptor)
                .build()

        return Retrofit.Builder()
            .addConverterFactory(Json { ignoreUnknownKeys = true }.asConverterFactory("application/json".toMediaType()))
            .baseUrl("http://52.150.35.224:80/".toHttpUrlOrNull()!!)
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    internal fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
