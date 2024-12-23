package edu.tamu.scholars.discovery.index.model;

import static edu.tamu.scholars.discovery.index.DiscoveryConstants.CLASS;
import static edu.tamu.scholars.discovery.index.DiscoveryConstants.ID;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.tamu.scholars.discovery.index.annotation.FieldSource;
import edu.tamu.scholars.discovery.index.annotation.FieldType;

public abstract class AbstractIndexDocument {

    @JsonProperty(CLASS)
    @FieldType(name = CLASS, type = "string", required = true)
    private String proxy;

    @FieldType(name = ID, required = true, readonly = true)
    private String id;

    @FieldType(type = "strings", docValues = true)
    @FieldSource(
        template = "common/type",
        predicate = "http://vitro.mannlib.cornell.edu/ns/vitro/0.7#mostSpecificType",
        parse = true
    )
    private List<String> type;

    @FieldType(type = "strings")
    private List<String> syncIds = new ArrayList<>();

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    public List<String> getSyncIds() {
        return syncIds;
    }

    public void setSyncIds(List<String> syncIds) {
        this.syncIds = syncIds;
    }

}
