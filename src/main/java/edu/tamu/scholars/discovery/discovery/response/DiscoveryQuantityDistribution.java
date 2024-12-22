package edu.tamu.scholars.discovery.discovery.response;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import edu.tamu.scholars.discovery.discovery.argument.DiscoveryQuantityDistributionDescriptor;

/**
 * 
 */
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
        // response
        //     .getFacetField(field)
        //     .getValues()
        //     .stream()
        //     .sorted(new Comparator<Count>() {

        //         @Override
        //         public int compare(Count o1, Count o2) {
        //             return Long.compare(o2.getCount(), o1.getCount());
        //         }

        //     }).forEach(value -> {
        //         distribution.add(new Slice(value.getName(), value.getCount()));
        //         total += value.getCount();
        //     });
    }

    public static DiscoveryQuantityDistribution from(
        DiscoveryQuantityDistributionDescriptor quantityDistributionDescriptor
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
