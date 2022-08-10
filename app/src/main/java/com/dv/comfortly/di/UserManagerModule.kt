package com.dv.comfortly.di

import android.content.Context
import com.dv.comfortly.data.managers.UserManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UserManagerModule {

    @Provides
    @Singleton
    fun provideUserManager(@ApplicationContext context: Context): UserManager = UserManager.Default(context)
}
