package com.dv.comfortly.domain.usecases

import android.content.Context
import com.dv.comfortly.R
import com.dv.comfortly.data.raw.db.repository.QuestionnaireRepository
import com.dv.comfortly.data.raw.db.repository.TripRepository
import com.dv.comfortly.domain.models.EcgDataSample
import com.dv.comfortly.domain.models.TripDatapoint
import com.dv.comfortly.domain.usecases.params.ExportTripParams
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedOutputStream
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.inject.Inject

interface ExportTripToZipUseCase : BaseUseCase.Input<ExportTripParams> {
    class Default
        @Inject
        constructor(
            @ApplicationContext private val context: Context,
            private val questionnaireRepository: QuestionnaireRepository,
            private val tripRepository: TripRepository,
        ) : ExportTripToZipUseCase {
            companion object {
                private const val DATA_DIR = "comfortly"
                private const val DATA_FILENAME = "data.zip"
                private const val CSV_FILE_SUFFIX = ".csv"

                private const val CALIBRATION_TRIP_DATA_FILE_NAME = "trip_%d_calibration_data$CSV_FILE_SUFFIX"
                private const val ECG_CALIBRATION_TRIP_DATA_FILE_NAME = "trip_%d_ecg_calibration_data$CSV_FILE_SUFFIX"
                private const val TRIP_DATA_FILE_NAME = "trip_%d_data$CSV_FILE_SUFFIX"
                private const val TRIP_ECG_DATA_FILE_NAME = "trip_%d_ecg_data$CSV_FILE_SUFFIX"

                private const val CSV_ITEM_SEPARATOR = ";"
            }

            override suspend fun invoke(input: ExportTripParams) {
                withContext(Dispatchers.IO) {
                    input.outputStream.use { dest ->
                        val zipDir = File(context.filesDir.absolutePath, DATA_DIR)
                        if (!zipDir.exists()) {
                            zipDir.mkdirs()
                        }
                        val zipFile = File(zipDir.absolutePath, DATA_FILENAME)

                        val questionnaires: List<Pair<String, String>> = getQuestionnaires(input.tripId)
                        val trip = tripRepository.loadTrip(input.tripId)

                        val calibrationTripData: Pair<String, String> =
                            String.format(CALIBRATION_TRIP_DATA_FILE_NAME, input.tripId) to getDatapointData(trip.calibrationData)
                        val ecgCalibrationTripData: Pair<String, String> =
                            String.format(ECG_CALIBRATION_TRIP_DATA_FILE_NAME, input.tripId) to getEcg(trip.ecgCalibrationData)
                        val tripData: Pair<String, String> =
                            String.format(TRIP_DATA_FILE_NAME, input.tripId) to getDatapointData(trip.data)
                        val tripEcgData: Pair<String, String> =
                            String.format(TRIP_ECG_DATA_FILE_NAME, input.tripId) to getEcg(trip.ecgData)

                        if (zipFile.exists()) {
                            zipFile.delete()
                        }

                        zipFile.createNewFile()

                        ZipOutputStream(BufferedOutputStream(dest)).use { out ->
                            questionnaires.forEach { questionnaire ->
                                out.writeCsvFile(questionnaire.first, questionnaire.second.toByteArray())
                            }
                            out.writeCsvFile(calibrationTripData.first, calibrationTripData.second.toByteArray())
                            out.writeCsvFile(ecgCalibrationTripData.first, ecgCalibrationTripData.second.toByteArray())
                            out.writeCsvFile(tripData.first, tripData.second.toByteArray())
                            out.writeCsvFile(tripEcgData.first, tripEcgData.second.toByteArray())
                        }
                    }
                }
            }

            private suspend fun getQuestionnaires(tripId: Long): List<Pair<String, String>> =
                questionnaireRepository.loadQuestionnairesForTripId(tripId).map { questionnaire ->
                    val fileName = questionnaire.questionnaireType.name + CSV_FILE_SUFFIX
                    val content =
                        listOf(
                            context.getString(R.string.question_id).escapeComma(),
                            context.getString(R.string.timestamp).escapeComma(),
                            context.getString(R.string.question).escapeComma(),
                            context.getString(R.string.answer).escapeComma(),
                        ).joinToString(separator = CSV_ITEM_SEPARATOR) + System.lineSeparator() +
                            questionnaire.questionsWithAnswers.joinToString(System.lineSeparator()) { questionAnswer ->
                                listOf(
                                    questionAnswer.id.escapeComma(),
                                    questionAnswer.timestamp.escapeComma(),
                                    questionAnswer.question.escapeComma(),
                                    questionAnswer.answer.escapeComma(),
                                ).joinToString(separator = CSV_ITEM_SEPARATOR)
                            }
                    fileName to content
                }

            private suspend fun getEcg(data: List<EcgDataSample>): String =
                listOf(
                    context.getString(R.string.datapoint_id).escapeComma(),
                    context.getString(R.string.timestamp).escapeComma(),
                    context.getString(R.string.ecg_mv).escapeComma(),
                ).joinToString(separator = CSV_ITEM_SEPARATOR) + System.lineSeparator() +
                    data.joinToString(System.lineSeparator()) { ecgDataSample ->
                        listOf(
                            ecgDataSample.id.escapeComma(),
                            ecgDataSample.timestamp.escapeComma(),
                            ecgDataSample.value.escapeComma(),
                        ).joinToString(separator = CSV_ITEM_SEPARATOR)
                    }

            private fun getDatapointData(data: List<TripDatapoint>): String =
                listOf(
                    context.getString(R.string.datapoint_id).escapeComma(),
                    context.getString(R.string.timestamp).escapeComma(),
                    context.getString(R.string.heart_rate_bpm).escapeComma(),
                    context.getString(R.string.gps_latitude).escapeComma(),
                    context.getString(R.string.gps_longitude).escapeComma(),
                    context.getString(R.string.gps_altitude).escapeComma(),
                    context.getString(R.string.gps_accuracy).escapeComma(),
                    context.getString(R.string.gps_bearing).escapeComma(),
                    context.getString(R.string.gps_bearing_accuracy_degrees).escapeComma(),
                    context.getString(R.string.gps_speed).escapeComma(),
                    context.getString(R.string.gps_speed_accuracy_meters_per_second).escapeComma(),
                    context.getString(R.string.accelerometer_x_axis_acceleration).escapeComma(),
                    context.getString(R.string.accelerometer_y_axis_acceleration).escapeComma(),
                    context.getString(R.string.accelerometer_z_axis_acceleration).escapeComma(),
                    context.getString(R.string.accelerometer_accuracy).escapeComma(),
                    context.getString(R.string.gravity_x_axis_gravity).escapeComma(),
                    context.getString(R.string.gravity_y_axis_gravity).escapeComma(),
                    context.getString(R.string.gravity_z_axis_gravity).escapeComma(),
                    context.getString(R.string.gravity_accuracy).escapeComma(),
                    context.getString(R.string.gyroscope_x_axis_rotation_rate).escapeComma(),
                    context.getString(R.string.gyroscope_y_axis_rotation_rate).escapeComma(),
                    context.getString(R.string.gyroscope_z_axis_rotation_rate).escapeComma(),
                    context.getString(R.string.gyroscope_accuracy).escapeComma(),
                    context.getString(R.string.gyroscope_orientation_x).escapeComma(),
                    context.getString(R.string.gyroscope_orientation_y).escapeComma(),
                    context.getString(R.string.gyroscope_orientation_z).escapeComma(),
                    context.getString(R.string.linear_accelerometer_x_axis_linear_acceleration).escapeComma(),
                    context.getString(R.string.linear_accelerometer_y_axis_linear_acceleration).escapeComma(),
                    context.getString(R.string.linear_accelerometer_z_axis_linear_acceleration).escapeComma(),
                    context.getString(R.string.linear_acceleration_accuracy).escapeComma(),
                    context.getString(R.string.rotation_vector_x_axis_rotation_vector).escapeComma(),
                    context.getString(R.string.rotation_vector_y_axis_rotation_vector).escapeComma(),
                    context.getString(R.string.rotation_vector_z_axis_rotation_vector).escapeComma(),
                    context.getString(R.string.rotation_vector_scalar).escapeComma(),
                    context.getString(R.string.rotation_vector_accuracy).escapeComma(),
                    context.getString(R.string.rotation_vector_orientation_x).escapeComma(),
                    context.getString(R.string.rotation_vector_orientation_y).escapeComma(),
                    context.getString(R.string.rotation_vector_orientation_z).escapeComma(),
                ).joinToString(separator = CSV_ITEM_SEPARATOR) + System.lineSeparator() +
                    data.joinToString(System.lineSeparator()) { tripDatapoint ->
                        listOf(
                            tripDatapoint.id.escapeComma(),
                            tripDatapoint.timestamp.escapeComma(),
                            tripDatapoint.heartRateData.heartRate.escapeComma(),
                            tripDatapoint.gpsData.latitude.escapeComma(),
                            tripDatapoint.gpsData.longitude.escapeComma(),
                            tripDatapoint.gpsData.altitude.escapeComma(),
                            tripDatapoint.gpsData.accuracy.escapeComma(),
                            tripDatapoint.gpsData.bearing.escapeComma(),
                            tripDatapoint.gpsData.bearingAccuracyDegrees.escapeComma(),
                            tripDatapoint.gpsData.speed.escapeComma(),
                            tripDatapoint.gpsData.speedAccuracyMetersPerSecond.escapeComma(),
                            tripDatapoint.sensorData.accelerometerData.xAxisAcceleration.escapeComma(),
                            tripDatapoint.sensorData.accelerometerData.yAxisAcceleration.escapeComma(),
                            tripDatapoint.sensorData.accelerometerData.zAxisAcceleration.escapeComma(),
                            tripDatapoint.sensorData.accelerometerData.accuracy?.escapeComma(),
                            tripDatapoint.sensorData.gravityData.xAxisGravity.escapeComma(),
                            tripDatapoint.sensorData.gravityData.yAxisGravity.escapeComma(),
                            tripDatapoint.sensorData.gravityData.zAxisGravity.escapeComma(),
                            tripDatapoint.sensorData.gravityData.accuracy?.escapeComma(),
                            tripDatapoint.sensorData.gyroscopeData.xAxisRotationRate.escapeComma(),
                            tripDatapoint.sensorData.gyroscopeData.yAxisRotationRate.escapeComma(),
                            tripDatapoint.sensorData.gyroscopeData.zAxisRotationRate.escapeComma(),
                            tripDatapoint.sensorData.gyroscopeData.accuracy?.escapeComma(),
                            tripDatapoint.sensorData.gyroscopeData.orientationX.escapeComma(),
                            tripDatapoint.sensorData.gyroscopeData.orientationY.escapeComma(),
                            tripDatapoint.sensorData.gyroscopeData.orientationZ.escapeComma(),
                            tripDatapoint.sensorData.linearAccelerometerData.xAxisLinearAcceleration.escapeComma(),
                            tripDatapoint.sensorData.linearAccelerometerData.yAxisLinearAcceleration.escapeComma(),
                            tripDatapoint.sensorData.linearAccelerometerData.zAxisLinearAcceleration.escapeComma(),
                            tripDatapoint.sensorData.linearAccelerometerData.accuracy?.escapeComma(),
                            tripDatapoint.sensorData.rotationVectorData.xAxisRotationVector.escapeComma(),
                            tripDatapoint.sensorData.rotationVectorData.yAxisRotationVector.escapeComma(),
                            tripDatapoint.sensorData.rotationVectorData.zAxisRotationVector.escapeComma(),
                            tripDatapoint.sensorData.rotationVectorData.rotationVectorScalar.escapeComma(),
                            tripDatapoint.sensorData.rotationVectorData.accuracy?.escapeComma(),
                            tripDatapoint.sensorData.rotationVectorData.orientationX.escapeComma(),
                            tripDatapoint.sensorData.rotationVectorData.orientationY.escapeComma(),
                            tripDatapoint.sensorData.rotationVectorData.orientationZ.escapeComma(),
                        ).joinToString(separator = CSV_ITEM_SEPARATOR)
                    }

            private fun ZipOutputStream.writeCsvFile(
                fileName: String,
                data: ByteArray,
            ) {
                val dataEntry = ZipEntry(fileName)
                putNextEntry(dataEntry)
                write(data, 0, data.size)
            }

            private fun Any.escapeComma() = "\"" + this.toString().replace("\"", "\"\"") + "\""
        }
}
