package org.qamock.dynamic.context

import org.springframework.stereotype.Service

@Service
class TestContextServiceImpl : TestContextService {
    private var contextMap = TtlHashMap<String, TestContext>()

    override fun getEquals(key: String): TestContext {
        var context = contextMap[key]
        if (context == null) {
            context = TestContext()
            contextMap[key] = context
        }
        contextMap.clearExpired()
        return context;
    }

    override fun getContains(key: String): TestContext {
        val context = contextMap.filter { x -> x.key.contains(key) }.values.stream().findFirst()
        if (context.isEmpty) throw Error("Context with key contains $key not found")
        return context.get()
    }

    override fun findFirstByValue(value: Any): TestContext {
        val context = contextMap.filterValues { x -> x.params.containsValue(value) }.values.stream().findFirst()
        if (context.isEmpty) throw Error("Context contain params with value $value not found")
        return context.get()
    }
}

