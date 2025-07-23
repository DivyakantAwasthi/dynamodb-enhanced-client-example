package ca.sunlife.dynamo_db.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.time.Instant;

public class InstantWithDateWrapperDeserializer extends JsonDeserializer<Instant> {
    @Override
    public Instant deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
        // Parse the JSON object and extract the "$date" field
        JsonNode node = parser.readValueAsTree();
        String dateValue = node.get("$date").asText();
        return Instant.parse(dateValue);
    }
}