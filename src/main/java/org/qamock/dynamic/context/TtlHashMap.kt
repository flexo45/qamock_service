package org.qamock.dynamic.context

import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit


class TtlHashMap<K, V> : HashMap<K, V>() {

    private val timestamps = HashMap<K, Long>()
    private val ttl: Long = 320000

    private val logger = LoggerFactory.getLogger(TtlHashMap::class.java)

    override operator fun get(key: K): V? {
        val value = super.get(key)
        return if (value != null && expired(key, value)) {
            logger.info("Context of key=$key removed")
            remove(key)
            timestamps.remove(key)
            null
        } else {
            value
        }
    }

    private fun expired(key: K, value: V): Boolean {
        return System.currentTimeMillis() - timestamps[key]!! > ttl
    }

    override fun put(key: K, value: V): V? {
        timestamps[key] = System.currentTimeMillis()
        return super.put(key, value)
    }

    override fun remove(key: K): V? {
        timestamps.remove(key)
        return super.remove(key)
    }

    override fun clear() {
        timestamps.clear()
        clear()
    }

    fun clearExpired() {
        for (k in keys) {
            this[k]
        }
    }
}