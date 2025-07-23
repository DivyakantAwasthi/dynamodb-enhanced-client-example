package ca.sunlife.dynamo_db.entity;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@Data
@DynamoDbBean
public class Label {
    private String operation;
    private String resourceName;
}
