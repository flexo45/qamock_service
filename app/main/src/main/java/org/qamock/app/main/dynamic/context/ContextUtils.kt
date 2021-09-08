package org.qamock.app.main.dynamic.context

interface ContextUtils {
    fun get(key: String): TestContext
    fun find(value: Any): TestContext
}