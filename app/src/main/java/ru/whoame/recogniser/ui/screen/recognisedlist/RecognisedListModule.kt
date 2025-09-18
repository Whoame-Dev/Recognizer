package ru.whoame.recogniser.ui.screen.recognisedlist

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.whoame.recogniser.ui.screen.recognisedlist.state.factory.ListScreenDefaultsFactory
import ru.whoame.recogniser.ui.screen.recognisedlist.state.factory.ListScreenStateFactory
import ru.whoame.recogniser.ui.screen.recognisedlist.state.handler.ListScreenDomainEventHandler
import ru.whoame.recogniser.ui.screen.recognisedlist.state.handler.ListScreenUiEventHandler

val recognisedListModule = module {
    factory { ListScreenDefaultsFactory() }
    factory { ListScreenStateFactory() }
    factory { ListScreenUiEventHandler() }
    factory { ListScreenDomainEventHandler(get()) }

    viewModel {
        RecognisedListViewModel(
            defaultsFactory = get(),
            stateFactory = get(),
            uiEventHandler = get(),
            domainEventHandler = get(),
        )
    }
}
