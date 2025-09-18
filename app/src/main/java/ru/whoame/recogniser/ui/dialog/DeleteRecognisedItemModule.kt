package ru.whoame.recogniser.ui.dialog

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import ru.whoame.recogniser.ui.dialog.state.factory.DeleteRecognisedItemDefaultsFactory
import ru.whoame.recogniser.ui.dialog.state.factory.DeleteRecognisedItemStateFactory
import ru.whoame.recogniser.ui.dialog.state.handler.DeleteRecognisedItemDomainEventHandler
import ru.whoame.recogniser.ui.dialog.state.handler.DeleteRecognisedItemUiEventHandler

val deleteRecognisedItemModule = module {
    factory { holder -> DeleteRecognisedItemDefaultsFactory(holder.get()) }
    factory { DeleteRecognisedItemStateFactory() }
    factory { DeleteRecognisedItemUiEventHandler() }
    factory { DeleteRecognisedItemDomainEventHandler(get()) }

    viewModel { holder ->
        DeleteRecognisedItemViewModel(
            defaultsFactory = get(parameters = holder.get()),
            stateFactory = get(),
            uiEventHandler = get(),
            domainEventHandler = get(),
        )
    }
}
