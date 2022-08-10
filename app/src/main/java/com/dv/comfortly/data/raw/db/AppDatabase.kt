package com.dv.comfortly.data.raw.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dv.comfortly.data.raw.db.dao.QuestionAnswerDao
import com.dv.comfortly.data.raw.db.dao.QuestionnaireDao
import com.dv.comfortly.data.raw.db.dao.TripCalibrationDatapointDao
import com.dv.comfortly.data.raw.db.dao.TripDao
import com.dv.comfortly.data.raw.db.dao.TripDatapointDao
import com.dv.comfortly.data.raw.db.dao.TripEcgCalibrationDatapointDao
import com.dv.comfortly.data.raw.db.dao.TripEcgDatapointDao
import com.dv.comfortly.data.raw.db.entity.QuestionAnswer
import com.dv.comfortly.data.raw.db.entity.Questionnaire
import com.dv.comfortly.data.raw.db.entity.Trip
import com.dv.comfortly.data.raw.db.entity.TripCalibrationDatapoint
import com.dv.comfortly.data.raw.db.entity.TripDatapoint
import com.dv.comfortly.data.raw.db.entity.TripEcgCalibrationSample
import com.dv.comfortly.data.raw.db.entity.TripEcgSample

@Database(
    entities = [
        Trip::class,
        TripDatapoint::class,
        TripEcgSample::class,
        TripCalibrationDatapoint::class,
        TripEcgCalibrationSample::class,
        QuestionAnswer::class,
        Questionnaire::class
    ],
    version = AppDbConfig.VERSION,
    exportSchema = true
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun trip(): TripDao

    abstract fun tripDatapoint(): TripDatapointDao
    abstract fun tripEcgDatapoint(): TripEcgDatapointDao
    abstract fun tripCalibrationDatapoint(): TripCalibrationDatapointDao
    abstract fun tripEcgCalibrationDatapoint(): TripEcgCalibrationDatapointDao

    abstract fun questionnaire(): QuestionnaireDao

    abstract fun questionAnswer(): QuestionAnswerDao
}
