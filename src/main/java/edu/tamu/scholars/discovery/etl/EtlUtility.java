package edu.tamu.scholars.discovery.etl;

import static edu.tamu.scholars.discovery.AppConstants.DOT;
import static edu.tamu.scholars.discovery.AppUtility.getAfter;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import edu.tamu.scholars.discovery.etl.model.DataFieldDescriptor;

public class EtlUtility {

    private EtlUtility() {

    }

    public static String getFieldPrefix(DataFieldDescriptor descriptor) {
        String name = getFieldName(descriptor);

        return getAfter(name, DOT);
    }

    public static String getFieldName(DataFieldDescriptor descriptor) {
        return isNestedReference(descriptor)
            ? descriptor.getNestedReference().getKey()
            : descriptor.getName();
    }

    public static boolean isNestedReference(DataFieldDescriptor descriptor) {
        return descriptor.getNestedReference() != null
            && isNotEmpty(descriptor.getNestedReference().getKey());
    }

}
