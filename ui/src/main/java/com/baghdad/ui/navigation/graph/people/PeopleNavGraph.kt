package com.baghdad.ui.navigation.graph.people

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.baghdad.ui.feature.people.PeopleScreen
import com.baghdad.ui.navigation.route.Graph
import com.baghdad.ui.navigation.route.PeopleRoute

fun NavGraphBuilder.peopleNavGraph(navController: NavHostController) {
    navigation<Graph.PeopleGraph>(
        startDestination = PeopleRoute.PeopleScreen
    ){
        composable<PeopleRoute.PeopleScreen> {
            PeopleScreen{ event ->
                handlePeopleNavigation(event, navController)
            }
        }
    }
}

private fun handlePeopleNavigation(
    event: PeopleNavEvent,
    navController: NavHostController
) {
    when (event) {
        PeopleNavEvent.NavigateBack -> navController.popBackStack()
        is PeopleNavEvent.NavigateToActorDetails -> navController.navigate(
            Graph.ActorDetailsGraph(event.actorId)
        )
    }

}