package ca.sunlife.dynamo_db.entity;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@Data
@DynamoDbBean
public class Endpoint {
    private String endpointId;
    private String resourceName;
    private String operation;
    private String uri;
    private String operationId;
}
