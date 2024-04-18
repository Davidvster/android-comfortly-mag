package com.dv.comfortly.di

import android.content.Context
import android.hardware.SensorManager
import com.dv.comfortly.data.raw.observers.AccelerometerSensorObserver
import com.dv.comfortly.data.raw.observers.GpsObserver
import com.dv.comfortly.data.raw.observers.GravitySensorObserver
import com.dv.comfortly.data.raw.observers.GyroscopeSensorObserver
import com.dv.comfortly.data.raw.observers.LinearAccelerationSensorObserver
import com.dv.comfortly.data.raw.observers.RotationVectorSensorObserver
import com.dv.comfortly.data.raw.sources.BluetoothSource
import com.dv.comfortly.data.raw.sources.sensor.GpsSource
import com.dv.comfortly.data.raw.sources.sensor.HeartRateSource
import com.dv.comfortly.data.raw.sources.sensor.SensorSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [SourceModule.Sources::class])
@InstallIn(SingletonComponent::class)
class SourceModule {
    @Provides
    @Singleton
    fun provideBluetoothSource(
        @ApplicationContext context: Context,
    ): BluetoothSource = BluetoothSource.Default(context)

    @Provides
    @Singleton
    fun provideSensorManager(
        @ApplicationContext context: Context,
    ): SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    @Provides
    @Singleton
    fun provideHearRateSource(
        @ApplicationContext context: Context,
    ): HeartRateSource = HeartRateSource.Default(context)

    @Provides
    @Singleton
    fun provideGpsSource(
        @ApplicationContext context: Context,
    ): GpsSource = GpsSource.Default(context)

    @Provides
    @Singleton
    fun provideGpsObserver(
        @ApplicationContext context: Context,
        gpsSource: GpsSource,
    ): GpsObserver = GpsObserver.Default(context, gpsSource)

    @Module
    @InstallIn(SingletonComponent::class)
    interface Sources {
        @Binds
        @Singleton
        fun provideSensorSource(sensorSource: SensorSource.Default): SensorSource

        @Binds
        @Singleton
        fun provideAccelerometerSensorObserver(observer: AccelerometerSensorObserver.Default): AccelerometerSensorObserver

        @Binds
        @Singleton
        fun provideGravitySensorObserver(observer: GravitySensorObserver.Default): GravitySensorObserver

        @Binds
        @Singleton
        fun provideGyroscopeSensorObserver(observer: GyroscopeSensorObserver.Default): GyroscopeSensorObserver

        @Binds
        @Singleton
        fun provideLinearAccelerationSensorObserver(observer: LinearAccelerationSensorObserver.Default): LinearAccelerationSensorObserver

        @Binds
        @Singleton
        fun provideRotationVectorSensorObserver(observer: RotationVectorSensorObserver.Default): RotationVectorSensorObserver
    }
}
