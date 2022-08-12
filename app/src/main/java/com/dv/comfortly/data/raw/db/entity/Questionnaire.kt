package com.dv.comfortly.data.raw.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverters
import com.dv.comfortly.data.raw.db.adapters.QuestionnaireTypeAdapter

@Entity(
    tableName = "questionnaire",
    foreignKeys = [
        ForeignKey(
            entity = Trip::class,
            parentColumns = ["id"],
            childColumns = ["trip_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
@TypeConverters(QuestionnaireTypeAdapter::class)
data class Questionnaire(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "trip_id") val tripId: Long,
    @ColumnInfo(name = "type") val questionnaireType: QuestionnaireType
)

@TypeConverters(QuestionnaireTypeAdapter::class)
data class QuestionnaireWithQuestions(
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "trip_id") val tripId: Long,
    @ColumnInfo(name = "type") val questionnaireType: QuestionnaireType,
    @Relation(
        parentColumn = "id",
        entityColumn = "questionnaire_id",
        entity = QuestionAnswer::class
    ) val questionsWithAnswers: List<QuestionAnswer>
)

enum class QuestionnaireType {
    PRE_DEMOGRAPHIC,
    PRE_SPECIFIC,
    PRE_TRIP_PANAS,
    POST_TRIP_PANAS,
    POST_SPECIFIC,
}
