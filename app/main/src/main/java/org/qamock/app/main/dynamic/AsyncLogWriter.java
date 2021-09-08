package org.qamock.app.main.dynamic;

import org.qamock.app.main.domain.DynamicResource;
import org.qamock.app.main.dynamic.domain.DynamicResourceRequest;

public interface AsyncLogWriter {

    void loggingDynamicRequest(DynamicResourceRequest dynamicResourceRequest, DynamicResource dynamicResource);

}
