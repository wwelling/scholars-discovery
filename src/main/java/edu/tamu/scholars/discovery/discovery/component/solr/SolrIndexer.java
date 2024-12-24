package edu.tamu.scholars.discovery.discovery.component.solr;

import static edu.tamu.scholars.discovery.discovery.service.IndexService.CREATED_FIELDS;
import static edu.tamu.scholars.discovery.discovery.DiscoveryConstants.ID;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import edu.tamu.scholars.discovery.discovery.annotation.FieldType;
import edu.tamu.scholars.discovery.discovery.component.Indexer;
import edu.tamu.scholars.discovery.discovery.model.AbstractIndexDocument;

public class SolrIndexer implements Indexer {

    private static final Logger logger = LoggerFactory.getLogger(SolrIndexer.class);

    @Value("${discovery.index.enableIndividualOnBatchFail:false}")
    private boolean enableIndividualOnBatchFail;

    @Autowired
    private SolrService solrService;

    @Autowired
    private ObjectMapper objectMapper;

    private final Class<AbstractIndexDocument> type;

    public SolrIndexer(Class<AbstractIndexDocument> type) {
        this.type = type;
    }

    @Override
    public void init() {
        for (Field field : FieldUtils.getFieldsListWithAnnotation(type, FieldType.class)) {
            FieldType fieldType = field.getAnnotation(FieldType.class);

            String name = StringUtils.isNotEmpty(fieldType.value())
                ? fieldType.value()
                : field.getName();

            if (!fieldType.readonly() && !CREATED_FIELDS.contains(name) && CREATED_FIELDS.add(name)) {
                ObjectNode addFieldNode = objectMapper.createObjectNode();
                ObjectNode fieldNode = objectMapper.createObjectNode();
                fieldNode.put("name", name);
                fieldNode.put("type", fieldType.type());
                fieldNode.put("stored", fieldType.stored());
                fieldNode.put("indexed", fieldType.indexed());
                fieldNode.put("docValues", fieldType.docValues());
                fieldNode.put("required", fieldType.required());

                if (StringUtils.isNotEmpty(fieldType.defaultValue())) {
                    fieldNode.put("defaultValue", fieldType.defaultValue());
                }

                fieldNode.put("multiValued", Collection.class.isAssignableFrom(field.getType()));

                addFieldNode.set("add-field", fieldNode);

                try {
                    solrService.addField(addFieldNode);

                    if (fieldType.copyTo().length > 0) {
                        ObjectNode addCopyFieldNode = objectMapper.createObjectNode();
                        ObjectNode copyFieldNode = objectMapper.createObjectNode();
                        copyFieldNode.put("source", name);

                        ArrayNode destinationFields = objectMapper.createArrayNode();

                        for (String destination : Arrays.asList(fieldType.copyTo())) {
                            destinationFields.add(destination);
                        }

                        copyFieldNode.set("dest", destinationFields);

                        addCopyFieldNode.set("add-copy-field", copyFieldNode);

                        try {
                            solrService.addCopyField(addCopyFieldNode);
                        } catch (Exception e) {
                            logger.error("Failed to add copy field", e);
                        }
                    }
                } catch (Exception e) {
                    logger.error("Failed to add field", e);
                }
            }
        }
    }

    @Override
    public void index(Collection<Map<String, Object>> documents) {
        try {
            solrService.index(documents);
            logger.info("Saved {} batch of {}", name(), documents.size());
        } catch (Exception e) {
            logger.debug("Error saving batch", e);
            if (enableIndividualOnBatchFail) {
                logger.warn("Failed to save batch of {}. Attempting individually.", name());
                documents.stream().forEach(this::index);
            } else {
                logger.warn("Skipping individuals of failed batch of {}. {}", name(), e.getMessage());
            }
        }
    }

    @Override
    public void index(Map<String, Object> document) {
        try {
            solrService.index(document);
            logger.info("Saved {} with id {}", name(), document.get(ID));
        } catch (Exception e) {
            logger.debug("Error saving individual", e);
            logger.warn("Failed to save {} with id {}", name(), document.get(ID));
        }
    }

    @Override
    public void optimize() {
        try {
            solrService.optimize();
        } catch (Exception e) {
            logger.debug("Error optimizing collection", e);
            logger.warn("Failed to optimize collection. {}", e.getMessage());
        }
    }

    @Override
    public Class<AbstractIndexDocument> type() {
        return type;
    }

    private String name() {
        return type.getSimpleName();
    }

}
