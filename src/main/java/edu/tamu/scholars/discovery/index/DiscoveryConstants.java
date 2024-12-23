package edu.tamu.scholars.discovery.index;

public class DiscoveryConstants {

    public static final String WILDCARD = "*";

    public static final String ID = "id";

    public static final String CLASS = "class";

    public static final String ABSTRACT = "abstract";

    public static final String TYPE = "type";

    public static final String SNIPPET = "snippet";

    public static final String MOD_TIME = "modTime";

    public static final String SYNC_IDS = "syncIds";

    public static final String QUERY_DELIMETER = ":";

    public static final String DEFAULT_QUERY = WILDCARD + QUERY_DELIMETER + WILDCARD;

    public static final String NESTED_DELIMITER = "::";

    public static final String REQUEST_PARAM_DELIMETER = ",";

    public static final String PATH_DELIMETER_REGEX = "\\.";

    public static final String DISCOVERY_MODEL_PACKAGE = "edu.tamu.scholars.discovery.index.model";

    public static final String PARENTHESES_TEMPLATE = "(%s)";

    private DiscoveryConstants() {

    }

}
