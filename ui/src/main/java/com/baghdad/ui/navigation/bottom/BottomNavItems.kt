package com.baghdad.ui.navigation.bottom

import com.baghdad.design_system.R
import com.baghdad.design_system.component.BottomNavItem
import com.baghdad.ui.navigation.route.CategoriesRoute
import com.baghdad.ui.navigation.route.Graph
import com.baghdad.ui.navigation.route.HomeRoute
import com.baghdad.ui.navigation.route.MyAccountRoute
import com.baghdad.ui.navigation.route.MyListsRoute
import com.baghdad.ui.navigation.route.SearchRoute

val BOTTOM_NAV_ITEMS =
    mapOf(
        Graph.HomeGraph to
            BottomNavItem(
                R.drawable.ic_home_filled,
                R.drawable.ic_home_outlined,
            ),
        Graph.SearchGraph to
            BottomNavItem(
                R.drawable.ic_search_filled,
                R.drawable.ic_search_outlined,
            ),
        Graph.CategoriesGraph to
            BottomNavItem(
                R.drawable.ic_masks_filled,
                R.drawable.ic_masks_outlined,
            ),
        Graph.MyListsGraph to
            BottomNavItem(
                R.drawable.ic_allbookmark_filled,
                R.drawable.ic_allbookmark_outlined,
            ),
        Graph.MyAccountGraph to
            BottomNavItem(
                R.drawable.ic_user_octagon_filled,
                R.drawable.ic_user_octagon_outlined,
            ),
    )

val TOP_LEVEL_ROUTES =
    setOf(
        HomeRoute.HomeScreen,
        SearchRoute.SearchScreen,
        CategoriesRoute.CategoriesScreen,
        MyListsRoute.MyListsScreen,
        MyAccountRoute.MyAccountScreen,
    )
