package org.qamock.app.main.dynamic.context

class TestContext {
    var params = HashMap<String, Any>()

    fun put(key: String, value: Any): TestContext {
        params[key] = value
        return this
    }
}