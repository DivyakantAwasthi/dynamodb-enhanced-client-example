package ca.sunlife.dynamo_db.repository;

import ca.sunlife.dynamo_db.entity.GeneratedAPI;
import ca.sunlife.dynamo_db.entity.Item;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class GeneratedAPIRepository {
    private final DynamoDbTable<GeneratedAPI> table;

    public GeneratedAPIRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.table = dynamoDbEnhancedClient.table("GeneratedAPI", TableSchema.fromBean(GeneratedAPI.class));
    }

    public void save(GeneratedAPI generatedAPI) {

        table.putItem(generatedAPI);
    }

    public GeneratedAPI getByApiId(String apiId) {
        return table.getItem(r -> r.key(k -> k.partitionValue(apiId)));
    }

    public GeneratedAPI getByApiIdAndVersion(String apiID, String version) {
        return table.query(r -> r.queryConditional(
                        QueryConditional.keyEqualTo(
                                Key.builder()
                                        .partitionValue(apiID)
                                        .sortValue(version)
                                        .build())))
                .items()
                .stream()
                .findFirst()
                .orElse(null);
    }

    public List<GeneratedAPI> getByApiName(String apiName) {
        return table.index("ApiNameIndex")
                .query(r -> r.queryConditional(
                        QueryConditional.keyEqualTo(
                                Key.builder()
                                        .partitionValue(apiName)
                                        .build())))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }

    public List<GeneratedAPI> getByTargetTopicNameLike(String targetTopicName) {
        return table.index("TargetTopicNameIndex")
                .query(r -> r.queryConditional(
                        QueryConditional.keyEqualTo(
                                Key.builder()
                                        .partitionValue(targetTopicName)
                                        .build())))
                .stream()
                .flatMap(page -> page.items().stream())
                .toList();
    }

    public List<GeneratedAPI> getBySourceAndTargetTopicNameLike(String source, String targetTopicNameSubstring) {
        return table.index("SourceGroupingIndex")
                .query(r -> r.queryConditional(
                        QueryConditional.keyEqualTo(
                                Key.builder()
                                        .partitionValue(source)
                                        .build())))
                .stream()
                .flatMap(page -> page.items().stream())
                .filter(item -> item.getTargetTopicName() != null && item.getTargetTopicName().contains(targetTopicNameSubstring))
                .toList();
    }
}
