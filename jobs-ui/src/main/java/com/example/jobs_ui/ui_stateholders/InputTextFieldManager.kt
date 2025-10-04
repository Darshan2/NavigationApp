package com.example.jobs_ui.ui_stateholders

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.common_ui.layout.ui_stateholders.UiText
import com.example.jobs_ui.R

@Immutable
data class TextState(
    val text: String,
    val error: UiText? = null
)

class InputTextFieldManager(
    override val fieldId: String,
    val initialText: String = "",
    val label: UiText? = null,
    val isRequired: Boolean,
    val minChars: Int = if(isRequired) 1 else 0,
    val maxChars: Int = Int.MAX_VALUE
): FieldManager<String> {

    override val fieldType = FieldType.INPUT_TEXT_FIELD

    var textState: TextState by mutableStateOf(TextState(initialText))
        private set

    fun onTextChange(newText: String) {
        val overFlowErrorStateUi = if(newText.length > maxChars) {
            UiText.StringResource(R.string.error_max_limit, listOf(maxChars))
        } else {
            null
        }

        textState = textState.copy(
            text = newText,
            error = overFlowErrorStateUi
        )
    }

    override fun showErrors() {
        if (!isRequired) return

        val currentText = textState.text

        val errorStateUi = when {
            currentText.isBlank() -> {
                UiText.StringResource(R.string.error_blank)
            }
            currentText.length < minChars -> {
                UiText.StringResource(R.string.error_min_limit, listOf(minChars))
            }
            currentText.length > maxChars -> {
                UiText.StringResource(R.string.error_max_limit, listOf(maxChars))
            }
            else -> { null }
        }

        textState = textState.copy(
            error = errorStateUi
        )
    }

    override fun isValid(): Boolean {
        return textState.text.let {
            it.isNotBlank() && it.length >= minChars && it.length <= maxChars
        }
    }

    override fun getDataToSubmit(): String {
        return textState.text
    }

}