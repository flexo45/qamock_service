package org.qamock.app.main.dynamic.datagen

interface DataGenerator {
    fun number(length: Int): String
}