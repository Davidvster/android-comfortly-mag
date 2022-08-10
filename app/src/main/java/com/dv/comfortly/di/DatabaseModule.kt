package com.dv.comfortly.di

import android.content.Context
import com.dv.comfortly.data.raw.db.DatabaseContainer
import com.dv.comfortly.data.raw.db.repository.QuestionAnswerRepository
import com.dv.comfortly.data.raw.db.repository.QuestionnaireRepository
import com.dv.comfortly.data.raw.db.repository.TripCalibrationDatapointRepository
import com.dv.comfortly.data.raw.db.repository.TripDatapointRepository
import com.dv.comfortly.data.raw.db.repository.TripEcgCalibrationDataRepository
import com.dv.comfortly.data.raw.db.repository.TripEcgDataRepository
import com.dv.comfortly.data.raw.db.repository.TripRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): DatabaseContainer = DatabaseContainer(context)

    @Provides
    @Singleton
    fun provideTripRepository(databaseContainer: DatabaseContainer): TripRepository = databaseContainer.tripRepository

    @Provides
    @Singleton
    fun provideTripCalibrationDatapointRepository(
        databaseContainer: DatabaseContainer
    ): TripCalibrationDatapointRepository = databaseContainer.tripCalibrationDatapointRepository

    @Provides
    @Singleton
    fun provideTripEcgCalibrationDatapointRepository(
        databaseContainer: DatabaseContainer
    ): TripEcgCalibrationDataRepository = databaseContainer.tripEcgCalibrationDatapointRepository

    @Provides
    @Singleton
    fun provideTripDatapointRepository(
        databaseContainer: DatabaseContainer
    ): TripDatapointRepository = databaseContainer.tripDatapointRepository

    @Provides
    @Singleton
    fun provideTripEcgDatapointRepository(
        databaseContainer: DatabaseContainer
    ): TripEcgDataRepository = databaseContainer.tripEcgDataRepository

    @Provides
    @Singleton
    fun provideQuestionnaireRepository(
        databaseContainer: DatabaseContainer
    ): QuestionnaireRepository = databaseContainer.questionnaireRepository

    @Provides
    @Singleton
    fun provideQuestionAnswerRepository(
        databaseContainer: DatabaseContainer
    ): QuestionAnswerRepository = databaseContainer.questionAnswerRepository
}
