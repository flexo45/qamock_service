package org.qamock.app.main.dynamic

import org.qamock.app.main.domain.DynamicResource
import org.qamock.app.main.dynamic.domain.DynamicResourceRequest

interface DynamicResourceRequestHandler {
    @Throws(DynamicResourceException::class)
    fun processResourceRequest(resource: DynamicResource, resourceRequest: DynamicResourceRequest)
}