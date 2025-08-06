package ru.whoame.recogniser.data.datasource

import org.koin.dsl.module

val datasourceModule = module {

    single<RecognisedObjectLocalDatasource> {
        RecognisedObjectLocalDatasourceImpl(get())
    }

}
