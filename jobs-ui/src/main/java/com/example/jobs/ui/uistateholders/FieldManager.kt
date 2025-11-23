package com.example.jobs.ui.uistateholders

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
