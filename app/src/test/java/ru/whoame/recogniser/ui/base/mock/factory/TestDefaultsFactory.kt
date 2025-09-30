package ru.whoame.recogniser.ui.base.mock.factory

import ru.whoame.recogniser.ui.base.mock.model.TestDomainEvent
import ru.whoame.recogniser.ui.base.mock.model.TestState
import ru.whoame.state_machine.contract.BaseDefaultsFactory

class TestDefaultsFactory(
    val defaultEvents: List<TestDomainEvent>,
) : BaseDefaultsFactory<TestState, TestDomainEvent> {

    constructor(defaultEvent: TestDomainEvent) : this(listOf(defaultEvent))

    constructor(vararg events: TestDomainEvent) : this(events.toList())

    override fun state(): TestState = TestState()

    override fun domainEvents(): List<TestDomainEvent> = defaultEvents

}
