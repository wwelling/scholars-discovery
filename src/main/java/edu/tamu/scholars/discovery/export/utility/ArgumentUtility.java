package edu.tamu.scholars.discovery.export.utility;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import edu.tamu.scholars.discovery.export.argument.ExportArg;

public class ArgumentUtility {

    private static final String EXPORT_QUERY_PARAM_KEY = "export";

    private ArgumentUtility() {

    }

    public static List<ExportArg> getExportArguments(HttpServletRequest request) {
        return Collections.list(request.getParameterNames()).stream()
            .filter(paramName -> paramName.equals(EXPORT_QUERY_PARAM_KEY))
            .map(request::getParameterValues)
            .map(Arrays::asList)
            .flatMap(Collection::stream)
            .map(ExportArg::of)
            .toList();
    }

}
