package edu.tamu.scholars.discovery.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.scholars.discovery.controller.argument.DiscoveryAcademicAgeDescriptor;
import edu.tamu.scholars.discovery.controller.argument.DiscoveryQuantityDistributionDescriptor;
import edu.tamu.scholars.discovery.controller.argument.FilterArg;
import edu.tamu.scholars.discovery.controller.argument.QueryArg;
import edu.tamu.scholars.discovery.controller.response.DiscoveryAcademicAge;
import edu.tamu.scholars.discovery.controller.response.DiscoveryQuantityDistribution;
import edu.tamu.scholars.discovery.index.model.repo.IndividualRepo;

@RestController
@RequestMapping("/individual/analytics")
public class IndividualAnalyticsController {

    @Lazy
    @Autowired
    private IndividualRepo repo;

    @GetMapping("/academicAge")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DiscoveryAcademicAge> academicAge(
        QueryArg query,
        List<FilterArg> filters,
        @RequestParam(name = "label", defaultValue = "Research") String label,
        @RequestParam(name = "dateField", defaultValue = "publicationDate") String dateField,
        @RequestParam(name = "accumulateMultivaluedDate", defaultValue = "false") Boolean accumulateMultivaluedDate,
        // use case: average number of publications out of set of people in age group
        @RequestParam(name = "averageOverInterval", defaultValue = "false") Boolean averageOverInterval,
        @RequestParam(name = "upperLimitInYears", defaultValue = "40") Integer upperLimitInYears,
        @RequestParam(name = "groupingIntervalInYears", defaultValue = "5") Integer groupingIntervalInYears) {

        return ResponseEntity.ok(
            repo.academicAge(
                DiscoveryAcademicAgeDescriptor.of(
                    label,
                    dateField,
                    accumulateMultivaluedDate,
                    averageOverInterval,
                    upperLimitInYears,
                    groupingIntervalInYears
                ),
                query,
                filters
            )
        );
    }

    @GetMapping("/quantityDistribution")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DiscoveryQuantityDistribution> quantityDistribution(
        QueryArg query,
        List<FilterArg> filters,
        @RequestParam(name = "label", defaultValue = "UN SDG") String label,
        @RequestParam(name = "field", defaultValue = "tags") String field) {

        return ResponseEntity.ok(
            repo.quantityDistribution(
                DiscoveryQuantityDistributionDescriptor.of(label, field),
                query,
                filters
            )
        );
    }

}
