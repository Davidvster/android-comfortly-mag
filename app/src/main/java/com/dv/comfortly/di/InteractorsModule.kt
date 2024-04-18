package com.dv.comfortly.di

import com.dv.comfortly.data.raw.interactors.BluetoothTurnedOnInteractor
import com.dv.comfortly.data.raw.interactors.ConnectHrDeviceInteractor
import com.dv.comfortly.data.raw.interactors.ObserveEcgDataInteractor
import com.dv.comfortly.data.raw.interactors.ObserveHrDataInteractor
import com.dv.comfortly.data.raw.interactors.ObserveHrDeviceConnectedInteractor
import com.dv.comfortly.data.raw.interactors.SearchHrDevicesInteractor
import com.dv.comfortly.data.raw.interactors.TurnOnGpsInteractor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface InteractorsModule {
    @Binds
    @Singleton
    fun provideSearchHrDevicesInteractor(searchHrDevicesInteractor: SearchHrDevicesInteractor.Default): SearchHrDevicesInteractor

    @Binds
    @Singleton
    fun provideObserveHrDeviceConnectedInteractor(
        observeHrDeviceConnectedInteractor: ObserveHrDeviceConnectedInteractor.Default,
    ): ObserveHrDeviceConnectedInteractor

    @Binds
    @Singleton
    fun provideConnectHrDeviceInteractor(connectHrDeviceInteractor: ConnectHrDeviceInteractor.Default): ConnectHrDeviceInteractor

    @Binds
    @Singleton
    fun provideTurnOnGpsInteractor(turnOnGpsInteractor: TurnOnGpsInteractor.Default): TurnOnGpsInteractor

    @Binds
    @Singleton
    fun provideBluetoothTurnedOnInteractor(bluetoothTurnedOnInteractor: BluetoothTurnedOnInteractor.Default): BluetoothTurnedOnInteractor

    @Binds
    @Singleton
    fun provideObserveHrDataInteractor(observeHrDataInteractor: ObserveHrDataInteractor.Default): ObserveHrDataInteractor

    @Binds
    @Singleton
    fun provideObserveEcgDataInteractor(observeEcgDataInteractor: ObserveEcgDataInteractor.Default): ObserveEcgDataInteractor
}
