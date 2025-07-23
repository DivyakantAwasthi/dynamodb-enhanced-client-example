package ca.sunlife.dynamo_db.entity;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.List;

@Data
@DynamoDbBean
public class Environment {
    private String namespace;
    private String averageVolume;
    private String maxPeakLoad;
    private String totalVolume;
    private String partitions;
    private String minReplicas;
    private String maxReplicas;
    private String pvClaimSize;
    private String cpuRequest;
    private String cpuLimit;
    private String memoryRequest;
    private String memoryLimit;
    private String functionalID;
    private String password;
    private List<String> logLevel;
    private List<String> nodeRegion;
}
