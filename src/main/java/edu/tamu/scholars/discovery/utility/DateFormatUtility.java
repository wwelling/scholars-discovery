package edu.tamu.scholars.discovery.utility;

import java.text.ParseException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.context.i18n.LocaleContextHolder;

public class DateFormatUtility {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    private static final String[] DATE_PATTERNS = {
            DATE_FORMAT,
            "yyyy",
            "E MMM dd HH:mm:ss z yyyy",
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd'T'HH:mm:ss",
            "dd-MM-yy",
            "MM-dd-yyyy",
            "EEEEE MMMMM yyyy HH:mm:ss.SSSZ"
    };

    private DateFormatUtility() {

    }

    public static String parseYear(String value) throws ParseException {
        return String.valueOf(parseZonedDateTime(value).getYear());
    }

    public static String format(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                .format(DATE_FORMATTER);
    }

    public static ZonedDateTime parseZonedDateTime(String value) throws ParseException {
        return parseDate(value)
                .toInstant()
                .atZone(ZoneId.systemDefault());
    }

    public static Date parseDate(String value) throws ParseException {
        Locale locale = LocaleContextHolder.getLocale();
        return DateUtils.parseDate(value, locale, DATE_PATTERNS);
    }

    public static int getYear() {
        return ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())
                .getYear();
    }

}
