package com.dv.comfortly.ui.trip.questionnaire

import android.text.InputType
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dv.comfortly.R
import com.dv.comfortly.data.models.answer.AnswerData
import com.dv.comfortly.data.raw.models.QuestionType
import com.dv.comfortly.databinding.ItemQuestionMultipleChoiceBinding
import com.dv.comfortly.databinding.ItemQuestionSingleChoiceBinding
import com.dv.comfortly.databinding.ItemQuestionTextBinding
import com.dv.comfortly.ui.ext.layoutInflater
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.radiobutton.MaterialRadioButton

class QuestionsAdapter(
    private val onAnswerWritten: (questionId: Long, answer: String) -> Unit,
    private val onMultiAnswerCheckedChanged: (questionId: Long, answer: String, isChecked: Boolean) -> Unit,
) :
    ListAdapter<AnswerData, RecyclerView.ViewHolder>(
            object : DiffUtil.ItemCallback<AnswerData>() {
                override fun areItemsTheSame(
                    oldItem: AnswerData,
                    newItem: AnswerData,
                ): Boolean = oldItem.questionId == newItem.questionId

                override fun areContentsTheSame(
                    oldItem: AnswerData,
                    newItem: AnswerData,
                ): Boolean = oldItem == newItem
            },
        ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder =
        when (QuestionType.values()[viewType]) {
            QuestionType.TEXT, QuestionType.NUMBER, QuestionType.NUMBER_DECIMAL ->
                QuestionTextAdapterItemView(
                    ItemQuestionTextBinding.inflate(parent.context.layoutInflater, parent, false),
                )
            QuestionType.MULTIPLE_CHOICE ->
                QuestionMultipleChoiceAdapterItemView(
                    ItemQuestionMultipleChoiceBinding.inflate(parent.context.layoutInflater, parent, false),
                )
            QuestionType.SINGLE_CHOICE ->
                QuestionSingleChoiceAdapterItemView(
                    ItemQuestionSingleChoiceBinding.inflate(parent.context.layoutInflater, parent, false),
                )
        }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        when (holder) {
            is QuestionTextAdapterItemView -> holder.bind(getItem(position))
            is QuestionMultipleChoiceAdapterItemView -> holder.bind(getItem(position))
            is QuestionSingleChoiceAdapterItemView -> holder.bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int = QuestionType.values().indexOf(getItem(position).type)

    private inner class QuestionTextAdapterItemView(
        private val binding: ItemQuestionTextBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(question: AnswerData) =
            with(binding) {
                questionName.setText(question.question)
                questionValue.inputType =
                    when (question.type) {
                        QuestionType.NUMBER -> InputType.TYPE_CLASS_NUMBER
                        QuestionType.NUMBER_DECIMAL -> InputType.TYPE_NUMBER_FLAG_DECIMAL
                        else -> InputType.TYPE_CLASS_TEXT
                    }
                question.answer?.let { questionValue.setText(it) }
                questionValue.doAfterTextChanged {
                    onAnswerWritten(question.questionId, it.toString().trim())
                }
            }
    }

    private inner class QuestionMultipleChoiceAdapterItemView(
        private val binding: ItemQuestionMultipleChoiceBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(question: AnswerData) =
            with(binding) {
                questionName.setText(question.question)
                answers.removeAllViews()
                val checkBoxes =
                    question.possibleAnswers?.map { answer ->
                        MaterialCheckBox(root.context).apply {
                            setTextAppearance(R.style.Comfortly_BodyLarge_Medium)
                            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                            setText(answer)
                            isChecked = question.answer?.contains(text.toString(), true) ?: false
                        }
                    } ?: emptyList()
                val noneCheckbox =
                    MaterialCheckBox(root.context).apply {
                        setTextAppearance(R.style.Comfortly_BodyLarge_Medium)
                        layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                        setText(R.string.checkbox_none)
                        isChecked = question.answer?.contains(text.toString(), true) ?: false
                        setOnCheckedChangeListener { _, isChecked ->
                            if (isChecked) {
                                checkBoxes.forEach { it.isChecked = false }
                                onAnswerWritten(
                                    question.questionId,
                                    text.toString(),
                                )
                            }
                        }
                    }
                checkBoxes.forEach { checkBox ->
                    checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                        onMultiAnswerCheckedChanged(
                            question.questionId,
                            buttonView.text.toString(),
                            isChecked,
                        )
                        if (isChecked) {
                            noneCheckbox.isChecked = false
                        }
                    }
                    answers.addView(checkBox)
                }
                answers.addView(noneCheckbox)
            }
    }

    private inner class QuestionSingleChoiceAdapterItemView(
        private val binding: ItemQuestionSingleChoiceBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(question: AnswerData) =
            with(binding) {
                questionName.setText(question.question)
                singleChoiceGroup.removeAllViews()
                val radioButtons =
                    question.possibleAnswers?.map { answer ->
                        val radioButton =
                            MaterialRadioButton(root.context).apply {
                                setTextAppearance(R.style.Comfortly_BodyLarge_Medium)
                                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                                setText(answer)
                                setOnCheckedChangeListener { buttonView, isChecked ->
                                    if (isChecked) {
                                        onAnswerWritten(question.questionId, buttonView.text.toString())
                                    }
                                }
                            }
                        singleChoiceGroup.addView(radioButton)
                        radioButton
                    } ?: emptyList()
                radioButtons.forEach { radioButton ->
                    radioButton.isChecked = question.answer?.contains(radioButton.text.toString(), true) ?: false
                }
            }
    }
}
