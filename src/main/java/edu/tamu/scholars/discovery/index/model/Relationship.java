package edu.tamu.scholars.discovery.index.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static edu.tamu.scholars.discovery.index.DiscoveryConstants.ABSTRACT;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.tamu.scholars.discovery.index.annotation.CollectionSource;
import edu.tamu.scholars.discovery.index.annotation.FieldSource;
import edu.tamu.scholars.discovery.index.annotation.FieldType;
import edu.tamu.scholars.discovery.index.annotation.NestedObject;
import edu.tamu.scholars.discovery.index.annotation.NestedObject.Reference;

@JsonInclude(NON_EMPTY)
@CollectionSource(name = "relationships", predicate = "http://vivoweb.org/ontology/core#Relationship")
public class Relationship extends Common {

    @FieldType(type = "text_general", copyTo = { "_text_", "title_t_sort" })
    @FieldSource(
        template = "relationship/title",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private String title;

    @JsonProperty(ABSTRACT)
    @FieldType(type = "string", value = ABSTRACT, copyTo = "_text_")
    @FieldSource(
        template = "relationship/abstract",
        predicate = "http://purl.org/ontology/bibo/abstract"
    )
    private String abstractText;

    @FieldType(type = "string")
    @FieldSource(
        template = "relationship/description",
        predicate = "http://vivoweb.org/ontology/core#description"
    )
    private String description;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "relationship/organization",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> organization;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "relationship/receiptOf",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> receiptOf;

    @FieldType(type = "strings")
    @NestedObject(properties = {
        @Reference(value = "awardOrHonorForType", key = "type")
    })
    @FieldSource(
        template = "relationship/awardOrHonorFor",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> awardOrHonorFor;

    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "relationship/awardOrHonorForType",
        predicate = "http://vitro.mannlib.cornell.edu/ns/vitro/0.7#mostSpecificType",
        parse = true
    )
    private List<String> awardOrHonorForType;

    @FieldType(type = "strings", copyTo = "_text_")
    @NestedObject(properties = {
        @Reference(value = "awardConferredByType", key = "type"),
        @Reference(value = "awardConferredByAbbreviation", key = "abbreviation"),
        @Reference(value = "awardConferredByPreferredLabel", key = "preferredLabel")
    })
    @FieldSource(
        template = "relationship/awardConferredBy",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> awardConferredBy;

    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "relationship/awardConferredByType",
        predicate = "http://vitro.mannlib.cornell.edu/ns/vitro/0.7#mostSpecificType",
        parse = true
    )
    private List<String> awardConferredByType;

    @FieldType(type = "strings", copyTo = "_text_")
    @FieldSource(
        template = "relationship/awardConferredByAbbreviation",
        predicate = "http://vivoweb.org/ontology/core#abbreviation"
    )
    private List<String> awardConferredByAbbreviation;

    @FieldType(type = "strings", copyTo = "_text_")
    @FieldSource(
        template = "relationship/awardConferredByPreferredLabel",
        predicate = "http://vivo.library.tamu.edu/ontology/TAMU#awardConferredBy_label"
    )
    private List<String> awardConferredByPreferredLabel;

    @FieldType(type = "text_general", copyTo = { "_text_", "awardedBy_str" })
    @NestedObject(properties = {
        @Reference(value = "awardedByType", key = "type"),
        @Reference(value = "awardedByAbbreviation", key = "abbreviation"),
        @Reference(value = "awardedByPreferredLabel", key = "preferredLabel")
    })
    @FieldSource(
        template = "relationship/awardedBy",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> awardedBy;

    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "relationship/awardedByType",
        predicate = "http://vitro.mannlib.cornell.edu/ns/vitro/0.7#mostSpecificType",
        parse = true
    )
    private List<String> awardedByType;

    @FieldType(type = "strings", copyTo = "_text_")
    @FieldSource(
        template = "relationship/awardedByAbbreviation",
        predicate = "http://vivoweb.org/ontology/core#abbreviation"
    )
    private List<String> awardedByAbbreviation;

    @FieldType(type = "strings", copyTo = "_text_")
    @FieldSource(
        template = "relationship/awardedByPreferredLabel",
        predicate = "http://vivo.library.tamu.edu/ontology/TAMU#awardedBy_label"
    )
    private List<String> awardedByPreferredLabel;

    @FieldType(type = "strings", indexed = false)
    @NestedObject(properties = {
        @Reference(value = "grantSubcontractedThroughType", key = "type")
    })
    @FieldSource(
        template = "relationship/grantSubcontractedThrough",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> grantSubcontractedThrough;

    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "relationship/grantSubcontractedThroughType",
        predicate = "http://vitro.mannlib.cornell.edu/ns/vitro/0.7#mostSpecificType",
        parse = true
    )
    private List<String> grantSubcontractedThroughType;

    @NestedObject
    @FieldType(type = "strings", docValues = true)
    @FieldSource(
        template = "relationship/administeredBy",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> administeredBy;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "relationship/subGrant",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> subGrant;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "relationship/subGrantOf",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> subGrantOf;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "relationship/providesFundingFor",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> providesFundingFor;

    @FieldType(type = "string", indexed = false)
    @FieldSource(
        template = "relationship/totalAwardAmount",
        predicate = "http://vivoweb.org/ontology/core#totalAwardAmount"
    )
    private String totalAwardAmount;

    @FieldType(type = "string", indexed = false)
    @FieldSource(
        template = "relationship/directCosts",
        predicate = "http://vivoweb.org/ontology/core#directCosts"
    )
    private String directCosts;

    @FieldType(type = "string", indexed = false)
    @FieldSource(
        template = "relationship/sponsorAwardId",
        predicate = "http://vivoweb.org/ontology/core#sponsorAwardId"
    )
    private String sponsorAwardId;

    @FieldType(type = "string", indexed = false)
    @FieldSource(
        template = "relationship/localAwardId",
        predicate = "http://vivoweb.org/ontology/core#localAwardId"
    )
    private String localAwardId;

    @FieldType(type = "text_general", copyTo = "_text_")
    @NestedObject(properties = {
        @Reference(value = "contributorRole", key = "role"),
        @Reference(value = "contributorOrganization", key = "organizations")
    })
    @FieldSource(
        template = "relationship/contributor",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> contributors;

    @FieldType(type = "strings")
    @FieldSource(
        template = "relationship/contributorRole",
        predicate = "http://vitro.mannlib.cornell.edu/ns/vitro/0.7#mostSpecificType",
        parse = true
    )
    private List<String> contributorRole;

    @FieldType(type = "strings", docValues = true)
    @FieldSource(
        template = "relationship/contributorOrganization",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> contributorOrganization;

    @NestedObject
    @FieldType(type = "strings", docValues = true, copyTo = "_text_")
    @FieldSource(
        template = "relationship/principalInvestigator",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> principalInvestigators;

    @NestedObject
    @FieldType(type = "strings", docValues = true, copyTo = "_text_")
    @FieldSource(
        template = "relationship/coPrincipalInvestigator",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> coPrincipalInvestigators;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "relationship/supportedPublicationOrOtherWork",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> supportedPublicationOrOtherWork;

    @FieldType(type = "pdate", docValues = true)
    @FieldSource(
        template = "relationship/startDateTime",
        predicate = "http://vivoweb.org/ontology/core#dateTime"
    )
    private String startDateTime;

    @FieldType(type = "pdate", docValues = true)
    @FieldSource(
        template = "relationship/endDateTime",
        predicate = "http://vivoweb.org/ontology/core#dateTime"
    )
    private String endDateTime;

    @NestedObject
    @FieldType(type = "strings")
    @FieldSource(
        template = "relationship/hasSubjectArea",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> subjectAreas;

    @FieldType(type = "pdate", docValues = true)
    @FieldSource(
        template = "relationship/yearAwarded",
        predicate = "http://vivoweb.org/ontology/core#dateTime"
    )
    private String yearAwarded;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "relationship/inheresIn",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> inheresIn;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "relationship/isSpecifiedOutputOf",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> specifiedOutputOf;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "relationship/outputOf",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> outputOf;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "relationship/participatesIn",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> participatesIn;

    public Relationship() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getOrganization() {
        return organization;
    }

    public void setOrganization(List<String> organization) {
        this.organization = organization;
    }

    public List<String> getReceiptOf() {
        return receiptOf;
    }

    public void setReceiptOf(List<String> receiptOf) {
        this.receiptOf = receiptOf;
    }

    public List<String> getAwardOrHonorFor() {
        return awardOrHonorFor;
    }

    public void setAwardOrHonorFor(List<String> awardOrHonorFor) {
        this.awardOrHonorFor = awardOrHonorFor;
    }

    public List<String> getAwardOrHonorForType() {
        return awardOrHonorForType;
    }

    public void setAwardOrHonorForType(List<String> awardOrHonorForType) {
        this.awardOrHonorForType = awardOrHonorForType;
    }

    public List<String> getAwardConferredBy() {
        return awardConferredBy;
    }

    public void setAwardConferredBy(List<String> awardConferredBy) {
        this.awardConferredBy = awardConferredBy;
    }

    public List<String> getAwardConferredByType() {
        return awardConferredByType;
    }

    public void setAwardConferredByType(List<String> awardConferredByType) {
        this.awardConferredByType = awardConferredByType;
    }

    public List<String> getAwardConferredByAbbreviation() {
        return awardConferredByAbbreviation;
    }

    public void setAwardConferredByAbbreviation(List<String> awardConferredByAbbreviation) {
        this.awardConferredByAbbreviation = awardConferredByAbbreviation;
    }

    public List<String> getAwardConferredByPreferredLabel() {
        return awardConferredByPreferredLabel;
    }

    public void setAwardConferredByPreferredLabel(List<String> awardConferredByPreferredLabel) {
        this.awardConferredByPreferredLabel = awardConferredByPreferredLabel;
    }

    public List<String> getAwardedBy() {
        return awardedBy;
    }

    public void setAwardedBy(List<String> awardedBy) {
        this.awardedBy = awardedBy;
    }

    public List<String> getAwardedByType() {
        return awardedByType;
    }

    public void setAwardedByType(List<String> awardedByType) {
        this.awardedByType = awardedByType;
    }

    public List<String> getAwardedByAbbreviation() {
        return awardedByAbbreviation;
    }

    public void setAwardedByAbbreviation(List<String> awardedByAbbreviation) {
        this.awardedByAbbreviation = awardedByAbbreviation;
    }

    public List<String> getAwardedByPreferredLabel() {
        return awardedByPreferredLabel;
    }

    public void setAwardedByPreferredLabel(List<String> awardedByPreferredLabel) {
        this.awardedByPreferredLabel = awardedByPreferredLabel;
    }

    public List<String> getGrantSubcontractedThrough() {
        return grantSubcontractedThrough;
    }

    public void setGrantSubcontractedThrough(List<String> grantSubcontractedThrough) {
        this.grantSubcontractedThrough = grantSubcontractedThrough;
    }

    public List<String> getGrantSubcontractedThroughType() {
        return grantSubcontractedThroughType;
    }

    public void setGrantSubcontractedThroughType(List<String> grantSubcontractedThroughType) {
        this.grantSubcontractedThroughType = grantSubcontractedThroughType;
    }

    public List<String> getAdministeredBy() {
        return administeredBy;
    }

    public void setAdministeredBy(List<String> administeredBy) {
        this.administeredBy = administeredBy;
    }

    public List<String> getSubGrant() {
        return subGrant;
    }

    public void setSubGrant(List<String> subGrant) {
        this.subGrant = subGrant;
    }

    public List<String> getSubGrantOf() {
        return subGrantOf;
    }

    public void setSubGrantOf(List<String> subGrantOf) {
        this.subGrantOf = subGrantOf;
    }

    public List<String> getProvidesFundingFor() {
        return providesFundingFor;
    }

    public void setProvidesFundingFor(List<String> providesFundingFor) {
        this.providesFundingFor = providesFundingFor;
    }

    public String getTotalAwardAmount() {
        return totalAwardAmount;
    }

    public void setTotalAwardAmount(String totalAwardAmount) {
        this.totalAwardAmount = totalAwardAmount;
    }

    public String getDirectCosts() {
        return directCosts;
    }

    public void setDirectCosts(String directCosts) {
        this.directCosts = directCosts;
    }

    public String getSponsorAwardId() {
        return sponsorAwardId;
    }

    public void setSponsorAwardId(String sponsorAwardId) {
        this.sponsorAwardId = sponsorAwardId;
    }

    public String getLocalAwardId() {
        return localAwardId;
    }

    public void setLocalAwardId(String localAwardId) {
        this.localAwardId = localAwardId;
    }

    public List<String> getContributors() {
        return contributors;
    }

    public void setContributors(List<String> contributors) {
        this.contributors = contributors;
    }

    public List<String> getContributorRole() {
        return contributorRole;
    }

    public void setContributorRole(List<String> contributorRole) {
        this.contributorRole = contributorRole;
    }

    public List<String> getContributorOrganization() {
        return contributorOrganization;
    }

    public void setContributorOrganization(List<String> contributorOrganization) {
        this.contributorOrganization = contributorOrganization;
    }

    public List<String> getPrincipalInvestigators() {
        return principalInvestigators;
    }

    public void setPrincipalInvestigators(List<String> principalInvestigators) {
        this.principalInvestigators = principalInvestigators;
    }

    public List<String> getCoPrincipalInvestigators() {
        return coPrincipalInvestigators;
    }

    public void setCoPrincipalInvestigators(List<String> coPrincipalInvestigators) {
        this.coPrincipalInvestigators = coPrincipalInvestigators;
    }

    public List<String> getSupportedPublicationOrOtherWork() {
        return supportedPublicationOrOtherWork;
    }

    public void setSupportedPublicationOrOtherWork(List<String> supportedPublicationOrOtherWork) {
        this.supportedPublicationOrOtherWork = supportedPublicationOrOtherWork;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public List<String> getSubjectAreas() {
        return subjectAreas;
    }

    public void setSubjectAreas(List<String> subjectAreas) {
        this.subjectAreas = subjectAreas;
    }

    public String getYearAwarded() {
        return yearAwarded;
    }

    public void setYearAwarded(String yearAwarded) {
        this.yearAwarded = yearAwarded;
    }

    public List<String> getInheresIn() {
        return inheresIn;
    }

    public void setInheresIn(List<String> inheresIn) {
        this.inheresIn = inheresIn;
    }

    public List<String> getSpecifiedOutputOf() {
        return specifiedOutputOf;
    }

    public void setSpecifiedOutputOf(List<String> specifiedOutputOf) {
        this.specifiedOutputOf = specifiedOutputOf;
    }

    public List<String> getOutputOf() {
        return outputOf;
    }

    public void setOutputOf(List<String> outputOf) {
        this.outputOf = outputOf;
    }

    public List<String> getParticipatesIn() {
        return participatesIn;
    }

    public void setParticipatesIn(List<String> participatesIn) {
        this.participatesIn = participatesIn;
    }

}
