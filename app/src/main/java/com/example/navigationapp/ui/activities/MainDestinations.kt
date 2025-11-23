package com.example.navigationapp.ui.activities

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDeepLink
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.common.core.utils.DEFAULT_TASK_ID
import com.example.navigationapp.R

interface MainDestination {
    val icon: ImageVector?
    @get:StringRes
    val titleResId: Int

    val route: String
    val routeWithArgs: String?
    val deepLinks: List<NavDeepLink>
}


object HomeDestination : MainDestination {
    override val icon = Icons.Default.Home
    override val titleResId: Int = R.string.home_tab_title
    override val route: String = "home"
    override val routeWithArgs: String? = null

    override val deepLinks = listOf (
        navDeepLink { uriPattern = "lokal://getlokalapp.com/main_screen/$route" }
    )
}

object TasksListDestination : MainDestination {
    override val icon = Icons.AutoMirrored.Filled.List
    override val titleResId: Int = R.string.task_tab_title
    override val route: String = "tasks"
    override val routeWithArgs: String? = null
    override val deepLinks = listOf(
        navDeepLink { uriPattern = "lokal://getlokalapp.com/main_screen/$route" }
    )
}

object ProfileDestination : MainDestination {
    override val icon = Icons.Default.AccountCircle
    override val titleResId: Int = R.string.profile_tab_title
    override val route: String = "profile"
    override val routeWithArgs: String? = null
    override val deepLinks = listOf (
        navDeepLink { uriPattern = "lokal://getlokalapp.com/main_screen/$route" }
    )
}

object TaskDetailsDestination : MainDestination {
    override val icon = null
    override val titleResId: Int = R.string.task_detail_destination_title
    override val route: String = "task_details"
    override val deepLinks = listOf (
        navDeepLink { uriPattern = "lokal://getlokalapp.com/main_screen/$route/{${taskIdArg}}" }
    )

    val taskIdArg = "jobId"
    override val routeWithArgs = "${route}/{${taskIdArg}}" // ex: task_details/101

    val arguments = listOf (
        navArgument(taskIdArg) {
            type = NavType.IntType
            defaultValue = DEFAULT_TASK_ID
        }
    )

    fun navigateWithArg(taskId: Int): String {
        return "$route/$taskId"
    }
}

object CreateTaskDestination : MainDestination {
    override val icon = null
    override val titleResId: Int = R.string.task_creation_destination_title
    override val route: String = "create_task"
    override val routeWithArgs: String? = null
    override val deepLinks = listOf (
        navDeepLink { uriPattern = "lokal://getlokalapp.com/main_screen/$route" }
    )
}

val mainTabDestinations = listOf(HomeDestination, TasksListDestination, ProfileDestination)

val mainDestinations = listOf(
    HomeDestination,
    TasksListDestination,
    ProfileDestination,
    TaskDetailsDestination,
    CreateTaskDestination
)
