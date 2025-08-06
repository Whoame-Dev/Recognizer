package ru.whoame.recogniser

import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.whoame.recogniser.data.database.databaseModule
import ru.whoame.recogniser.data.datasource.datasourceModule
import ru.whoame.recogniser.data.repository.repositoryModule
import ru.whoame.recogniser.ui.RecogniserAppViewModel

private val appModule = module {

    viewModelOf(::RecogniserAppViewModel)

}

val appModules: List<Module> = listOf(
    appModule,
    repositoryModule,
    datasourceModule,
    databaseModule,
)
