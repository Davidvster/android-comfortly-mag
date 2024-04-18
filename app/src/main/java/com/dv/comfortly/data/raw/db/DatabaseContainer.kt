package com.dv.comfortly.data.raw.db

import android.content.Context
import androidx.room.Room
import com.dv.comfortly.data.raw.db.dao.QuestionAnswerDao
import com.dv.comfortly.data.raw.db.dao.QuestionnaireDao
import com.dv.comfortly.data.raw.db.dao.TripCalibrationDatapointDao
import com.dv.comfortly.data.raw.db.dao.TripDao
import com.dv.comfortly.data.raw.db.dao.TripDatapointDao
import com.dv.comfortly.data.raw.db.dao.TripEcgCalibrationDatapointDao
import com.dv.comfortly.data.raw.db.dao.TripEcgDatapointDao
import com.dv.comfortly.data.raw.db.repository.QuestionAnswerRepository
import com.dv.comfortly.data.raw.db.repository.QuestionnaireRepository
import com.dv.comfortly.data.raw.db.repository.TripCalibrationDatapointRepository
import com.dv.comfortly.data.raw.db.repository.TripDatapointRepository
import com.dv.comfortly.data.raw.db.repository.TripEcgCalibrationDataRepository
import com.dv.comfortly.data.raw.db.repository.TripEcgDataRepository
import com.dv.comfortly.data.raw.db.repository.TripRepository

class DatabaseContainer(
    private val context: Context,
) {
    companion object {
        private const val DATABASE_NAME = "app_db"
    }

    private val db: AppDatabase by lazy {
        Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .addMigrations(Migration1to2())
            .build()
    }

    private val tripDao: TripDao by lazy {
        db.trip()
    }

    private val tripCalibrationDatapointDao: TripCalibrationDatapointDao by lazy {
        db.tripCalibrationDatapoint()
    }

    private val tripEcgCalibrationDatapointDao: TripEcgCalibrationDatapointDao by lazy {
        db.tripEcgCalibrationDatapoint()
    }

    private val tripDatapointDao: TripDatapointDao by lazy {
        db.tripDatapoint()
    }

    private val tripEcgDatapointDao: TripEcgDatapointDao by lazy {
        db.tripEcgDatapoint()
    }

    private val questionnaireDao: QuestionnaireDao by lazy {
        db.questionnaire()
    }

    private val questionAnswerDao: QuestionAnswerDao by lazy {
        db.questionAnswer()
    }

    val tripRepository: TripRepository by lazy {
        TripRepository.Default(tripDao, tripDatapointDao)
    }

    val tripDatapointRepository: TripDatapointRepository by lazy {
        TripDatapointRepository.Default(tripDatapointDao)
    }

    val tripEcgDataRepository: TripEcgDataRepository by lazy {
        TripEcgDataRepository.Default(tripEcgDatapointDao)
    }

    val tripCalibrationDatapointRepository: TripCalibrationDatapointRepository by lazy {
        TripCalibrationDatapointRepository.Default(tripCalibrationDatapointDao)
    }

    val tripEcgCalibrationDatapointRepository: TripEcgCalibrationDataRepository by lazy {
        TripEcgCalibrationDataRepository.Default(tripEcgCalibrationDatapointDao)
    }

    val questionnaireRepository: QuestionnaireRepository by lazy {
        QuestionnaireRepository.Default(questionnaireDao)
    }

    val questionAnswerRepository: QuestionAnswerRepository by lazy {
        QuestionAnswerRepository.Default(questionAnswerDao)
    }
}
