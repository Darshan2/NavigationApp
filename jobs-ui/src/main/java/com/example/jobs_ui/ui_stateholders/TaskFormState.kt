package com.example.jobs_ui.ui_stateholders

import com.example.common_ui.layout.ui_stateholders.UiText
import com.example.jobs_core.data.model.TaskSubmissionModel
import com.example.jobs_ui.R


enum class TaskFormFields {
    TITLE,
    DESCRIPTION,
    PLACE,
    DUE_DATE
}

fun String.toTaskFormFieldKey(): TaskFormFields? {
    return try {
        TaskFormFields.valueOf(this)
    } catch (e: IllegalArgumentException) {
        null
    }
}

class TaskFormState {
    val fields by lazy {
        mutableMapOf<TaskFormFields, FieldManager<*>>().apply {
            this[TaskFormFields.TITLE] = InputTextFieldManager(
                fieldId = TaskFormFields.TITLE.name,
                isRequired = true,
                label = UiText.StringResource(R.string.label_title),
                minChars = 10,
                maxChars = 100
            )

            this[TaskFormFields.DESCRIPTION] = InputTextFieldManager(
                fieldId = TaskFormFields.DESCRIPTION.name,
                isRequired = true,
                label = UiText.StringResource(R.string.label_description),
                maxChars = 150
            )

            this[TaskFormFields.PLACE] = InputTextFieldManager(
                fieldId = TaskFormFields.PLACE.name,
                label = UiText.StringResource(R.string.label_place),
                isRequired = false
            )
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : FieldManager<*>> get(key: TaskFormFields): T {
        return fields[key] as? T
            ?: throw IllegalArgumentException("No field found for key: $key or type mismatch.")
    }

    fun isValid(): Boolean {
        return fields.values.all { it.isValid() }
    }

    fun getDataToSubmit(): TaskSubmissionModel {
        val titleManager = get<InputTextFieldManager>(TaskFormFields.TITLE)
        val descriptionManager = get<InputTextFieldManager>(TaskFormFields.DESCRIPTION)
        val placeManager = get<InputTextFieldManager>(TaskFormFields.PLACE)
        // val dateManager = get<DateFieldManager>(TaskFormFields.DUE_DATE)

        return TaskSubmissionModel(
            title = titleManager.getDataToSubmit(),
            description = descriptionManager.getDataToSubmit(),
            place = placeManager.getDataToSubmit()
        )
    }

    fun showErrors() {
        fields.values.forEach { it.showErrors() }
    }

}