package ru.whoame.recogniser.ui.screen.recognisedlist.state.factory

import ru.whoame.recogniser.ui.screen.recognisedlist.state.model.ListScreenDomainEvent
import ru.whoame.recogniser.ui.screen.recognisedlist.state.model.ListScreenState
import ru.whoame.state_machine.contract.BaseDefaultsFactory

class ListScreenDefaultsFactory : BaseDefaultsFactory<ListScreenState, ListScreenDomainEvent> {

    override fun state(): ListScreenState = ListScreenState()

    override fun domainEvents(): List<ListScreenDomainEvent> = listOf(ListScreenDomainEvent.GetRecognisedList)

}
