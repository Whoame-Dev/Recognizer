package ru.whoame.recogniser.data.repository

import org.koin.dsl.module

val repositoryModule = module {

    single<RecognisedObjectRepository> {
        RecognisedObjectRepositoryImpl(get())
    }

}
