package com.example.common_ui.utils.permissions

import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat

class NotificationPermission(private val context: ComponentActivity, permission: Permission)
    : PermissionBase(context, permission) {

    override fun isPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(context, permission.permissionName) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }
}

class CameraPermission(private val context: ComponentActivity, permission: Permission)
    : PermissionBase(context, permission) {

    override fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission.permissionName
        ) == PackageManager.PERMISSION_GRANTED
    }
}