package ca.sunlife.dynamo_db.entity;

import ca.sunlife.dynamo_db.util.InstantWithDateWrapperDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
@DynamoDbBean
public class GeneratedAPI {

    private String apiName;
    private String version;
    private String owner;
    private String apiID;
    private Boolean active;
    @JsonDeserialize(using = InstantWithDateWrapperDeserializer.class)
    private Instant created;
    @JsonDeserialize(using = InstantWithDateWrapperDeserializer.class)
    private Instant lastModified;
    private Boolean isBusinessAPI;
    private Boolean isATS;
    private Metadata metadata;
    private Map<String, Endpoint> endpoints;
    private Security security;
    private Configurations configurations;
    private List<String> dataSources;
    private String _class;
    private String targetTopicName;
    private String source;

    @DynamoDbPartitionKey
    public String getApiID() {
        return apiID;
    }

    @DynamoDbSortKey
    public String getVersion() {
        return version;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "ApiNameIndex")
    public String getApiName() {
        return apiName;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "TargetTopicNameIndex")
    public String getTargetTopicName() {
        return configurations != null ? configurations.getTargetTopicName() : null;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "SourceGroupingIndex")
    public String getSource() {
        return configurations != null ? configurations.getSource() : null;
    }

    public void setConfigurations(Configurations configurations) {
        this.configurations = configurations;
        if (configurations != null) {
            this.targetTopicName = configurations.getTargetTopicName();
            this.source = configurations.getSource();
        }
    }
}