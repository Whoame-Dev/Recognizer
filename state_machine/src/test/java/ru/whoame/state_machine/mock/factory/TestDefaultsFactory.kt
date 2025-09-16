package ru.whoame.state_machine.mock.factory

import ru.whoame.state_machine.contract.BaseDefaultsFactory
import ru.whoame.state_machine.mock.model.TestDomainEvent
import ru.whoame.state_machine.mock.model.TestState

class TestDefaultsFactory(
    val defaultEvents: List<TestDomainEvent> = emptyList(),
) : BaseDefaultsFactory<TestState, TestDomainEvent> {

    constructor(defaultEvent: TestDomainEvent) : this(listOf(defaultEvent))

    constructor(vararg events: TestDomainEvent) : this(events.toList())

    override fun state(): TestState = TestState()

    override fun domainEvents(): List<TestDomainEvent> = defaultEvents

}
