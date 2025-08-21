package ru.whoame.recogniser.ui.viewmodel.mock.factory

import ru.whoame.recogniser.ui.viewmodel.contracts.BaseDefaultsFactory
import ru.whoame.recogniser.ui.viewmodel.mock.model.TestDomainEvent
import ru.whoame.recogniser.ui.viewmodel.mock.model.TestState

class TestDefaultsFactory(
    val defaultEvents: List<TestDomainEvent>,
) : BaseDefaultsFactory<TestState, TestDomainEvent> {

    constructor(defaultEvent: TestDomainEvent) : this(listOf(defaultEvent))

    constructor(vararg events: TestDomainEvent) : this(events.toList())

    override fun state(): TestState = TestState()

    override fun domainEvents(): List<TestDomainEvent> = defaultEvents

}
