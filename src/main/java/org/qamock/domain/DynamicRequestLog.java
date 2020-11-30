package org.qamock.domain;

import org.qamock.date.LocalTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "dynamic_request_log")
public class DynamicRequestLog implements Serializable {
    private static final long serialVersionUID = -7584236543654536568L;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    public long getId(){return this.id;}
    public void setId(long id) {this.id = id;}

    @ManyToOne
    @JoinColumn(name = "dynamic_resource_id")
    private DynamicResource dynamicResource;
    public DynamicResource getDynamicResource(){return this.dynamicResource;}
    public void setDynamicResource(DynamicResource dynamicResource) {this.dynamicResource = dynamicResource;}

    @ManyToOne
    @JoinColumn(name = "mock_response_id")
    private MockResponse mockResponse;
    public MockResponse getMockResponse(){return this.mockResponse;}
    public void setMockResponse(MockResponse mockResponse){this.mockResponse = mockResponse;}

    @ManyToOne
    @JoinColumn(name = "mock_request_id")
    private MockRequest mockRequest;
    public MockRequest getMockRequest(){return this.mockRequest;}
    public void setMockRequest(MockRequest mockRequest){this.mockRequest = mockRequest;}

    @Column(name = "created", nullable = false)
    private Date created;
    public Date getCreated(){return this.created;}
    public void setCreated(Date created) {this.created = created;}

    public DynamicRequestLog(){}

    public DynamicRequestLog(long id,
                             DynamicResource dynamicResource,
                             MockResponse mockResponse,
                             MockRequest mockRequest){
        setId(id);
        setCreated(LocalTime.now());
        setDynamicResource(dynamicResource);
        setMockResponse(mockResponse);
        setMockRequest(mockRequest);
    }

    public DynamicRequestLog(DynamicResource dynamicResource,
                             MockResponse mockResponse,
                             MockRequest mockRequest){
        setId(-1);
        setCreated(LocalTime.now());
        setDynamicResource(dynamicResource);
        setMockResponse(mockResponse);
        setMockRequest(mockRequest);
    }

    @Override
    public String toString(){
        return "DynamicRequestLog{id=" + id + ", dynamic_request_id=" + mockRequest.getId() + "}";
    }

}
