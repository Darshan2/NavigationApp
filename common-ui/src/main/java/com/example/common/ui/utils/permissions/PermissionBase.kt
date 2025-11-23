package com.example.common.ui.utils.permissions

import android.R
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar

abstract class PermissionBase(private val activityContext: ComponentActivity, val permission: Permission) {

    abstract fun isPermissionGranted(): Boolean

    private var activityLauncher: ActivityResultLauncher<String>? = null

    fun registerForPermissionResult(executeAfterPermission: () -> Unit) {
        return registerForPermissionResult(null, executeAfterPermission)
    }

    /**
     * Registers for the result of a runtime permission request using
     * [ActivityResultContracts.RequestPermission].
     *
     * Must be called early (e.g., `onCreate`) before the permission is requested.
     *
     * @param permissionRationale Optional lambda invoked on denial if a custom rationale UI is desired.
     *                            If null, a default Snackbar with a "Grant" action (to re-request)
     *                            is shown.
     * @param executeAfterPermission Lambda executed if the permission is granted.
     */
    fun registerForPermissionResult(permissionRationale: (() -> Unit)?, executeAfterPermission: () -> Unit){
       activityLauncher = activityContext.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                executeAfterPermission.invoke()
            } else {
                permissionRationale?.invoke() ?: run {
                    //This is the rootView of xml layouts. If we are using compose this will be null
                    val rootView: View? = activityContext.findViewById(R.id.content)
                    rootView?.let {
                        val snackBar: Snackbar = Snackbar.make(
                            rootView,
                            "Need permission to do this action",
                            Snackbar.LENGTH_INDEFINITE
                        )
                        snackBar.setAction("Grant") { _ ->
                            snackBar.dismiss()
                            launchPermissionPopUp()
                        }
                        snackBar.view.elevation = 0F
                        snackBar.show()
                    } ?: run {
                        Toast.makeText(activityContext, "Need permission to do this action", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    fun checkPermissionThenExecute(executeAfterPermission: () -> Unit) {
        checkPermissionThenExecute(null, executeAfterPermission)
    }


    /**
     * Checks if the required permission is granted. If so, executes [executeAfterPermission].
     * Otherwise, it handles requesting the permission:
     * - If rationale should be shown ([androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale] is true),
     *   invokes [permissionRationale] if provided; otherwise, directly launches the permission request.
     * - If no rationale is needed (e.g., first request or "Don't ask again" was selected),
     *   directly launches the permission request.
     *
     * Assumes `registerForPermissionResult()` has been called previously.
     *
     * @param permissionRationale Optional lambda to display a custom rationale UI. If null and
     *                            rationale is needed, the permission request is launched directly.
     * @param executeAfterPermission Lambda executed if the permission is already granted or
     *                               is subsequently granted via the request flow.
     */
    fun checkPermissionThenExecute(permissionRationale: (() -> Unit)?, executeAfterPermission: () -> Unit) {
        when {
            isPermissionGranted() -> {
                executeAfterPermission.invoke()
            }

            activityContext.shouldShowRequestPermissionRationale(permission.permissionName) -> {
                permissionRationale?.invoke() ?: run {
                    launchPermissionPopUp()
                }
            }

            else -> {
               launchPermissionPopUp()
            }
        }
    }

    private fun launchPermissionPopUp() {
        check(activityLauncher)
        activityLauncher?.launch(permission.permissionName)
    }

    private fun check(activityLauncher: ActivityResultLauncher<String>?) {
        if(activityLauncher == null) throw RuntimeException("Must call registerForPermissionResult() before onStart()")
    }
}

