package org.qamock.dynamic;

import org.qamock.domain.DynamicResource;
import org.qamock.domain.DynamicResponse;
import org.qamock.dynamic.domain.DynamicResourceRequest;

import javax.servlet.http.HttpServletResponse;

public interface AsyncLogWriter {

    void loggingDynamicRequest(DynamicResourceRequest dynamicResourceRequest, DynamicResource dynamicResource);

}
