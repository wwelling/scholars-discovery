package edu.tamu.scholars.discovery.etl;

import java.util.regex.Pattern;

public class EtlConstants {

    public static final Pattern NESTED_DELIMITER_PATTERN = Pattern.compile("\\|\\|");

    private EtlConstants() {

    }

}
