package ca.sunlife.dynamo_db.entity;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@Data
@DynamoDbBean
public class Configurations {
    private String source;
    private String transformerEngine;
    private String streamingEngine;
    private String payloadSize;
    private String replicationThreads;
    private Boolean captureEvent;
    private String refreshThreads;
    private String targetTopicName;
    private String retentionTime;
    private String logCompacted;
    private String deploymentName;
    private String type;
    private Boolean enableEmptyEvents;
    private Boolean caseInsensitive;
    private Boolean isTransformerCDCEvent;
    private Environment development;
    private Environment sit;
    private Environment stage;
    private Environment production;
}
