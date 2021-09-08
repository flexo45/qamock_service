package org.qamock.app.main.dynamic.datagen

import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
class DataGeneratorImpl : DataGenerator {
    override fun number(length: Int): String {
        var resut = ""
        for (i in 1..length) {
            resut += Random.nextInt(from = 0, until = 9)
        }
        return resut
    }
}