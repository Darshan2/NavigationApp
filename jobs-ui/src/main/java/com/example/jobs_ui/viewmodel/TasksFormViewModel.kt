package com.example.jobs_ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.common_core.utils.Result
import com.example.common_core.utils.toStateFlow
import com.example.jobs_core.data.model.TaskSubmissionModel
import com.example.jobs_core.domain.usecases.TaskUseCase
import com.example.jobs_ui.ui_stateholders.DateFieldManager
import com.example.jobs_ui.ui_stateholders.TaskFormState
import com.example.jobs_ui.ui_stateholders.InputTextFieldManager
import com.example.jobs_ui.ui_stateholders.toTaskFormFieldKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TasksFormViewModel @Inject constructor(
    private val taskUseCase: TaskUseCase
): ViewModel() {

    private val _taskFormState = MutableStateFlow(TaskFormState())
    val taskFormState = _taskFormState.asStateFlow()


    fun onTextFieldChange(key: String, newText: String) {
        val fieldKey = key.toTaskFormFieldKey() ?: return

        val inputTextFieldManager = _taskFormState.value.get<InputTextFieldManager>(fieldKey)
        inputTextFieldManager.onTextChange(newText)
    }

    fun onDateFieldChange(key: String, dateInMillis: Long) {
        val fieldKey = key.toTaskFormFieldKey() ?: return

        val dateFieldManager = _taskFormState.value.get<DateFieldManager>(fieldKey)
        dateFieldManager.onDateChange(dateInMillis)
    }

    private val _submitTrigger = MutableSharedFlow<TaskSubmissionModel>(replay = 1)
    val submitState = _submitTrigger
        .distinctUntilChanged()
        .flatMapLatest { dataToSubmit ->
            taskUseCase.createTask(dataToSubmit)
        }.toStateFlow(this, Result.Idle)

    fun submitData() {
        if(taskFormState.value.isValid()) {
            val dataToSubmit = _taskFormState.value.getDataToSubmit()
            _submitTrigger.tryEmit(dataToSubmit)
        } else {
            _taskFormState.value.showErrors()
        }
    }

}