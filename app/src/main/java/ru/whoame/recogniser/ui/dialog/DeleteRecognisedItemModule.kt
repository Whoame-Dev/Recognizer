package ru.whoame.recogniser.ui.dialog

import org.koin.core.module.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import ru.whoame.recogniser.model.RecognisedObject
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
    val item = holder.get<RecognisedObject>()
    DeleteRecognisedItemViewModel(
      defaultsFactory = get { parametersOf(item) },
      stateFactory = get(),
      uiEventHandler = get(),
      domainEventHandler = get(),
    )
  }
}
