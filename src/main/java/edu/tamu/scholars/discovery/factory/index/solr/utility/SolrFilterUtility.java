package edu.tamu.scholars.discovery.factory.index.solr.utility;

import static edu.tamu.scholars.discovery.AppConstants.QUERY_DELIMETER;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.tamu.scholars.discovery.controller.argument.FilterArg;

public class SolrFilterUtility {

    private static final Pattern RANGE_PATTERN = Pattern.compile("^\\[(.*?) TO (.*?)\\]$");

    private SolrFilterUtility() {

    }

    public static String buildFilter(FilterArg filter, boolean skipTag) {
        String field = skipTag ? filter.getField() : filter.getCommand();
        String value = filter.getValue();

        StringBuilder filterQuery = new StringBuilder()
            .append(field)
            .append(QUERY_DELIMETER);

        switch (filter.getOpKey()) {
            case BETWEEN:
                Matcher rangeMatcher = RANGE_PATTERN.matcher(value);
                if (rangeMatcher.matches()) {
                    String start = rangeMatcher.group(1);
                    String end = rangeMatcher.group(2);

                    // NOTE: hard coded inclusive start exclusive end
                    filterQuery
                        .append("[")
                        .append(start)
                        .append(" TO ")
                        .append(end)
                        .append("}");

                    // NOTE: if date field, must be ISO format for Solr to recognize
                    // https://lucene.apache.org/solr/7_5_0/solr-core/org/apache/solr/schema/DatePointField.html
                } else {
                    filterQuery
                        .append("\"")
                        .append(value)
                        .append("\"");
                }
                break;
            case ENDS_WITH:
                filterQuery
                    .append(value)
                    .append("}");
                break;
            case EQUALS:
                filterQuery
                    .append("\"")
                    .append(value)
                    .append("\"");
                break;
            case FUZZY:
                // NOTE: only supporting single-word terms and default edit distance of 2
                filterQuery
                    .append(value)
                    .append("~");
                break;
            case NOT_EQUALS:
                filterQuery
                    .append("!")
                    .append("\"")
                    .append(value)
                    .append("\"");
                break;
            case STARTS_WITH:
                filterQuery
                    .append("{!edismax qf=")
                    .append(field)
                    .append("}")
                    .append(value)
                    .append("*");
                break;
            case CONTAINS:
                throw new UnsupportedOperationException("CONTAINS: Solr contains query not yet supported");
            case EXPRESSION, RAW:
                filterQuery
                    .append(value);
                break;
            default:
                break;
        }

        return filterQuery.toString();
    }
    
}
