package ru.whoame.recogniser

import org.koin.dsl.module
import ru.whoame.recogniser.data.database.databaseModule
import ru.whoame.recogniser.data.datasource.datasourceModule
import ru.whoame.recogniser.data.repository.repositoryModule
import ru.whoame.recogniser.ui.dialog.deleteRecognisedItemModule
import ru.whoame.recogniser.ui.screen.recognisedlist.recognisedListModule

val appModule = module {
    includes(
        databaseModule,
        datasourceModule,
        repositoryModule,
        recognisedListModule,
        deleteRecognisedItemModule,
    )
}
