package com.dv.comfortly.data

import com.dv.comfortly.data.models.analyzedtrip.AnalyzedTripData
import com.dv.comfortly.data.models.analyzedtrip.SummaryAnalyzedTripData
import com.dv.comfortly.data.models.answer.AnswerData
import com.dv.comfortly.data.models.trip.TripData
import com.dv.comfortly.data.raw.models.QuestionnaireData
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

//    @GET("http://10.0.2.2:8083/v1/analyzed-trips/summary")
    @GET("/analyzed-trip-catalog/v1/analyzed-trips/summary")
    suspend fun getTrips(): List<SummaryAnalyzedTripData>

//    @GET("http://10.0.2.2:8083/v1/analyzed-trips/details/{analyzedTripId}")
    @GET("/analyzed-trip-catalog/v1/analyzed-trips/details/{analyzedTripId}")
    suspend fun getTripsDetails(@Path("analyzedTripId") type: Int): AnalyzedTripData

//    @POST("http://10.0.2.2:8081/v1/trips")
    @POST("/trip-catalog/v1/trips")
    suspend fun addTrip(@Body tripData: TripData): TripData

//    @GET("http://10.0.2.2:8082/v1/questionnaire")
    @GET("/questionnaire-catalog/v1/questionnaire")
    suspend fun getQuestionnaire(): List<QuestionnaireData>

//    @POST("http://10.0.2.2:8082/v1/answers")
    @POST("/questionnaire-catalog/v1/answers")
    suspend fun postAnswers(@Body answersData: List<AnswerData>)
}
