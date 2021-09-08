package org.qamock.app.main.web.mvc;

import org.qamock.app.main.domain.DynamicResource;
import org.qamock.app.main.domain.DynamicResponse;

import java.util.ArrayList;
import java.util.List;

public class DynamicResourceModel {

    public DynamicResourceModel(DynamicResource resource, List<DynamicResponse> responseList){
        this.id = resource.getId();
        this.path = resource.getPath();
        this.strategy = resource.getDispatch_strategy();

        this.responses = new ArrayList<String>();
        for(DynamicResponse response : responseList){
            responses.add(response.getName());
        }
    }

    private long id;
    private String path;
    private int strategy;
    private List<String> responses;

    public long getId(){return id;}
    public String getPath(){return path;}
    public int getStrategy(){return strategy;}
    public List<String> getResponses(){return responses;}
}
