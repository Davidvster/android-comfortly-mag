package com.dv.comfortly.data.raw.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dv.comfortly.data.raw.db.entity.QuestionAnswer

@Dao
interface QuestionAnswerDao {
    @Insert
    suspend fun inserQuestionAnswer(model: QuestionAnswer): Long

    @Query("""SELECT * FROM question_answer WHERE id == :questionAnswerId""")
    suspend fun loadQuestionAnswer(questionAnswerId: Long): QuestionAnswer

    @Query("""SELECT * FROM question_answer""")
    suspend fun loadAll(): List<QuestionAnswer>

    @Delete
    suspend fun deleteQuestionAnswer(model: QuestionAnswer)
}
