package edu.tamu.scholars.discovery;

public class AppUtility {

    private AppUtility() {

    }

    public static String getAfter(String value, char delimiter) {
        int index = value.lastIndexOf(delimiter);

        return index < 0 ? value : value.substring(index + 1);
    }

    public static String getBefore(String value, char delimiter) {
        int index = value.lastIndexOf(delimiter);

        return index < 0 ? value : value.substring(0, index);
    }

}
