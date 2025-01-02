package edu.tamu.scholars.discovery.controller.response;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import edu.tamu.scholars.discovery.controller.argument.AcademicAgeDescriptorArg;

public class DiscoveryAcademicAge {

    private final String label;

    private final String dateField;

    private final Map<String, String> ranges;

    private final List<AgeGroup> groups;

    private double mean;

    private int median;

    public DiscoveryAcademicAge(String label, String dateField) {
        this.label = label;
        this.dateField = dateField;
        this.ranges = new HashMap<>();
        this.groups = new ArrayList<>();
    }

    private synchronized void add(Integer index, String range, String label, Integer value) {
        if (!ranges.containsKey(label)) {
            ranges.put(label, range);
            groups.add(new AgeGroup(index, label, value));
        }
    }

    public void from(AcademicAgeDescriptorArg academicAgeDescriptor, JsonNode results) {
        // TODO
    }

    public String getLabel() {
        return label;
    }

    public String getDateField() {
        return dateField;
    }

    public Map<String, String> getRanges() {
        return ranges;
    }

    public List<AgeGroup> getGroups() {
        return groups;
    }

    public double getMean() {
        return this.mean;
    }

    public int getMedian() {
        return this.median;
    }

    public static DiscoveryAcademicAge create(String label, String dateField) {
        return new DiscoveryAcademicAge(label, dateField);
    }

    public class AgeGroup {
        private final Integer index;
        private final String label;
        private final Integer value;

        private AgeGroup(Integer index, String label, Integer value) {
            this.index = index;
            this.label = label;
            this.value = value;
        }

        public Integer getIndex() {
            return index;
        }

        public String getLabel() {
            return label;
        }

        public Integer getValue() {
            return value;
        }
    }

    private class AgeGroupComparator implements Comparator<AgeGroup> {

        @Override
        public int compare(AgeGroup o1, AgeGroup o2) {
            return o1.getIndex().compareTo(o2.getIndex());
        }

    }

}
