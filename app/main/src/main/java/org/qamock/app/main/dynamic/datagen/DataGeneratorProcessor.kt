package org.qamock.app.main.dynamic.datagen

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class DataGeneratorProcessor(private val dataGenerator: DataGenerator) {
    private val regex = Regex("\\\$\\{#gen#([a-z0-9.]+)}", RegexOption.MULTILINE)

    private val logger = LoggerFactory.getLogger(DataGeneratorProcessor::class.java)

    fun process(text: String): String {
        var result = text

        var match = regex.find(result)
        while (match != null) {
            val function = match.groupValues[1].split(".").first()
            val valueOfExpression = when (function.toLowerCase()) {
                "number" -> dataGenerator
                        .number(try { match.groupValues[1].replace("number.", "").toInt() }
                        catch(e: NumberFormatException) { 6 })
                else -> null
            }
            result = result.replaceFirst(match.value, valueOfExpression.toString())
            match = regex.find(result)
        }
        return result
    }
}