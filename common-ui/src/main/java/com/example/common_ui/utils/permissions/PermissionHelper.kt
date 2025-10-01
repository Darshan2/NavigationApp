package com.example.common_ui.utils.permissions

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.ComponentActivity


@SuppressLint("InlinedApi")
enum class Permission(val permissionName: String) {
    CAMERA(Manifest.permission.CAMERA),
    POST_NOTIFICATIONS(Manifest.permission.POST_NOTIFICATIONS),
}

class PermissionFactory {
    companion object {
        fun create(context: ComponentActivity, permission: Permission): PermissionBase {
            return when (permission) {
                Permission.CAMERA -> {
                    CameraPermission(context, permission)
                }

                Permission.POST_NOTIFICATIONS -> {
                    NotificationPermission(context, permission)
                }
            }
        }
    }
}
