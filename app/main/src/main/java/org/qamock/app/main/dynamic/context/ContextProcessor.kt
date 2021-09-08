package org.qamock.app.main.dynamic.context

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ContextProcessor(private val testContextService: TestContextService) {
    private val regex = Regex("\\\$\\{#context#([a-z]+)\\(([a-z.]+)\\)#([a-z]+)}", RegexOption.MULTILINE)

    private val logger = LoggerFactory.getLogger(ContextProcessor::class.java)

    fun process(text: String, params: Map<String, String>): String {
        var result = text
        val results = regex.findAll(text)
        results.forEach {
            x ->
            val operator = x.groupValues[1]
            val key = (if (x.groupValues[2].toLowerCase().contains("params."))
                params[x.groupValues[2].replace("params.", "")]
            else x.groupValues[2]) ?: throw Error("Key of context not found in params by path ${x.groupValues[2]}")

            val field = x.groupValues[3]
            val valueOfExpression = when (operator.toLowerCase()) {
                "equals" -> testContextService.getEquals(key).params[field]
                "contains" -> testContextService.getContains(key).params[field]
                "find" -> testContextService.findFirstByValue(key).params[field]
                else -> null
            }
            result = result.replace(x.value, valueOfExpression.toString())
        }
        return result
    }
}