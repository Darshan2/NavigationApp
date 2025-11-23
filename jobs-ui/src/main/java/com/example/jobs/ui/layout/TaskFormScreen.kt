package com.example.jobs.ui.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.common.ui.theme.NavigationAppTheme
import com.example.jobs.ui.R
import com.example.jobs.ui.uistateholders.DateFieldManager
import com.example.jobs.ui.uistateholders.FieldType
import com.example.jobs.ui.uistateholders.InputTextFieldManager
import com.example.jobs.ui.uistateholders.TaskFormState
import com.example.jobs.ui.uistateholders.TextState
import com.example.jobs.ui.viewmodel.TasksFormViewModel

@Composable
fun TaskFormScreen(modifier: Modifier = Modifier) {
    val tasksFormViewModel = hiltViewModel<TasksFormViewModel>()

    val taskFormState = tasksFormViewModel.taskFormState.collectAsStateWithLifecycle()

    NavigationAppTheme {
        TaskFormScreen(
            modifier = modifier,
            taskFormState = taskFormState.value,
            onTextChange = tasksFormViewModel::onTextFieldChange,
            onSubmitClick = tasksFormViewModel::submitData,
        )
    }
}

@Composable
fun TaskFormScreen(
    modifier: Modifier = Modifier,
    taskFormState: TaskFormState,
    onTextChange: (String, String) -> Unit,
    onSubmitClick: () -> Unit
) {
    LazyColumn (
        contentPadding = PaddingValues(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        items(
            items = taskFormState.fields.values.toList(),
            key = { it.fieldId },
            contentType = { it.fieldType }
        ) {
            when(it.fieldType) {
                FieldType.INPUT_TEXT_FIELD -> {
                    val inputTextFieldManager = it as? InputTextFieldManager ?: return@items

                    TextFormField(
                        fieldState = inputTextFieldManager.textState,
                        label = inputTextFieldManager.label?.asString(),
                        onTextChange = { text ->
                            onTextChange(inputTextFieldManager.fieldId, text)
                        }
                    )
                }
                FieldType.DATE_FIELD -> {
                    val dateFieldManager = it as? DateFieldManager ?: return@items

                }
            }
        }

        item {
            SubmitBtn {
                onSubmitClick()
            }
        }
    }
}

@Composable
fun SubmitBtn(onSubmitClick: () -> Unit) {
    TextButton(
        onClick = onSubmitClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    ) {
        Text(
            text = stringResource(R.string.btn_text_submit),
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Composable
fun TextFormField(
    fieldState: TextState,
    label: String?,
    onTextChange: (String) -> Unit
) {

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = fieldState.text,
        label = {
            if(label?.isNotBlank() == true) Text(label)
        },
        onValueChange = onTextChange,
        isError = fieldState.error != null,
        supportingText = {
            if (fieldState.error != null) {
                Text(text = fieldState.error.asString())
            }
        }
    )
}

@Preview
@Composable
private fun SubmitBtnPreview() {
    NavigationAppTheme {
        SubmitBtn{}
    }
}