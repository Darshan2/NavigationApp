package com.example.navigationapp.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.common.ui.theme.NavigationAppTheme
import com.example.navigationapp.ui.viewModels.MainViewModel
import com.example.common.ui.utils.permissions.Permission
import com.example.common.ui.utils.permissions.PermissionFactory
import com.example.navigationapp.ui.layouts.MainActivityScreen
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    private val notificationPermission =
        PermissionFactory.create(this, Permission.POST_NOTIFICATIONS)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavigationAppTheme {
                MainActivityScreen()
            }
        }
    }
}





