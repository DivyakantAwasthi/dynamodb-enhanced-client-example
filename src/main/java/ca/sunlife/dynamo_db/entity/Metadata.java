package ca.sunlife.dynamo_db.entity;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.List;

@Data
@DynamoDbBean
public class Metadata {
    private String apiName;
    private String owner;
    private String ownerTeam;
    private String description;
    private String businessGroup;
    private String lob;
    private String regionCountry;
    private String platformJourney;
    private List<String> businessCapabilities;
    private String serviceId;
    private String domainOfData;
    private String subDomainOfData;
    private String sourceSystemName;
    private String databaseName;
    private String tableName;
    private String topicName;
    private String unloadTopic;
}
