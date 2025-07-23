package ca.sunlife.dynamo_db.repository;

import ca.sunlife.dynamo_db.entity.Item;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ItemRepository {
    private final DynamoDbTable<Item> table;

    public ItemRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.table = dynamoDbEnhancedClient.table("Items", TableSchema.fromBean(Item.class));
    }

    public void save(Item item) {
        table.putItem(item);
    }

    public Item getById(String id, String category) {
        return table.getItem(r -> r.key(k -> k.partitionValue(id).sortValue(category)));
    }

    public List<Item> getAll() {
        return table.scan().items().stream().toList();
    }

    public void deleteById(String id, String category) {
        table.deleteItem(r -> r.key(k -> k.partitionValue(id).sortValue(category)));
    }

    public void saveAll(List<Item> items) {
        items.forEach(table::putItem);
    }

    // Query items by category
    public List<Item> getItemsByCategory(String category) {
        return table.scan()
                .items()
                .stream()
                .filter(item -> category.equals(item.getCategory()))
                .toList();
    }

    // Filter items by price range
    public List<Item> getItemsByPriceRange(double minPrice, double maxPrice) {
        Expression filterExpression = Expression.builder()
                .expression("price BETWEEN :minPrice AND :maxPrice")
                .expressionValues(Map.of(
                        ":minPrice", AttributeValue.builder().n(String.valueOf(minPrice)).build(),
                        ":maxPrice", AttributeValue.builder().n(String.valueOf(maxPrice)).build()
                ))
                .build();

        return table.index("PriceIndex")
                .scan(r -> r.filterExpression(filterExpression))
                .stream()
                .flatMap(page -> page.items().stream())
                .collect(Collectors.toList());
    }

    public List<Item> getItemsByPriceRange(String id, double minPrice, double maxPrice) {
        Expression filterExpression = Expression.builder()
                .expression("price BETWEEN :minPrice AND :maxPrice")
                .expressionValues(Map.of(
                        ":minPrice", AttributeValue.builder().n(String.valueOf(minPrice)).build(),
                        ":maxPrice", AttributeValue.builder().n(String.valueOf(maxPrice)).build()
                ))
                .build();

        return table.index("PriceIndex")
                .query(r -> r.queryConditional(QueryConditional.keyEqualTo(k -> k.partitionValue(id)))
                        .filterExpression(filterExpression))
                .stream()
                .flatMap(page -> page.items().stream())
                .collect(Collectors.toList());
    }

    // Query items with nested object filtering
    public List<Item> getItemsByManufacturer(String manufacturer) {
        return table.scan()
                .items()
                .stream()
                .filter(item -> item.getDetails() != null && manufacturer.equals(item.getDetails().getManufacturer()))
                .collect(Collectors.toList());
    }
}
