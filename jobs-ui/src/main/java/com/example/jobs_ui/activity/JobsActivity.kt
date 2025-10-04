package com.example.jobs_ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.jobs_ui.layout.TaskListScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            TaskListScreen(openTask = {})
        }

    }

}


