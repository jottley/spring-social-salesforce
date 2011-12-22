package org.springframework.social.salesforce.api.impl.json;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.social.salesforce.api.QueryResult;
import org.springframework.social.salesforce.api.ResultItem;

import java.io.IOException;
import java.util.Map;

/**
 * Deserializer for {@see org.springframework.social.salesforce.api.ResultItem}
 *
 * @author Umut Utkan
 */
public class ResultItemDeserializer extends JsonDeserializer<ResultItem> {

    @Override
    public ResultItem deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDeserializationConfig(ctxt.getConfig());
        jp.setCodec(mapper);

        JsonNode jsonNode = jp.readValueAsTree();
        Map<String, Object> map = mapper.readValue(jsonNode, Map.class);
        ResultItem item = new ResultItem((String) ((Map) map.get("attributes")).get("type"),
                (String) ((Map) map.get("attributes")).get("url"));
        map.remove("attributes");
        //item.setAttributes(map);

        for(Map.Entry<String, Object> entry : map.entrySet()) {
            if(entry.getValue() instanceof Map) {
                Map<String, Object> inner = (Map) entry.getValue();
                if(inner.get("records") != null) {
                    item.getAttributes().put(entry.getKey(), mapper.readValue(jsonNode.get(entry.getKey()), QueryResult.class));
                } else {
                    item.getAttributes().put(entry.getKey(), mapper.readValue(jsonNode.get(entry.getKey()), ResultItem.class));
                }
            } else {
                item.getAttributes().put(entry.getKey(), entry.getValue());
            }
        }

        return item;
    }

}
