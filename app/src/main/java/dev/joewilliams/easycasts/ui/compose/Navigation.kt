package dev.joewilliams.easycasts.ui.compose

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import dev.joewilliams.easycasts.R
import dev.joewilliams.easycasts.viewmodel.MainViewModel

@Composable
fun MainBottomNavigationBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    onItemClick: () -> Unit
) {
    NavigationBar(
        modifier = modifier
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        navItems.forEach {
            NavigationBarItem(
                selected = it.route == currentRoute,
                icon = {
                    Icon(
                        painter = painterResource(id = it.iconId),
                        contentDescription = stringResource(id = it.stringId)
                    )
                },
                onClick = { navController.navigate(it.route) },
                label = {
                    Text(
                        text = stringResource(id = it.stringId)
                    )
                }
            )
        }
    }
}

sealed class NavScreen(
    val route: String,
    val stringId: Int,
    val iconId: Int
) {
    data object PodcastList : NavScreen(route = "podcastList", stringId = R.string.podcasts_tab, iconId = R.drawable.ic_grid)
    data object EpisodeList : NavScreen(route = "episodeList", stringId = R.string.episodes_tab, iconId = R.drawable.ic_list)
    data object Search : NavScreen(route = "search", stringId = R.string.search_tab, iconId = R.drawable.ic_search)
}

@Composable
fun NavigationGraph(navController: NavHostController, viewModel: MainViewModel) {
    NavHost(
        navController = navController,
        startDestination = NavScreen.PodcastList.route
    ) {
        composable(route = NavScreen.PodcastList.route) {
            PodcastGrid()
        }
        composable(route = NavScreen.EpisodeList.route) {
            EpisodeDetail()
        }
        composable(route = NavScreen.Search.route) {
            Search(viewModel = viewModel)
        }
    }
}

val navItems = listOf(
    NavScreen.PodcastList,
    NavScreen.EpisodeList,
    NavScreen.Search
)