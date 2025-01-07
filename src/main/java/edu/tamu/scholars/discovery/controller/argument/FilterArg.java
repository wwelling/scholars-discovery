package edu.tamu.scholars.discovery.controller.argument;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import edu.tamu.scholars.discovery.model.OpKey;

public class FilterArg {

    private final String field;

    private final String value;

    private final OpKey opKey;

    private final String tag;

    FilterArg(String field, String value, OpKey opKey, String tag) {
        this.field = field;
        this.value = value;
        this.opKey = opKey;
        this.tag = tag;
    }

    public String getValue() {
        return value;
    }

    public OpKey getOpKey() {
        return opKey;
    }

    public String getTag() {
        return tag;
    }

    public String getField() {
        return field;
    }

    public String getCommand() {
        return StringUtils.isNotEmpty(tag)
            ? String.format("{!tag=%s}%s", tag, field)
            : field;
    }

    public static FilterArg of(String field, Optional<String> value, Optional<String> opKey, Optional<String> tag) {
        String valueParam = value.isPresent() ? value.get() : StringUtils.EMPTY;
        OpKey opKeyParam = opKey.isPresent() ? OpKey.valueOf(opKey.get()) : OpKey.EQUALS;
        String tagParam = tag.isPresent() ? tag.get() : StringUtils.EMPTY;
        return new FilterArg(field, valueParam, opKeyParam, tagParam);
    }

    @Override
    public String toString() {
        return "FilterArg [field=" + field + ", value=" + value + ", opKey=" + opKey + ", tag=" + tag + "]";
    }

}
