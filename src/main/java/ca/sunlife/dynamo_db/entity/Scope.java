package ca.sunlife.dynamo_db.entity;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.List;

@Data
@DynamoDbBean
public class Scope {
    private String scope;
    private List<EndpointId> endpointIds;
    private String description;
    private String scopeName;
}
