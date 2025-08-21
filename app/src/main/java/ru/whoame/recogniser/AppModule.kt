package ru.whoame.recogniser

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.whoame.recogniser.data.database.databaseModule
import ru.whoame.recogniser.data.datasource.datasourceModule
import ru.whoame.recogniser.data.repository.repositoryModule
import ru.whoame.recogniser.ui.RecogniserAppViewModel

val appModule = module {

    includes(
        databaseModule,
        datasourceModule,
        repositoryModule,
    )

    viewModelOf(::RecogniserAppViewModel)

}
