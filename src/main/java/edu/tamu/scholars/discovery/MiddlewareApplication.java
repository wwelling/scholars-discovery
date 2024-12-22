package edu.tamu.scholars.discovery;

import java.util.TimeZone;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import edu.tamu.scholars.discovery.discovery.service.IndexService;

/**
 * Scholars discovery application and initialization.
 * 
 * <p>Enables {@link Scheduled} annotation used in {@link IndexService}.</p>
 */
@EnableScheduling
@SpringBootApplication
public class MiddlewareApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiddlewareApplication.class, args);
    }

    @PostConstruct
    public void initializeTimeZone() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

}
