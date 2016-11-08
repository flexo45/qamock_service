package org.qamock.dynamic;

import org.qamock.dynamic.domain.DynamicResourceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
@Service
public class DynamicResourceRequestQueueImpl implements DynamicResourceRequestQueue {

    private static final Logger logger = LoggerFactory.getLogger(DynamicResourceRequestQueueImpl.class);

    @Autowired
    DynamicResourceRequestQueueListener requestQueueListener;

    private List<DynamicResourceRequest> queue = new ArrayList<DynamicResourceRequest>();

    @Override
    public void enqueue(DynamicResourceRequest resourceRequest) {

        logger.info("Queue receive dynamic resource message");

        queue.add(resourceRequest);
        requestQueueListener.onMessage();
    }

    @Override
    public List<DynamicResourceRequest> receive(int pack) {

        if(queue.size() == 0){return null;}
        else {
            List<DynamicResourceRequest> packList = new ArrayList<DynamicResourceRequest>();

            for(DynamicResourceRequest request : queue){
                packList.add(request);
                pack--;
                if(pack == 0){
                    break;
                }
            }

            return packList;
        }
    }

}
