package ru.whoame.recogniser.ui.viewmodel.mock.factory

import ru.whoame.recogniser.ui.viewmodel.contracts.BaseStateFactory
import ru.whoame.recogniser.ui.viewmodel.mock.model.TestState
import ru.whoame.recogniser.ui.viewmodel.mock.model.TestUiState

class TestStateFactory : BaseStateFactory<TestState, TestUiState> {

    override fun convert(state: TestState) = TestUiState(state.value)

}
