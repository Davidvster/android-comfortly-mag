package com.dv.comfortly.data.raw.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.dv.comfortly.data.raw.db.adapters.InstantTypeAdapter
import kotlinx.datetime.Instant

@Entity(
    tableName = "question_answer",
    foreignKeys = [
        ForeignKey(
            entity = Questionnaire::class,
            parentColumns = ["id"],
            childColumns = ["questionnaire_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
)
@TypeConverters(InstantTypeAdapter::class)
data class QuestionAnswer(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "questionnaire_id") val questionnaireId: Long,
    @ColumnInfo(name = "question") val question: String,
    @ColumnInfo(name = "answer") val answer: String,
    @ColumnInfo(name = "timestamp") val timestamp: Instant,
)
