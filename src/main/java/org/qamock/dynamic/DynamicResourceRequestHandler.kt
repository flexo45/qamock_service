package org.qamock.dynamic

import org.qamock.domain.DynamicResource
import org.qamock.dynamic.domain.DynamicResourceRequest

interface DynamicResourceRequestHandler {
    @Throws(DynamicResourceException::class)
    fun processResourceRequest(resource: DynamicResource, resourceRequest: DynamicResourceRequest)
}