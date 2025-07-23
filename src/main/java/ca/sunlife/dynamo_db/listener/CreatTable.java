package ca.sunlife.dynamo_db.listener;

import ca.sunlife.dynamo_db.entity.GeneratedAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

@Component
public class CreatTable {
    @Autowired
    private DynamoDbClient dynamoDbClient;

//    @EventListener(ApplicationReadyEvent.class)
    public void createItemsTable() {

        CreateTableRequest createTableRequest = CreateTableRequest.builder()
                .tableName("Items")
                .attributeDefinitions(
                        AttributeDefinition.builder()
                                .attributeName("id")
                                .attributeType(ScalarAttributeType.S)
                                .build(),
                        AttributeDefinition.builder()
                                .attributeName("category")
                                .attributeType(ScalarAttributeType.S)
                                .build(),
                        AttributeDefinition.builder()
                                .attributeName("price")
                                .attributeType(ScalarAttributeType.N)
                                .build()
                )
                .keySchema(
                        KeySchemaElement.builder()
                                .attributeName("id")
                                .keyType(KeyType.HASH)
                                .build(),
                        KeySchemaElement.builder()
                                .attributeName("category")
                                .keyType(KeyType.RANGE)
                                .build()
                )
                .globalSecondaryIndexes(
                        GlobalSecondaryIndex.builder()
                                .indexName("PriceIndex")
                                .keySchema(
                                        KeySchemaElement.builder()
                                                .attributeName("id")
                                                .keyType(KeyType.HASH)
                                                .build(),
                                        KeySchemaElement.builder()
                                                .attributeName("price")
                                                .keyType(KeyType.RANGE)
                                                .build()
                                )
                                .projection(Projection.builder()
                                        .projectionType(ProjectionType.ALL)
                                        .build())
                                .provisionedThroughput(
                                        ProvisionedThroughput.builder()
                                                .readCapacityUnits(5L)
                                                .writeCapacityUnits(5L)
                                                .build()
                                )
                                .build()
                )
                .provisionedThroughput(
                        ProvisionedThroughput.builder()
                                .readCapacityUnits(5L)
                                .writeCapacityUnits(5L)
                                .build()
                )
                .build();

        dynamoDbClient.createTable(createTableRequest);
        System.out.println("Table 'Items' with GSI 'PriceIndex' created successfully.");
    }

//    @EventListener(ApplicationReadyEvent.class)
    public void createGeneratedAPI() {
        CreateTableRequest createTableRequest = CreateTableRequest.builder()
                .tableName("GeneratedAPI")
                .attributeDefinitions(
                        AttributeDefinition.builder()
                                .attributeName("apiID")
                                .attributeType(ScalarAttributeType.S)
                                .build(),
                        AttributeDefinition.builder()
                                .attributeName("version")
                                .attributeType(ScalarAttributeType.S)
                                .build(),
                        AttributeDefinition.builder()
                                .attributeName("targetTopicName")
                                .attributeType(ScalarAttributeType.S)
                                .build(),
                        AttributeDefinition.builder()
                                .attributeName("source")
                                .attributeType(ScalarAttributeType.S)
                                .build()
                )
                .keySchema(
                        KeySchemaElement.builder()
                                .attributeName("apiID")
                                .keyType(KeyType.HASH)
                                .build(),
                        KeySchemaElement.builder()
                                .attributeName("version")
                                .keyType(KeyType.RANGE)
                                .build()
                )
                .globalSecondaryIndexes(
                        GlobalSecondaryIndex.builder()
                                .indexName("TargetTopicIndex")
                                .keySchema(
                                        KeySchemaElement.builder()
                                                .attributeName("targetTopicName")
                                                .keyType(KeyType.HASH)
                                                .build()
                                )
                                .projection(Projection.builder()
                                        .projectionType(ProjectionType.ALL)
                                        .build())
                                .provisionedThroughput(ProvisionedThroughput.builder()
                                        .readCapacityUnits(5L)
                                        .writeCapacityUnits(5L)
                                        .build())
                                .build(),
                        GlobalSecondaryIndex.builder()
                                .indexName("SourceGroupingIndex")
                                .keySchema(
                                        KeySchemaElement.builder()
                                                .attributeName("source")
                                                .keyType(KeyType.HASH)
                                                .build()
                                )
                                .projection(Projection.builder()
                                        .projectionType(ProjectionType.ALL)
                                        .build())
                                .provisionedThroughput(ProvisionedThroughput.builder()
                                        .readCapacityUnits(5L)
                                        .writeCapacityUnits(5L)
                                        .build())
                                .build()
                )
                .provisionedThroughput(ProvisionedThroughput.builder()
                        .readCapacityUnits(5L)
                        .writeCapacityUnits(5L)
                        .build())
                .build();

        dynamoDbClient.createTable(createTableRequest);

        System.out.println("Table 'GeneratedAPI' with GSI for 'targetTopicName' and 'source' created successfully.");
    }

//    @EventListener(ApplicationReadyEvent.class)
    public void updateGeneratedAPISchema() {
        String tableName = "GeneratedAPI";

        // Check if the table exists
        try {
            DescribeTableResponse describeTableResponse = dynamoDbClient.describeTable(r -> r.tableName(tableName));
            System.out.println("Table '" + tableName + "' already exists. Checking schema...");

            // Check if the "TargetTopicIndex" exists
            boolean targetTopicIndexExists = describeTableResponse.table().globalSecondaryIndexes().stream()
                    .anyMatch(index -> "TargetTopicIndex".equals(index.indexName()));

            // Delete and recreate "TargetTopicIndex" if it exists
            if (targetTopicIndexExists) {
                System.out.println("Deleting existing GSI: TargetTopicIndex");
                dynamoDbClient.updateTable(r -> r.tableName(tableName)
                        .globalSecondaryIndexUpdates(
                                GlobalSecondaryIndexUpdate.builder()
                                        .delete(DeleteGlobalSecondaryIndexAction.builder()
                                                .indexName("TargetTopicIndex")
                                                .build())
                                        .build()
                        )
                );

                System.out.println("Recreating GSI: TargetTopicIndex");
                dynamoDbClient.updateTable(r -> r.tableName(tableName)
                        .attributeDefinitions(
                                AttributeDefinition.builder()
                                        .attributeName("targetTopicName")
                                        .attributeType(ScalarAttributeType.S)
                                        .build()
                        )
                        .globalSecondaryIndexUpdates(
                                GlobalSecondaryIndexUpdate.builder()
                                        .create(CreateGlobalSecondaryIndexAction.builder()
                                                .indexName("TargetTopicIndex")
                                                .keySchema(
                                                        KeySchemaElement.builder()
                                                                .attributeName("targetTopicName")
                                                                .keyType(KeyType.HASH)
                                                                .build()
                                                )
                                                .projection(Projection.builder()
                                                        .projectionType(ProjectionType.ALL)
                                                        .build())
                                                .provisionedThroughput(ProvisionedThroughput.builder()
                                                        .readCapacityUnits(5L)
                                                        .writeCapacityUnits(5L)
                                                        .build())
                                                .build()
                                        )
                                        .build()
                        )
                );
                System.out.println("GSI 'TargetTopicIndex' updated successfully.");
            } else {
                System.out.println("GSI 'TargetTopicIndex' does not exist. Skipping update.");
            }

            // Check if the "SourceGroupingIndex" exists
            boolean sourceGroupingIndexExists = describeTableResponse.table().globalSecondaryIndexes().stream()
                    .anyMatch(index -> "SourceGroupingIndex".equals(index.indexName()));

            // Delete and recreate "SourceGroupingIndex" if it exists
            if (sourceGroupingIndexExists) {
                System.out.println("Deleting existing GSI: SourceGroupingIndex");
                dynamoDbClient.updateTable(r -> r.tableName(tableName)
                        .globalSecondaryIndexUpdates(
                                GlobalSecondaryIndexUpdate.builder()
                                        .delete(DeleteGlobalSecondaryIndexAction.builder()
                                                .indexName("SourceGroupingIndex")
                                                .build())
                                        .build()
                        )
                );

                System.out.println("Recreating GSI: SourceGroupingIndex");
                dynamoDbClient.updateTable(r -> r.tableName(tableName)
                        .attributeDefinitions(
                                AttributeDefinition.builder()
                                        .attributeName("source")
                                        .attributeType(ScalarAttributeType.S)
                                        .build()
                        )
                        .globalSecondaryIndexUpdates(
                                GlobalSecondaryIndexUpdate.builder()
                                        .create(CreateGlobalSecondaryIndexAction.builder()
                                                .indexName("SourceGroupingIndex")
                                                .keySchema(
                                                        KeySchemaElement.builder()
                                                                .attributeName("source")
                                                                .keyType(KeyType.HASH)
                                                                .build()
                                                )
                                                .projection(Projection.builder()
                                                        .projectionType(ProjectionType.ALL)
                                                        .build())
                                                .provisionedThroughput(ProvisionedThroughput.builder()
                                                        .readCapacityUnits(5L)
                                                        .writeCapacityUnits(5L)
                                                        .build())
                                                .build()
                                        )
                                        .build()
                        )
                );
                System.out.println("GSI 'SourceGroupingIndex' updated successfully.");
            } else {
                System.out.println("GSI 'SourceGroupingIndex' does not exist. Skipping update.");
            }

        } catch (ResourceNotFoundException e) {
            System.err.println("Table '" + tableName + "' does not exist. Exiting schema update.");
        } catch (Exception e) {
            System.err.println("Failed to update schema for table '" + tableName + "': " + e.getMessage());
        }
    }
}
