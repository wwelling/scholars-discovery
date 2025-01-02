package edu.tamu.scholars.discovery.export.utility;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import edu.tamu.scholars.discovery.model.Individual;

public class FilenameUtility {

    private static final String UNDERSCORE = "_";
    private static final String NAME = "name";
    private static final String LAST_NAME = "lastName";
    private static final String FIRST_NAME = "firstName";

    private FilenameUtility() {

    }

    public static String normalizeExportFilename(String label) {
        return prefixWithTimestampAfterUnderscore(localizeWhileUnderscoreReplaceSpaceWithUnderscore(label));
    }

    public static String normalizeExportFilename(Individual individual) {
        Map<String, Object> content = individual.getContent();
        StringBuilder label = new StringBuilder();

        String proxy = individual.getProxy();

        if (proxy.equals("Organization") && content.containsKey(NAME)) {
            label.append((String) content.get(NAME))
                .append(UNDERSCORE);
        } else if (proxy.equals("Person") && content.containsKey(LAST_NAME)) {
            label.append((String) content.get(LAST_NAME))
                .append(UNDERSCORE);

            if (content.containsKey(FIRST_NAME)) {
                label.append((String) content.get(FIRST_NAME))
                    .append(UNDERSCORE);
            }
        }

        label.append(individual.getId());

        return localizeWhileUnderscoreReplaceSpaceWithUnderscore(label.toString());
    }

    private static String localizeWhileUnderscoreReplaceSpaceWithUnderscore(String value) {
        Locale locale = LocaleContextHolder.getLocale();
        return value.toLowerCase(locale)
            .replace(StringUtils.SPACE, UNDERSCORE);
    }

    private static String prefixWithTimestampAfterUnderscore(String value) {
        return String.format("%s_%s", value, String.valueOf(new Date().getTime()));
    }

}
