package com.dv.comfortly.di

import com.dv.comfortly.domain.repositories.AccelerometerSensorRepository
import com.dv.comfortly.domain.repositories.BluetoothRepository
import com.dv.comfortly.domain.repositories.GpsRepository
import com.dv.comfortly.domain.repositories.GravitySensorRepository
import com.dv.comfortly.domain.repositories.GyroscopeSensorRepository
import com.dv.comfortly.domain.repositories.HeartRateRepository
import com.dv.comfortly.domain.repositories.LinearAccelerometerSensorRepository
import com.dv.comfortly.domain.repositories.RotationVectorSensorRepository
import com.dv.comfortly.domain.repositories.SensorRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun provideAccelerometerSensorRepository(accelerometerSensorRepository: AccelerometerSensorRepository): SensorRepository.Accelerometer

    @Binds
    @Singleton
    fun provideGravitySensorRepository(gravitySensorRepository: GravitySensorRepository): SensorRepository.Gravity

    @Binds
    @Singleton
    fun provideGyroscopeSensorRepository(gyroscopeSensorRepository: GyroscopeSensorRepository): SensorRepository.Gyroscope

    @Binds
    @Singleton
    fun provideLinearAccelerometerSensorRepository(
        linearAccelerometerSensorRepository: LinearAccelerometerSensorRepository,
    ): SensorRepository.LinearAcceleration

    @Binds
    @Singleton
    fun provideRotationVectorSensorRepository(
        rotationVectorSensorRepository: RotationVectorSensorRepository,
    ): SensorRepository.RotationVector

    @Binds
    @Singleton
    fun provideGpsRepository(gpsRepository: GpsRepository): SensorRepository.GpsRepository

    @Binds
    @Singleton
    fun provideHeartRateRepository(heartRateRepository: HeartRateRepository.Default): HeartRateRepository

    @Binds
    @Singleton
    fun provideBluetoothRepository(bluetoothRepository: BluetoothRepository.Default): BluetoothRepository
}
