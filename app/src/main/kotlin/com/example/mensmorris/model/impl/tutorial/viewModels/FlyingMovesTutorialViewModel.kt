package com.example.mensmorris.model.impl.tutorial.viewModels

import androidx.lifecycle.ViewModel
import com.example.mensmorris.data.DataModel
import com.example.mensmorris.data.impl.tutorial.data.FlyingMovesTutorialData
import com.example.mensmorris.domain.ScreenModel
import com.example.mensmorris.domain.impl.tutorial.domain.FlyingMovesTutorialScreen
import com.example.mensmorris.model.ViewModelInterface

/**
 * view model for tutorial on indicators
 */
class FlyingMovesTutorialViewModel :
    ViewModelInterface, ViewModel() {
    override var render: ScreenModel = FlyingMovesTutorialScreen()

    override val data: DataModel = FlyingMovesTutorialData(this)
}
