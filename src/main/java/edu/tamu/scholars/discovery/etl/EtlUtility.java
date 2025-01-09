package edu.tamu.scholars.discovery.etl;

import static edu.tamu.scholars.discovery.AppConstants.DOT;
import static edu.tamu.scholars.discovery.AppUtility.getBefore;

import edu.tamu.scholars.discovery.etl.model.DataFieldDescriptor;

public class EtlUtility {

    private EtlUtility() {

    }

    public static String getFieldPrefix(DataFieldDescriptor descriptor) {
        String name = descriptor.getName();

        return getBefore(name, DOT);
    }

}
