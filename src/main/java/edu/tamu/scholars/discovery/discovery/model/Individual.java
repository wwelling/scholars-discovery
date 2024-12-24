package edu.tamu.scholars.discovery.discovery.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static edu.tamu.scholars.discovery.discovery.DiscoveryConstants.CLASS;
import static edu.tamu.scholars.discovery.discovery.DiscoveryConstants.ID;
import static edu.tamu.scholars.discovery.discovery.DiscoveryConstants.SYNC_IDS;
import static edu.tamu.scholars.discovery.discovery.DiscoveryConstants.TYPE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.hateoas.server.core.Relation;

import edu.tamu.scholars.discovery.discovery.utility.DiscoveryUtility;

@JsonInclude(NON_EMPTY)
@Relation(collectionRelation = "individual", itemRelation = "individual")
public class Individual extends AbstractIndexDocument {

    private final Map<String, Object> content;

    public Individual() {
        this(new HashMap<>());
    }

    private Individual(Map<String, Object> content) {
        this.content = content;
    }

    public Map<String, Object> getContent() {
        return content;
    }

    @Override
    public String getProxy() {
        return (String) content.get(CLASS);
    }

    @Override
    public void setProxy(String proxy) {
        this.content.put(CLASS, proxy);
    }

    @Override
    public String getId() {
        return (String) content.get(ID);
    }

    @Override
    public void setId(String id) {
        this.content.put(ID, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> getType() {
        return (List<String>) content.get(TYPE);
    }

    @Override
    public void setType(List<String> type) {
        this.content.put(TYPE, type);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> getSyncIds() {
        return (List<String>) content.get(SYNC_IDS);
    }

    @Override
    public void setSyncIds(List<String> syncIds) {
        this.content.put(SYNC_IDS, syncIds);
    }

    public static Individual from(JsonNode document) {
        Map<String, Object> content = new HashMap<>();

        String name = document.get(CLASS).asText();

        DiscoveryUtility.getDiscoveryDocumentTypeFields(name)
            .entrySet()
            .stream()
            .filter(entry -> document.hasNonNull(entry.getKey()))
            .forEach(entry -> {
                String field = entry.getKey();
                JsonNode value = document.get(field);
                if (Collection.class.isAssignableFrom(entry.getValue()) && value.isArray()) {
                    List<String> values = new ArrayList<>();
                    for (JsonNode v : (ArrayNode) value) {
                        values.add(v.asText());
                    }
                    content.put(field, values);
                } else {
                    content.put(field, value.asText());
                }
            });

        return Individual.from(content);
    }

    public static Individual from(Map<String, Object> content) {
        return new Individual(content);
    }

    @Override
    public String toString() {
        return "Individual [content=" + content + "]";
    }

}
