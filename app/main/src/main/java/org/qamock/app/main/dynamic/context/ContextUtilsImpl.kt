package org.qamock.app.main.dynamic.context

import org.springframework.stereotype.Component

@Component
class ContextUtilsImpl(private val testContextService: TestContextService) : ContextUtils {
    override fun get(key: String): TestContext { return testContextService.getEquals(key) }
    override fun find(value: Any): TestContext { return testContextService.findFirstByValue(value) }
}