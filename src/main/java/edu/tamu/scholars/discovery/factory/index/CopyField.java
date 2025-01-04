package edu.tamu.scholars.discovery.factory.index;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class CopyField {

    private String source;
    private List<String> dest;

    public static CopyField of(Map<String, Object> map) {
        CopyField copyField = new CopyField();
        if (map.containsKey("source")) {
            copyField.setSource((String) map.get("source"));
        }
        if (map.containsKey("dest")) {
            Object dest = map.get("dest");
            if (dest instanceof String destination) {
                copyField.setDest(List.of(destination));
            } else if (dest instanceof List<?> destinations) {
                copyField.setDest((List<String>) destinations);
            }
        }

        return copyField;
    }

    public static CopyField of(String source, String dest) {
        CopyField copyField = new CopyField();
        copyField.setSource(source);
        copyField.setDest(List.of(dest));

        return copyField;
    }

}
