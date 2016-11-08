package org.qamock.dynamic;

import org.qamock.dynamic.domain.DynamicResourceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DynamicResourceRequestQueueListenerImpl implements DynamicResourceRequestQueueListener {

    private static final Logger logger = LoggerFactory.getLogger(DynamicResourceRequestQueueListenerImpl.class);

    private static final int packSize = 5;

    @Autowired
    DynamicResourceRequestQueue queue;

    @Override
    public void onMessage(){

        List<DynamicResourceRequest> messages = queue.receive(packSize);

    }
}
