package com.example.jobs_ui.ui_stateholders

interface FieldManager<T> {
    val fieldId: String

    val fieldType: FieldType

    fun isValid(): Boolean

    fun getDataToSubmit(): T

    fun showErrors()

}

enum class FieldType {
    INPUT_TEXT_FIELD,
    DATE_FIELD
}
