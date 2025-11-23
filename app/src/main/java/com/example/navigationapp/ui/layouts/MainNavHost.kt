package com.example.navigationapp.ui.layouts

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.common.core.utils.DEFAULT_TASK_ID
import com.example.common.ui.animation.defaultNavigationEnterTransition
import com.example.common.ui.animation.defaultNavigationExitTransition
import com.example.common.ui.animation.defaultNavigationPopEnterTransition
import com.example.common.ui.animation.defaultNavigationPopExitTransition
import com.example.jobs.ui.layout.TaskDetailsScreen
import com.example.jobs.ui.layout.TaskFormScreen
import com.example.jobs.ui.layout.TaskListScreen
import com.example.navigationapp.ui.activities.CreateTaskDestination
import com.example.navigationapp.ui.activities.HomeDestination
import com.example.navigationapp.ui.activities.TaskDetailsDestination
import com.example.navigationapp.ui.activities.TasksListDestination
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
            route = TasksListDestination.route,
            deepLinks = TasksListDestination.deepLinks,
            enterTransition = { defaultNavigationEnterTransition() },
            exitTransition = { defaultNavigationExitTransition() },
            popEnterTransition = { defaultNavigationPopEnterTransition() },
            popExitTransition = { defaultNavigationPopExitTransition() },
        ) {
            RegisterTabBackPress(navController)
            TaskListScreen { taskUiModel ->
                navController.navigate(TaskDetailsDestination.navigateWithArg(taskUiModel.id))
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
            route = TaskDetailsDestination.routeWithArgs,
            arguments = TaskDetailsDestination.arguments,
            deepLinks = TaskDetailsDestination.deepLinks,
            enterTransition = { defaultNavigationEnterTransition() },
            exitTransition = { defaultNavigationExitTransition() },
            popEnterTransition = { defaultNavigationPopEnterTransition() },
            popExitTransition = { defaultNavigationPopExitTransition() },
        ) { navBackStackEntry ->
            val jobId = navBackStackEntry.arguments?.getInt(TaskDetailsDestination.taskIdArg) ?: DEFAULT_TASK_ID
            TaskDetailsScreen(jobId = jobId)
        }

        composable(
            route = CreateTaskDestination.route,
            deepLinks = CreateTaskDestination.deepLinks,
            enterTransition = { defaultNavigationEnterTransition() },
            exitTransition = { defaultNavigationExitTransition() },
            popEnterTransition = { defaultNavigationPopEnterTransition() },
            popExitTransition = { defaultNavigationPopExitTransition() },
        ) {
            TaskFormScreen()
        }
    }
}

@Composable
fun RegisterTabBackPress(navController: NavHostController) {
    BackHandler(enabled = true) {
        navController.navigateSingleTopTo(HomeDestination.route)
    }
}
