package com.dv.comfortly.di

import com.dv.comfortly.domain.usecases.ConnectToHeartRateDeviceUseCase
import com.dv.comfortly.domain.usecases.CreateNewTripUseCase
import com.dv.comfortly.domain.usecases.DeleteTripUseCase
import com.dv.comfortly.domain.usecases.DisconnectFromHeartRateDeviceUseCase
import com.dv.comfortly.domain.usecases.ExportTripToZipUseCase
import com.dv.comfortly.domain.usecases.IsBluetoothTurnedOnUseCase
import com.dv.comfortly.domain.usecases.LoadQuestionsUseCase
import com.dv.comfortly.domain.usecases.LoadTripUseCase
import com.dv.comfortly.domain.usecases.LoadTripsUseCase
import com.dv.comfortly.domain.usecases.ObserveConnectedHeartRateDeviceUseCase
import com.dv.comfortly.domain.usecases.RecordEcgSensorDataUseCase
import com.dv.comfortly.domain.usecases.RecordSensorDataUseCase
import com.dv.comfortly.domain.usecases.SearchForHeartRateDevicesUseCase
import com.dv.comfortly.domain.usecases.StoreAnswersUseCase
import com.dv.comfortly.domain.usecases.TurnOnLocationUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface UseCasesModule {

    @Binds
    @Singleton
    fun provideSensorDataUseCase(sensorDataUseCase: RecordSensorDataUseCase.Default): RecordSensorDataUseCase

    @Binds
    @Singleton
    fun provideNewTripUseCase(newTripUseCase: CreateNewTripUseCase.Default): CreateNewTripUseCase

    @Binds
    @Singleton
    fun provideLoadTripsUseCase(loadTripsUseCase: LoadTripsUseCase.Default): LoadTripsUseCase

    @Binds
    @Singleton
    fun provideDeleteTripUseCasee(deleteTripUseCase: DeleteTripUseCase.Default): DeleteTripUseCase

    @Binds
    @Singleton
    fun provideLoadTripUseCase(loadTripUseCase: LoadTripUseCase.Default): LoadTripUseCase

    @Binds
    @Singleton
    fun provideLoadQuestionsUseCase(loadQuestionsUseCase: LoadQuestionsUseCase.Default): LoadQuestionsUseCase

    @Binds
    @Singleton
    fun provideTurnOnLocationUseCase(turnOnLocationUseCase: TurnOnLocationUseCase.Default): TurnOnLocationUseCase

    @Binds
    @Singleton
    fun provideBluetoothTurnedOnUseCase(bluetoothTurnedOnUseCase: IsBluetoothTurnedOnUseCase.Default): IsBluetoothTurnedOnUseCase

    @Binds
    @Singleton
    fun provideExportTripToZipUseCase(exportTripToZipUseCase: ExportTripToZipUseCase.Default): ExportTripToZipUseCase

    @Binds
    @Singleton
    fun provideSearchForHeartRateDevicesUseCase(
        searchForHeartRateDevicesUseCase: SearchForHeartRateDevicesUseCase.Default
    ): SearchForHeartRateDevicesUseCase

    @Binds
    @Singleton
    fun provideConnectToHeartRateDeviceUseCase(
        connectToHeartRateDeviceUseCase: ConnectToHeartRateDeviceUseCase.Default
    ): ConnectToHeartRateDeviceUseCase

    @Binds
    @Singleton
    fun provideDisconnectFromHeartRateDeviceUseCase(
        disconnectFromHeartRateDeviceUseCase: DisconnectFromHeartRateDeviceUseCase.Default
    ): DisconnectFromHeartRateDeviceUseCase

    @Binds
    @Singleton
    fun provideObserveConnectedHeartRateDevicesUseCase(
        observeConnectedHeartRateDeviceUseCase: ObserveConnectedHeartRateDeviceUseCase.Default
    ): ObserveConnectedHeartRateDeviceUseCase

    @Binds
    @Singleton
    fun provideStoreAnswersUseCase(storeAnswersUseCase: StoreAnswersUseCase.Default): StoreAnswersUseCase

    @Binds
    @Singleton
    fun provideRecordEcgSensorDataUseCase(RecordEcgSensorDataUseCase: RecordEcgSensorDataUseCase.Default): RecordEcgSensorDataUseCase
}
