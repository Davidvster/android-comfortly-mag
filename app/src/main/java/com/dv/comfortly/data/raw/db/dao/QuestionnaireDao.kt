package com.dv.comfortly.data.raw.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dv.comfortly.data.raw.db.entity.Questionnaire
import com.dv.comfortly.data.raw.db.entity.QuestionnaireWithQuestions

@Dao
interface QuestionnaireDao {

    @Insert
    suspend fun createQuestionnaire(model: Questionnaire): Long

    @Query("""SELECT * FROM questionnaire WHERE id == :questionnaireId""")
    suspend fun loadQuestionnaire(questionnaireId: Long): QuestionnaireWithQuestions

    @Query("""SELECT * FROM questionnaire WHERE trip_id == :tripId""")
    suspend fun loadQuestionnaireForTrip(tripId: Long): List<QuestionnaireWithQuestions>

    @Query("""SELECT * FROM questionnaire""")
    suspend fun loadAll(): List<QuestionnaireWithQuestions>

    @Delete
    suspend fun deleteQuestionnaire(model: Questionnaire)
}
