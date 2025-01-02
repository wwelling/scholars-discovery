package edu.tamu.scholars.discovery.controller;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.scholars.discovery.controller.argument.AcademicAgeDescriptorArg;
import edu.tamu.scholars.discovery.controller.argument.FilterArg;
import edu.tamu.scholars.discovery.controller.argument.QuantityDistributionDescriptorArg;
import edu.tamu.scholars.discovery.controller.argument.QueryArg;
import edu.tamu.scholars.discovery.controller.response.DiscoveryAcademicAge;
import edu.tamu.scholars.discovery.controller.response.DiscoveryQuantityDistribution;
import edu.tamu.scholars.discovery.model.repo.IndividualRepo;

@RestController
@RequestMapping("/individual/analytics")
public class IndividualAnalyticsController {

    private final IndividualRepo repo;

    IndividualAnalyticsController(@Lazy IndividualRepo repo) {
        this.repo = repo;
    }

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
                // TODO: add argument resolver for AcademicAgeDescriptorArg
                AcademicAgeDescriptorArg.of(
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
                // TODO: add argument resolver for QuantityDistributionDescriptorArg
                QuantityDistributionDescriptorArg.of(label, field),
                query,
                filters
            )
        );
    }

}
