package com.example.navigationapp.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common_core.utils.toStateFlow
import com.example.navigationapp.data.repository.NewsBaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val newsRepository: NewsBaseRepository
): ViewModel() {

    var currentPage = 0

    private val _loadDataTrigger = MutableSharedFlow<Unit>()

    @OptIn(ExperimentalCoroutinesApi::class)
    val newsListUiState = _loadDataTrigger
        .conflate() //To keep the ongoing api call going when new data is requested before completion of that call
        .flatMapLatest {
            newsRepository.getNewsList(currentPage)
        }
        .toStateFlow(viewModelScope)


    fun fetchNewsList() {
        viewModelScope.launch {
            _loadDataTrigger.emit(Unit)
        }
    }

}