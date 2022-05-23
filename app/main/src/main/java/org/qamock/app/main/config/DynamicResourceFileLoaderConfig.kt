package org.qamock.app.main.config

import org.qamock.app.main.dynamic.loader.DynamicResourceFileLoader
import org.qamock.app.main.service.DynamicResourcesService
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DynamicResourceFileLoaderConfig {
    @Bean
    fun resourceFileLoader(dynamicResourcesService: DynamicResourcesService): DynamicResourceFileLoader {
        return DynamicResourceFileLoader(dynamicResourcesService).also {
            try {
                it.loadScriptsOnInit()
            } catch (e: Exception) {
                LoggerFactory.getLogger(DynamicResourceFileLoaderConfig::class.java)
                    .error("Load dynamic resource scripts failed: ${e.message}")
            }
        }
    }
}
