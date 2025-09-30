package ru.whoame.recogniser.ui.base.mock.factory

import ru.whoame.recogniser.ui.base.mock.model.TestState
import ru.whoame.recogniser.ui.base.mock.model.TestUiState
import ru.whoame.state_machine.contract.BaseStateFactory

class TestStateFactory : BaseStateFactory<TestState, TestUiState> {

    override fun convert(state: TestState) = TestUiState(state.value)

}
