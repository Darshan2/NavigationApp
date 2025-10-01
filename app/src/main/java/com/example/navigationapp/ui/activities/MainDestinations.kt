package com.example.navigationapp.ui.activities

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDeepLink
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.common_core.utils.DEFAULT_JOB_ID
import com.example.navigationapp.R

interface MainDestination {
    val icon: ImageVector
    @get:StringRes
    val titleResId: Int

    val route: String
    val deepLinks: List<NavDeepLink>
}

object HomeDestination : MainDestination {
    override val icon = Icons.Default.Home
    override val titleResId: Int = R.string.home_tab_title
    override val route: String = "home"
    override val deepLinks = listOf (
        navDeepLink { uriPattern = "lokal://getlokalapp.com/main_screen/$route" }
    )
}

object JobsDestination : MainDestination {
    override val icon = Icons.Default.Search
    override val titleResId: Int = R.string.job_tab_title
    override val route: String = "jobs"
    override val deepLinks = listOf (
        navDeepLink { uriPattern = "lokal://getlokalapp.com/main_screen/$route" }
    )
}

object JobDetailsDestination : MainDestination {
    override val icon = Icons.Default.Search // Not used
    override val titleResId: Int = R.string.job_tab_title
    override val route: String = "job_details"
    override val deepLinks = listOf (
        navDeepLink { uriPattern = "lokal://getlokalapp.com/main_screen/$route/{${jobIdArg}}" }
    )
    val jobIdArg = "jobId"
    val routeWithArgs = "${route}/{${jobIdArg}}" // ex: job_details/101

    val arguments = listOf (
        navArgument(jobIdArg) {
            type = NavType.IntType
            defaultValue = DEFAULT_JOB_ID
        }
    )

    fun navigateWithArg(jobId: Int): String {
        return "$route/$jobId"
    }
}

object ProfileDestination : MainDestination {
    override val icon = Icons.Default.AccountCircle
    override val titleResId: Int = R.string.profile_tab_title
    override val route: String = "profile"
    override val deepLinks = listOf (
        navDeepLink { uriPattern = "lokal://getlokalapp.com/main_screen/$route" }
    )
}

val mainBottomNavDestinations = listOf(HomeDestination, JobsDestination, ProfileDestination)