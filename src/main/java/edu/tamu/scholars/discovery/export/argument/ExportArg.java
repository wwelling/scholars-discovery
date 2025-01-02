package edu.tamu.scholars.discovery.export.argument;

import java.util.Map;

public class ExportArg {

    private final String field;

    private final String label;

    public ExportArg(String field, String label) {
        this.field = field;
        this.label = label;
    }

    public String getField() {
        return field;
    }

    public String getLabel() {
        return label;
    }

    @SuppressWarnings("unchecked")
    public static ExportArg of(Object input) {
        Map<String, Object> export = (Map<String, Object>) input;
        String field = (String) export.get("field");
        String label = (String) export.get("label");
        return new ExportArg(field, label);
    }

    public static ExportArg of(String parameter) {
        String[] parts = parameter.split(",");
        return new ExportArg(parts[0], parts.length > 1 ? parts[1] : parts[0]);
    }

    @Override
    public String toString() {
        return "ExportArg [field=" + field + ", label=" + label + "]";
    }

}
