package edu.tamu.scholars.discovery.controller.argument;

import static edu.tamu.scholars.discovery.AppConstants.DEFAULT_QUERY;
import static edu.tamu.scholars.discovery.AppConstants.ID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkDescriptorArg {

    private final String id;

    private final String dateField;

    private final List<String> dataFields;

    private final String typeFilter;

    private NetworkDescriptorArg(String id, String dateField, List<String> dataFields, String typeFilter) {
        super();
        this.id = id;
        this.dateField = dateField;
        this.dataFields = dataFields;
        this.typeFilter = typeFilter;
    }

    public String getId() {
        return id;
    }

    public String getDateField() {
        return dateField;
    }

    public List<String> getDataFields() {
        return dataFields;
    }

    public String getTypeFilter() {
        return typeFilter;
    }

    public String getSort() {
        return String.format("%s asc", dateField);
    }

    public String getFieldList() {
        List<String> fields = new ArrayList<>(getDataFields());
        fields.add(dateField);
        fields.add(ID);
        return String.join(",", fields);
    }

    public String getFilterQuery() {
        return String.format("syncIds:%s AND %s", id, typeFilter);
    }

    public Map<String, String> getSolrParams() {
        final Map<String, String> queryParamMap = new HashMap<>();
        queryParamMap.put("q", DEFAULT_QUERY);
        queryParamMap.put("rows", String.valueOf(Integer.MAX_VALUE));
        queryParamMap.put("sort", getSort());
        queryParamMap.put("fl", getFieldList());
        queryParamMap.put("fq", getFilterQuery());

        return queryParamMap;
    }

    public static NetworkDescriptorArg of(
        String id,
        String dateField,
        List<String> dataFields,
        String typeFilter
    ) {
        return new NetworkDescriptorArg(id, dateField, dataFields, typeFilter);
    }

}
