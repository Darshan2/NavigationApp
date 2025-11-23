package com.example.jobs.ui.viewmodel.timer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.core.utils.Result
import com.example.common.core.utils.UN_DEFINED_ID_LONG
import com.example.common.core.utils.mapData
import com.example.common.core.utils.mapListData
import com.example.common.core.utils.toMins
import com.example.common.core.utils.toStateFlow
import com.example.jobs.core.domain.usecases.TimerUseCase
import com.example.jobs.ui.events.TimerUiEvent
import com.example.jobs.ui.framework.AlarmChimeScheduler
import com.example.jobs.ui.mappers.timer.toDomainModel
import com.example.jobs.ui.mappers.timer.toUiModel
import com.example.jobs.ui.uimodels.timer.TimerIntervalUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
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
class TimerCreationViewModel @Inject constructor(
    private val timerUseCase: TimerUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val alarmChimeScheduler: AlarmChimeScheduler
): ViewModel()  {

    companion object {
        const val TIMER_ID_KEY = "timer_id"
    }

    private val timerId
        get() = savedStateHandle.getStateFlow(key = TIMER_ID_KEY, initialValue = UN_DEFINED_ID_LONG)

    private val _uiEvents = MutableSharedFlow<TimerUiEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

    private val _timerName = MutableStateFlow("")
    private val _newTimeIntervals = MutableStateFlow(listOf<TimerIntervalUiModel>())

    //Modelling events don't make these as StateFlow which is used to model state
    private val _deleteTimeIntervalTrigger = MutableSharedFlow<TimerIntervalUiModel>()
    private val _saveTrigger = MutableSharedFlow<List<TimerIntervalUiModel>>()

    private val existingTimeIntervalsStatus = timerId
        .flatMapLatest { timerId ->
            timerUseCase.getTimerIntervals(timerId).mapListData { it -> it.toUiModel() }
        }
        .catch {
            emit(Result.Error(it))
        }

    private val deleteTimeIntervalStatus = _deleteTimeIntervalTrigger
        .flatMapLatest { timerInterval ->
            timerUseCase.deleteTimerInterval(timerInterval.id).mapData { it -> timerInterval }
        }
        .onEach { result ->
            if(result is Result.Success) {
                alarmChimeScheduler.cancelAlarmChime(result.data.toDomainModel())
            }
        }
        .catch {
            emit(Result.Error(it))
        }

    val timeIntervalListState = combineTransform(
        flow = existingTimeIntervalsStatus,
        flow2 = _newTimeIntervals,
        flow3 = deleteTimeIntervalStatus.onStart { emit(Result.Idle) }
    ) { existingRes, newTimeIntervals, deleteTimerIntervalRes ->
        if (existingRes is Result.Success) {
            val merged = if (deleteTimerIntervalRes is Result.Success)
                existingRes.data + newTimeIntervals - deleteTimerIntervalRes.data
            else {
                existingRes.data + newTimeIntervals
            }

            if (deleteTimerIntervalRes is Result.Error) {
                _uiEvents.emit(TimerUiEvent.OnTimerIntervalDeleteFailed)
            }
            emit(Result.Success(merged))
        } else {
            emit(existingRes)
        }
    }.toStateFlow(this, Result.Idle)


    private val saveState = _saveTrigger
        .flatMapLatest { allTimeIntervals ->
            val res = if(timerId.value > 0) {
                timerUseCase.addTimerInterval(
                    timerId = timerId.value,
                    intervals = allTimeIntervals.toDomainModel()
                )
            } else {
                timerUseCase.createTimer(
                    timerName = _timerName.value,
                    intervals = _newTimeIntervals.value.toDomainModel()
                )
            }
            res.mapData { it -> allTimeIntervals }
        }
        .onEach { result ->
            if(result is Result.Success) {
                alarmChimeScheduler.scheduleAlarmChimes(result.data.toDomainModel())
            }
        }
        .catch {
            emit(Result.Error(it))
        }
        .toStateFlow(this, Result.Idle)


    /**
     * Persists the given [timerId] into [SavedStateHandle].
     *
     * This value is critical for the Timer Creation screen to function correctly.
     * Storing it in [SavedStateHandle] ensures that the [timerId] is restored
     * automatically after process death or configuration changes.
     *
     * @param timerId The unique identifier of the timer being created or edited.
     */
    fun loadTimerIntervals(timerId: Long) {
        savedStateHandle[TIMER_ID_KEY] = timerId
    }

    fun addNewTimeInterval(mins: Int, hours: Int) {
        val newInterval = TimerIntervalUiModel(intervalInMins = toMins(hours, mins))
        _newTimeIntervals.value = _newTimeIntervals.value + newInterval
    }

    fun onTimerNameChanged(timerName: String) {
        _timerName.value = timerName
    }

    fun onSave() {
        viewModelScope.launch {
            if(alarmChimeScheduler.canScheduleAlarm()) {
                if(timeIntervalListState.value is Result.Success<List<TimerIntervalUiModel>>) {
                    _saveTrigger.emit((timeIntervalListState.value as Result.Success<List<TimerIntervalUiModel>>).data)
                } else {
                    _uiEvents.emit(TimerUiEvent.OnTimerSaveFailed)
                }
            } else {
                alarmChimeScheduler.askPermission()
            }
        }
    }

    fun onCancel() {
        _newTimeIntervals.value = emptyList()
    }

    fun onDeleteTimerInterval(timerInterval: TimerIntervalUiModel) {
        viewModelScope.launch {
            if(_newTimeIntervals.value.contains(timerInterval)) {
                _newTimeIntervals.emit(_newTimeIntervals.value - timerInterval)
            } else {
                _deleteTimeIntervalTrigger.emit(timerInterval)
            }
        }
    }

}