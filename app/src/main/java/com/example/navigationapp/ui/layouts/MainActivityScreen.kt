package com.example.navigationapp.ui.layouts

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.common_ui.layout.NavigationAppTopAppBar
import com.example.common_ui.theme.NavigationAppTheme
import com.example.jobs_ui.R
import com.example.navigationapp.ui.activities.CreateTaskDestination
import com.example.navigationapp.ui.activities.HomeDestination
import com.example.navigationapp.ui.activities.MainDestination
import com.example.navigationapp.ui.activities.mainDestinations
import com.example.navigationapp.ui.activities.mainTabDestinations
import com.example.navigationapp.ui.layouts.extensions.navigateSingleTopTo

const val BOTTOM_NAV_HIDE_ANIMATION_DURATION = 600 //IN MS

@Composable
fun MainActivityScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    val currentDestination by remember {
        derivedStateOf {
            val route = currentBackStackEntry.value?.destination?.route
            mainDestinations.find { it.route == route || it.routeWithArgs == route} ?: HomeDestination
        }
    }

    val currentTabIndex by remember {
        derivedStateOf {
            mainTabDestinations.indexOf(currentDestination)
        }
    }

    val showFabAndBottomBar by remember {
        derivedStateOf {
            currentTabIndex > -1
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            NavigationAppTopAppBar(modifier = Modifier,
                titleResId = currentDestination.titleResId,
                showUpBtn = currentDestination != HomeDestination,
                onUpBtnClick = {
                    onBackPressedDispatcher?.onBackPressed()
                }
            )
        },
        floatingActionButton = {
            MainFAB(
                modifier = Modifier,
                isVisible = showFabAndBottomBar,
                onFabClick = {
                    navController.navigate(CreateTaskDestination.route)
                }
            )
        },
        bottomBar = {
            MainBottomNavigationBar(
                modifier = Modifier,
                isVisible = showFabAndBottomBar,
                navController = navController,
                selectedTabIndex = currentTabIndex
            )
        }
    ) { contentPadding ->
        MainNavHost(
            navController = navController,
            modifier = Modifier.padding(contentPadding)
        )
    }
}

@Composable
fun MainBottomNavigationBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    selectedTabIndex: Int,
    isVisible: Boolean
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            animationSpec = tween(durationMillis = BOTTOM_NAV_HIDE_ANIMATION_DURATION),
            initialOffsetY = { fullHeight -> fullHeight }
        ),
        exit = slideOutVertically(
            animationSpec = tween(durationMillis = BOTTOM_NAV_HIDE_ANIMATION_DURATION),
            targetOffsetY = { fullHeight -> fullHeight }
        )
    ) {
        MainBottomNavigationBar(
            modifier = modifier,
            navController = navController,
            selectedTabIndex = selectedTabIndex
        )
    }
}

@Composable
fun MainBottomNavigationBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    selectedTabIndex: Int,
) {
    Surface(modifier = modifier
        .fillMaxWidth()
        .height(60.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        shadowElevation = 4.dp,
        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            for((index, navDestination) in mainTabDestinations.withIndex()) {
                MainBottomNavigationItem(
                    modifier = Modifier.weight(1f),
                    mainDestination = navDestination,
                    isSelected = index == selectedTabIndex,
                    onItemClick = {
                        navController.navigateSingleTopTo(navDestination.route)
                    }
                )
            }
        }
    }
}


@Composable
fun MainBottomNavigationItem(
    modifier: Modifier = Modifier,
    mainDestination: MainDestination,
    isSelected: Boolean,
    onItemClick: () -> Unit
) {
    val itemShape = RoundedCornerShape(4.dp)

    val contentColor = if(isSelected)
        MaterialTheme.colorScheme.onSecondaryContainer
    else
        MaterialTheme.colorScheme.onPrimaryContainer


    val backGround = if(isSelected) {
        Modifier
            .border(
                shape = itemShape,
                border = BorderStroke(2.dp, color = MaterialTheme.colorScheme.inversePrimary)
            )
            .background(
                shape = itemShape,
                color = MaterialTheme.colorScheme.secondaryContainer
            )
    } else {
        Modifier
    }


    Column(
        modifier = modifier
            .then(backGround)
            .clickable(onClick = onItemClick)
            .padding(4.dp)
            .wrapContentHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(modifier = Modifier.size(20.dp),
            tint = contentColor,
            imageVector = mainDestination.icon ?: Icons.Default.Add,
            contentDescription = mainDestination.route
        )
        Spacer(Modifier
            .height(4.dp)
            .fillMaxWidth()
        )
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            color = contentColor,
            style = MaterialTheme.typography.titleSmall,
            text = stringResource(id = mainDestination.titleResId),
            maxLines = 2
        )
    }
}

@Composable
fun MainFAB(modifier: Modifier = Modifier, isVisible: Boolean, onFabClick: () -> Unit) {
    AnimatedVisibility(
        modifier = modifier,
        visible = isVisible,
        enter = scaleIn(
            animationSpec = spring(
                dampingRatio = 0.6f, // Lower numbers = more bounce
                stiffness = 200f      // Higher numbers = faster
            )
        ),
        exit = scaleOut()
    ) {
        FloatingActionButton(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            onClick = onFabClick
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(id = R.string.cd_add_task) // Add to strings.xml
            )
        }
    }
}

@Composable
fun HomeTabIndicator(selectedTabIndex: Int, tabPositions: List<TabPosition>) {
    if(selectedTabIndex >= 0 && selectedTabIndex < tabPositions.size) {

        val currentTabPosition by remember(selectedTabIndex) {
            mutableStateOf(tabPositions[selectedTabIndex])
        }
        val shape by remember {
            mutableStateOf(RoundedCornerShape(size = 4.dp))
        }

        Box(
            modifier = Modifier
                .tabIndicatorOffset(currentTabPosition)
                .fillMaxHeight()
                .padding(horizontal = 4.dp)
                .border(
                    border = BorderStroke(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.outline
                    ),
                    shape = shape
                )
                .background(
                    shape = shape,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )

        )
    }

}

@Preview
@Composable
private fun MainBottomNavigationBarPreview() {
    NavigationAppTheme {
        val navController = rememberNavController()
        MainBottomNavigationBar(navController = navController, selectedTabIndex = 0)
    }
}

@Preview
@Composable
private fun MainBottomNavigationItemPreview() {
    NavigationAppTheme {
        MainBottomNavigationItem(mainDestination = HomeDestination, isSelected = true, onItemClick = {})
    }
}


@Preview
@Composable
private fun MainActivityScreenPreview() {
    NavigationAppTheme {
        MainActivityScreen()
    }
}