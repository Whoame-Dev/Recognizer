package ru.whoame.recogniser

import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.KoinTest
import org.koin.test.verify.verify

class DiGraphTest : KoinTest {

    @Test
    @OptIn(KoinExperimentalAPI::class)
    fun koinModulesAreValid() {
        appModule.verify()
    }

}
