package com.example.jobs.ui.uistateholders

import com.example.common.ui.layout.ui_stateholders.UiText


data class DateState(
    val dateMillis: Long? = null,
    val error: UiText? = null
)

class DateFieldManager(
    override val fieldId: String,
    val initialDate: Long,
    val isRequired: Boolean,
): FieldManager<String> {

    override val fieldType = FieldType.DATE_FIELD

    override fun isValid(): Boolean {
        TODO("Not yet implemented")
    }

    fun onDateChange(dateMillis: Long) {

    }

    override fun getDataToSubmit(): String {
        TODO("Not yet implemented")
    }

    override fun showErrors() {
        TODO("Not yet implemented")
    }
}