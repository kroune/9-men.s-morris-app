package com.example.mensmorris.model.impl.tutorial.viewModels

import androidx.lifecycle.ViewModel
import com.example.mensmorris.data.DataModel
import com.example.mensmorris.data.impl.tutorial.data.PlacementTutorialData
import com.example.mensmorris.domain.ScreenModel
import com.example.mensmorris.domain.impl.tutorial.domain.PlacementTutorialScreen
import com.example.mensmorris.model.ViewModelInterface

/**
 * view model for tutorial on indicators
 */
class PlacementTutorialViewModel :
    ViewModelInterface, ViewModel() {

    override val data: DataModel = PlacementTutorialData(this)
    override var render: ScreenModel = PlacementTutorialScreen()
}
