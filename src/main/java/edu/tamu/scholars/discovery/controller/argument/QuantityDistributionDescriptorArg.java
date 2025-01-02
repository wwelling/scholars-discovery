package edu.tamu.scholars.discovery.controller.argument;

public class QuantityDistributionDescriptorArg {

    private final String label;

    private final String field;

    private QuantityDistributionDescriptorArg(String label, String field) {
        super();
        this.label = label;
        this.field = field;
    }

    public String getLabel() {
        return label;
    }

    public String getField() {
        return field;
    }

    public static QuantityDistributionDescriptorArg of(String label, String field) {
        return new QuantityDistributionDescriptorArg(label, field);
    }

}
