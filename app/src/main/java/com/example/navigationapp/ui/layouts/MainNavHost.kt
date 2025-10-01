package com.example.navigationapp.ui.layouts

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.common_core.utils.DEFAULT_JOB_ID
import com.example.common_ui.animation.defaultNavigationEnterTransition
import com.example.common_ui.animation.defaultNavigationExitTransition
import com.example.common_ui.animation.defaultNavigationPopEnterTransition
import com.example.common_ui.animation.defaultNavigationPopExitTransition
import com.example.jobs_ui.layout.JobsActivityScreen
import com.example.jobs_ui.layout.TaskDetailsScreen
import com.example.navigationapp.ui.activities.HomeDestination
import com.example.navigationapp.ui.activities.JobDetailsDestination
import com.example.navigationapp.ui.activities.JobsDestination
import com.example.navigationapp.ui.activities.ProfileDestination
import com.example.navigationapp.ui.layouts.extensions.navigateSingleTopTo

@Composable
fun MainNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController, startDestination = HomeDestination.route, modifier = modifier) {
        composable(
            route = HomeDestination.route,
            deepLinks = HomeDestination.deepLinks,
            enterTransition = { defaultNavigationEnterTransition() },
            exitTransition = { defaultNavigationExitTransition() },
            popEnterTransition = { defaultNavigationPopEnterTransition() },
            popExitTransition = { defaultNavigationPopExitTransition() },
        ) {
            HomeScreen()
        }
        composable(
            route = JobsDestination.route,
            deepLinks = JobsDestination.deepLinks,
            enterTransition = { defaultNavigationEnterTransition() },
            exitTransition = { defaultNavigationExitTransition() },
            popEnterTransition = { defaultNavigationPopEnterTransition() },
            popExitTransition = { defaultNavigationPopExitTransition() },
        ) {
            RegisterTabBackPress(navController)
            JobsActivityScreen { taskUiModel ->
                navController.navigate(JobDetailsDestination.navigateWithArg(taskUiModel.id))
            }
        }

        composable(
            route = ProfileDestination.route,
            deepLinks = ProfileDestination.deepLinks,
            enterTransition = { defaultNavigationEnterTransition() },
            exitTransition = { defaultNavigationExitTransition() },
            popEnterTransition = { defaultNavigationPopEnterTransition() },
            popExitTransition = { defaultNavigationPopExitTransition() },
        ) {
            RegisterTabBackPress(navController)
            ProfileScreen()
        }

        composable(
            route = JobDetailsDestination.routeWithArgs,
            arguments = JobDetailsDestination.arguments,
            deepLinks = JobDetailsDestination.deepLinks,
            enterTransition = { defaultNavigationEnterTransition() },
            exitTransition = { defaultNavigationExitTransition() },
            popEnterTransition = { defaultNavigationPopEnterTransition() },
            popExitTransition = { defaultNavigationPopExitTransition() },
        ) { navBackStackEntry ->
            val jobId = navBackStackEntry.arguments?.getInt(JobDetailsDestination.jobIdArg) ?: DEFAULT_JOB_ID
            TaskDetailsScreen(jobId = jobId)
        }
    }
}

@Composable
fun RegisterTabBackPress(navController: NavHostController) {
    BackHandler(enabled = true) {
        navController.navigateSingleTopTo(HomeDestination.route)
    }
}
