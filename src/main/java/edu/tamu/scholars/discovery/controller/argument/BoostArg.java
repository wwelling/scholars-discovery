package edu.tamu.scholars.discovery.controller.argument;

public class BoostArg {

    private final String field;

    private final float value;

    BoostArg(String field, float value) {
        this.field = field;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public float getValue() {
        return value;
    }

    public static BoostArg of(String parameter) {
        String[] parts = parameter.split(",");
        return new BoostArg(parts[0], parts.length > 1 ? Float.valueOf(parts[1]) : 1.0f);
    }

    @Override
    public String toString() {
        return "BoostArg [field=" + field + ", value=" + value + "]";
    }

}
