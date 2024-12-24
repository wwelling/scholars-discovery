package edu.tamu.scholars.discovery.discovery.argument;

import edu.tamu.scholars.discovery.discovery.utility.DiscoveryUtility;

// TODO: refactor to DiscoveryQuantityDistributionDescriptorArg and add argument resolver
public class DiscoveryQuantityDistributionDescriptor {

    private final String label;

    private final String field;

    private DiscoveryQuantityDistributionDescriptor(String label, String field) {
        super();
        this.label = label;
        this.field = DiscoveryUtility.findProperty(field);
    }

    public String getLabel() {
        return label;
    }

    public String getField() {
        return field;
    }

    public static DiscoveryQuantityDistributionDescriptor of(String label, String field) {
        return new DiscoveryQuantityDistributionDescriptor(label, field);
    }

}
