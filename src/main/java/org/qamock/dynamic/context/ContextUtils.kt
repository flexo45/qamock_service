package org.qamock.dynamic.context

interface ContextUtils {
    fun get(key: String): TestContext
    fun find(value: Any): TestContext
}