package edu.tamu.scholars.discovery.utility;

import java.time.Instant;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateUtility {

    private DateUtility() {

    }

    public static int ageInYearsFromEpochSecond(long timestamp) {
        return Period.between(
            Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate(),
            Instant.now().atZone(ZoneId.systemDefault()).toLocalDate()
        ).getYears();
    }

    public static int getYear() {
        return ZonedDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())
            .getYear();
    }

}
