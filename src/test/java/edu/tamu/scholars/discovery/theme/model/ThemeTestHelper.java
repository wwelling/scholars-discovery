package edu.tamu.scholars.discovery.theme.model;

public class ThemeTestHelper {

    private ThemeTestHelper() {

    }

    public static Style getTestStyle(String key, String value) {
        Style style = new Style();
        style.setKey(key);
        style.setValue(value);

        return style;

    }

    public static Link getTestLink(String label, String uri) {
        Link link = new Link();
        link.setLabel(label);
        link.setUri(uri);

        return link;

    }

}
