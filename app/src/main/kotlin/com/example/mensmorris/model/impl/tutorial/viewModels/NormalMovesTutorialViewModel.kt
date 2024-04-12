package com.example.mensmorris.model.impl.tutorial.viewModels

import androidx.lifecycle.ViewModel
import com.example.mensmorris.data.DataModel
import com.example.mensmorris.data.impl.tutorial.data.NormalMovesTutorialData
import com.example.mensmorris.domain.ScreenModel
import com.example.mensmorris.domain.impl.tutorial.domain.NormalMovesTutorialScreen
import com.example.mensmorris.model.ViewModelInterface

/**
 * view model for tutorial on indicators
 */
class NormalMovesTutorialViewModel :
    ViewModelInterface, ViewModel() {

    override val data: DataModel = NormalMovesTutorialData(this)
    override var render: ScreenModel = NormalMovesTutorialScreen()
}
