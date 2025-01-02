package edu.tamu.scholars.discovery.controller.response;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import edu.tamu.scholars.discovery.controller.argument.QuantityDistributionDescriptorArg;

public class DiscoveryQuantityDistribution {

    private final String label;

    private final String field;

    private final List<Slice> distribution;

    private Long total;

    private DiscoveryQuantityDistribution(String label, String field) {
        this.label = label;
        this.field = field;
        this.distribution = new ArrayList<>();
        this.total = 0L;
    }

    public String getLabel() {
        return label;
    }

    public String getField() {
        return field;
    }

    public List<Slice> getDistribution() {
        return distribution;
    }

    public Long getTotal() {
        return total;
    }

    public void parse(JsonNode response) {
       // TODO
    }

    public static DiscoveryQuantityDistribution from(
        QuantityDistributionDescriptorArg quantityDistributionDescriptor
    ) {
        return new DiscoveryQuantityDistribution(
            quantityDistributionDescriptor.getLabel(),
            quantityDistributionDescriptor.getField()
        );
    }

    public class Slice {

        private final String label;
        private final Long count;

        public Slice(String label, Long count) {
            this.label = label;
            this.count = count;
        }

        public String getLabel() {
            return label;
        }

        public Long getCount() {
            return count;
        }

    }

}
