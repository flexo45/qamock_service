package org.qamock.app.main.dynamic.context

interface TestContextService {
    fun getEquals(key: String): TestContext
    fun getContains(key: String): TestContext
    fun findFirstByValue(value: Any): TestContext
}