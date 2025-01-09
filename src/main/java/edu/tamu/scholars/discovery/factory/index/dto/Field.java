package edu.tamu.scholars.discovery.factory.index.dto;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

import edu.tamu.scholars.discovery.etl.model.DataFieldDescriptor;
import edu.tamu.scholars.discovery.etl.model.FieldDestination;

@Data
public class Field {

    private String name;
    private String type;
    private boolean required;
    private boolean stored;
    private boolean indexed;
    private boolean docValues;
    private boolean multiValued;
    private String defaultValue;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("type", type);
        map.put("required", required);
        map.put("stored", stored);
        map.put("indexed", indexed);
        map.put("docValues", docValues);
        map.put("multiValued", multiValued);

        if (isNotEmpty(defaultValue)) {
            map.put("defaultValue", defaultValue);
        }

        return map;
    }

    public static Field of(Map<String, Object> map) {
        Field field = new Field();
        if (map.containsKey("name")) {
            field.setName((String) map.get("name"));
        }
        if (map.containsKey("type")) {
            field.setType((String) map.get("type"));
        }
        if (map.containsKey("required")) {
            field.setRequired((Boolean) map.get("required"));
        }
        if (map.containsKey("stored")) {
            field.setStored((Boolean) map.get("stored"));
        }
        if (map.containsKey("indexed")) {
            field.setIndexed((Boolean) map.get("indexed"));
        }
        if (map.containsKey("docValues")) {
            field.setDocValues((Boolean) map.get("docValues"));
        }
        if (map.containsKey("multiValued")) {
            field.setMultiValued((Boolean) map.get("multiValued"));
        }
        if (map.containsKey("defaultValue")) {
            field.setDefaultValue((String) map.get("defaultValue"));
        }

        return field;
    }

    public static Field of(DataFieldDescriptor descriptor) {
        FieldDestination destination = descriptor.getDestination();

        Field field = new Field();
        field.setName(descriptor.getName());
        field.setType(destination.getType());
        field.setRequired(destination.isRequired());
        field.setStored(destination.isStored());
        field.setIndexed(destination.isIndexed());
        field.setDocValues(destination.isDocValues());
        field.setMultiValued(destination.isMultiValued());

        if (isNotEmpty(destination.getDefaultValue())) {
            field.setDefaultValue(destination.getDefaultValue());
        }

        return field;
    }

}
