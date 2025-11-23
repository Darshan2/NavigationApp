package com.example.jobs.ui.viewmodel.timer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.core.utils.Result
import com.example.common.core.utils.mapData
import com.example.common.core.utils.mapListData
import com.example.jobs.core.domain.model.Timer
import com.example.jobs.core.domain.usecases.TimerUseCase
import com.example.jobs.ui.events.TimerUiEvent
import com.example.jobs.ui.framework.AlarmChimeScheduler
import com.example.jobs.ui.mappers.timer.toDomainModel
import com.example.jobs.ui.mappers.timer.toUiModel
import com.example.jobs.ui.uimodels.timer.TimerUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TimerListingViewModel @Inject constructor(
    private val timerUseCase: TimerUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val alarmChimeScheduler: AlarmChimeScheduler
): ViewModel() {

    private val _uiEvents = MutableSharedFlow<TimerUiEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

    private val _loadTimersListTrigger = MutableSharedFlow<Unit>()
    private val _deleteTimerTrigger = MutableSharedFlow<TimerUiModel>()

    private val existingTimerStatus = _loadTimersListTrigger
        .flatMapLatest {
            timerUseCase.getTimers().mapListData { it -> it.toUiModel() }
        }.catch {
            emit(Result.Error(it))
        }

    private val deleteTimerStatus = _deleteTimerTrigger
        .flatMapLatest { timer ->
            timerUseCase.deleteTimer(timer.id).mapData { it -> timer }
        }.onEach { result ->
            if(result is Result.Success) {
                alarmChimeScheduler.ca(result.data.toDomainModel())
            }
        }
        .catch {
            emit(Result.Error(it))
        }

    val timerListState = combineTransform(
        flow = existingTimerStatus,
        flow2 = deleteTimerStatus.onStart { emit(Result.Idle) }
    ) { existingRes, deleteRes ->
        if (existingRes is Result.Success) {
            val merged = if (deleteRes is Result.Success)
                existingRes.data - deleteRes.data
            else {
                existingRes.data
            }
            if(deleteRes is Result.Error) {
                _uiEvents.emit(TimerUiEvent.OnTimerDeleteFailed)
            }
            emit(Result.Success(merged))
        } else {
            emit(existingRes)
        }
    }

    fun loadTimers() {
        viewModelScope.launch {
            _loadTimersListTrigger.emit(Unit)
        }
    }

    fun onDeleteTimer(timer: Timer) {

    }

    fun onResetTimer(timerId: Long) {

    }
}