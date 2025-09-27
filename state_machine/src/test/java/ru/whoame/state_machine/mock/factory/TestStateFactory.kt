package ru.whoame.state_machine.mock.factory

import ru.whoame.state_machine.contract.BaseStateFactory
import ru.whoame.state_machine.mock.model.TestState
import ru.whoame.state_machine.mock.model.TestUiState

class TestStateFactory : BaseStateFactory<TestState, TestUiState> {

    override fun convert(state: TestState) = TestUiState(state.value)

}
