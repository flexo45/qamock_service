package org.qamock.dynamic;

import org.qamock.dynamic.domain.DynamicResourceRequest;

import java.util.List;

public interface DynamicResourceRequestQueue {

    void enqueue(DynamicResourceRequest resourceRequest);

    List<DynamicResourceRequest> receive(int pack);

}
