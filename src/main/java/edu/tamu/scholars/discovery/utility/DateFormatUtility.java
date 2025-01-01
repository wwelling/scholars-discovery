package edu.tamu.scholars.discovery.utility;

import static java.lang.String.valueOf;
import static java.time.ZoneId.systemDefault;
import static java.time.format.DateTimeFormatter.ISO_INSTANT;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

import java.text.ParseException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

public class DateFormatUtility {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final DateTimeFormatter DATE_FORMATTER = ofPattern(DATE_FORMAT);

    private static final String[] DATE_PATTERNS = {
        DATE_FORMAT,
        "yyyy",
        "yyyy-MM-dd'T'HH:mm:ss",
        "dd-MM-yy",
        "MM-dd-yyyy",
        "yyyy-MM-dd'T'HH:mm:ss'Z'",
        "E MMM dd HH:mm:ss z yyyy",
        "EEEEE MMMMM yyyy HH:mm:ss.SSSZ"
    };

    private DateFormatUtility() {

    }

    public static String format(Date date) {
        return date.toInstant()
            .atZone(systemDefault())
            .toLocalDateTime()
            .format(DATE_FORMATTER);
    }

    public static String parse(String value) throws ParseException {
        return parseZonedDateTime(value)
            .format(ISO_INSTANT);
    }

    public static String parseYear(String value) throws ParseException {
        return valueOf(parseZonedDateTime(value).getYear());
    }

    public static ZonedDateTime parseZonedDateTime(String value) throws ParseException {
        return parseDate(value)
            .toInstant()
            .atZone(systemDefault());
    }

    public static Date parseDate(String value) throws ParseException {
        return DateUtils.parseDate(value, getLocale(), DATE_PATTERNS);
    }

}
