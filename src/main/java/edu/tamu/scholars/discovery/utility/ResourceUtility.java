package edu.tamu.scholars.discovery.utility;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;

import edu.tamu.scholars.discovery.MiddlewareApplication;

public class ResourceUtility {

    private ResourceUtility() {

    }

    public static InputStream getResource(String path) throws IOException {
        return new ClassPathResource(path, MiddlewareApplication.class.getClassLoader())
                .getInputStream();
    }

}
