package ru.whoame.recogniser.data.database

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {

    single<AppDatabase> {
        Room.databaseBuilder(
            context = androidApplication(),
            klass = AppDatabase::class.java,
            name = "recogniser-app-database",
        ).build()
    }

    single<RecognisedObjectDao> {
        get<AppDatabase>().recognisedObjectDao()
    }

}
