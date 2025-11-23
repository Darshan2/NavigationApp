package com.example.jobs.ui.layout.intermittent_timer

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.common.ui.layout.NavigationAppTopAppBar
import com.example.navigationapp.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.jobs.R
import java.time.LocalTime


@Composable
fun CreateTimerScreen(modifier: Modifier = Modifier) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    Scaffold(
        modifier = modifier,
        topBar = {
            NavigationAppTopAppBar(modifier = Modifier,
                titleResId = R.string.alarm_title,
                showUpBtn = true,
                onUpBtnClick = {
                    onBackPressedDispatcher?.onBackPressed()
                }
            )
        },
    ) { contentPadding ->
        CreateTimerScreen(Modifier.padding(contentPadding))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTimerScreen(modifier: Modifier = Modifier, ) {
    var currentTime by remember {
        mutableStateOf(LocalTime.now())
    }

    Column(modifier = modifier.padding(horizontal = 8.dp)) {
        TimePicker(
            state = rememberTimePickerState(
                initialHour = currentTime.hour,
                initialMinute = currentTime.minute
            )
        )
    }
}