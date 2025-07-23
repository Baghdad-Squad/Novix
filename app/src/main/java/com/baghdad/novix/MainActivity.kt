package com.baghdad.novix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.baghdad.design_system.theme.NovixTheme
import com.baghdad.ui.feature.trendingActors.TrendingActorsScreen
import com.baghdad.ui.navigation.graph.home.HomeNavEvent
import com.baghdad.ui.navigation.route.Graph
import com.baghdad.viewmodel.people.TrendingActorViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
          val viewModel: TrendingActorViewModel = koinViewModel()
            NovixTheme {
                val navController = rememberNavController()
//                NovixNavHost(
//                    navController = navController,
//                    startDestination = Graph.HomeGraph
//                )
               TrendingActorsScreen(
                   viewModel,
                   handleNavigation = {event ->
                   when(event){
                       is HomeNavEvent.NavigateBack -> {
                           navController.popBackStack()
                       }
                       is HomeNavEvent.NavigateToActorDetails -> {
                           navController.navigate(
                               route = "${Graph.ActorDetailsGraph}/${event.actorId}"
                           )
                       }

                       HomeNavEvent.NavigateToActors -> TODO()
                       HomeNavEvent.NavigateToContinueWatching -> TODO()
                       HomeNavEvent.NavigateToLogin -> TODO()
                       is HomeNavEvent.NavigateToMovieDetails -> TODO()
                       HomeNavEvent.NavigateToMovies -> TODO()
                       HomeNavEvent.NavigateToPopularMovies -> TODO()
                       HomeNavEvent.NavigateToTopRatingMovies -> TODO()
                       is HomeNavEvent.NavigateToTvShowDetails -> TODO()
                       HomeNavEvent.NavigateToTvShows -> TODO()
                   }
               })
                //TODO
            }
        }
    }
}
