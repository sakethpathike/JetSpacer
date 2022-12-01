package com.sakethh.jetspacer.screens.space.rovers

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.sakethh.jetspacer.screens.space.rovers.curiosity.CuriosityRoverScreen
import com.sakethh.jetspacer.screens.space.rovers.opportunity.OpportunityRoverScreen
import com.sakethh.jetspacer.screens.space.rovers.spirit.SpiritRoverScreen

data class RoversScreen(val screenName: String, val composable: @Composable () -> Unit)
class RoversScreenVM : ViewModel() {
    val listForDrawerContent = listOf(
        RoversScreen(screenName = "Curiosity", composable = { CuriosityRoverScreen() }),
        RoversScreen(screenName = "Opportunity", composable = { OpportunityRoverScreen() }),
        RoversScreen(screenName = "Spirit", composable = { SpiritRoverScreen() }),
    )
}