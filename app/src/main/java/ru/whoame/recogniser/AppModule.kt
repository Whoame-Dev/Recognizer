package ru.whoame.recogniser

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.whoame.recogniser.data.database.databaseModule
import ru.whoame.recogniser.data.datasource.datasourceModule
import ru.whoame.recogniser.data.repository.repositoryModule
import ru.whoame.recogniser.ui.dialog.DeleteRecognisedItemViewModel
import ru.whoame.recogniser.ui.dialog.state.factory.DeleteRecognisedItemDefaultsFactory
import ru.whoame.recogniser.ui.dialog.state.factory.DeleteRecognisedItemStateFactory
import ru.whoame.recogniser.ui.dialog.state.handler.DeleteRecognisedItemDomainEventHandler
import ru.whoame.recogniser.ui.dialog.state.handler.DeleteRecognisedItemUiEventHandler
import ru.whoame.recogniser.ui.screen.recognisedlist.RecognisedListViewModel
import ru.whoame.recogniser.ui.screen.recognisedlist.state.factory.ListScreenDefaultsFactory
import ru.whoame.recogniser.ui.screen.recognisedlist.state.factory.ListScreenStateFactory
import ru.whoame.recogniser.ui.screen.recognisedlist.state.handler.ListScreenDomainEventHandler
import ru.whoame.recogniser.ui.screen.recognisedlist.state.handler.ListScreenUiEventHandler

val appModule = module {
    includes(
        databaseModule,
        datasourceModule,
        repositoryModule,
    )

    viewModel {
        RecognisedListViewModel(
            defaultsFactory = ListScreenDefaultsFactory(),
            stateFactory = ListScreenStateFactory(),
            uiEventHandler = ListScreenUiEventHandler(),
            domainEventHandler = ListScreenDomainEventHandler(get()),
        )
    }
    viewModel { holder ->
        DeleteRecognisedItemViewModel(
            defaultsFactory = DeleteRecognisedItemDefaultsFactory(holder.get()),
            stateFactory = DeleteRecognisedItemStateFactory(),
            uiEventHandler = DeleteRecognisedItemUiEventHandler(),
            domainEventHandler = DeleteRecognisedItemDomainEventHandler(get()),
        )
    }
}
