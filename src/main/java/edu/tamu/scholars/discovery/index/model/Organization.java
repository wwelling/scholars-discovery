package edu.tamu.scholars.discovery.index.model;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import edu.tamu.scholars.discovery.index.annotation.CollectionSource;
import edu.tamu.scholars.discovery.index.annotation.FieldSource;
import edu.tamu.scholars.discovery.index.annotation.FieldType;
import edu.tamu.scholars.discovery.index.annotation.NestedObject;
import edu.tamu.scholars.discovery.index.annotation.NestedObject.Reference;

@JsonInclude(NON_EMPTY)
@CollectionSource(name = "organizations", predicate = "http://xmlns.com/foaf/0.1/Organization")
public class Organization extends Common {

    @FieldType(type = "text_general", copyTo = { "_text_", "name_t_sort" })
    @FieldSource(
        template = "organization/name",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private String name;

    @FieldType(type = "text_general", copyTo = "_text_")
    @FieldSource(
        template = "organization/overview",
        predicate = "http://vivoweb.org/ontology/core#overview"
    )
    private String overview;

    @NestedObject
    @FieldType(type = "strings", copyTo = "_text_")
    @FieldSource(
        template = "organization/offersDegree",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> degrees;

    @FieldType(type = "string", copyTo = "_text_")
    @FieldSource(
        template = "organization/abbreviation",
        predicate = "http://vivoweb.org/ontology/core#abbreviation"
    )
    private String abbreviation;

    @FieldType(type = "pdate", docValues = true)
    @FieldSource(
        template = "organization/date",
        predicate = "http://vivoweb.org/ontology/core#dateTime"
    )
    private String date;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "organization/sponsorsAwardOrHonor",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> sponsorsAwardOrHonor;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "organization/awardOrHonorGiven",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> awardOrHonorGiven;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "organization/awardOrHonorReceived",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> awardOrHonorReceived;

    @FieldType(type = "strings", copyTo = "_text_")
    @FieldSource(
        template = "organization/keyword",
        predicate = "http://vivoweb.org/ontology/core#freetextKeyword"
    )
    private List<String> keywords;

    @FieldType(type = "strings", indexed = false)
    @NestedObject(properties = {
        @Reference(value = "organizationForTrainingTrainee", key = "trainee"),
        @Reference(value = "organizationForTrainingDegree", key = "degree"),
        @Reference(value = "organizationForTrainingStartDate", key = "startDate"),
        @Reference(value = "organizationForTrainingEndDate", key = "endDate")
    })
    @FieldSource(
        template = "organization/organizationForTraining",
        predicate = "http://vivoweb.org/ontology/core#majorField"
    )
    private List<String> organizationForTraining;

    @NestedObject(root = false)
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "organization/organizationForTrainingTrainee",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> organizationForTrainingTrainee;

    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "organization/organizationForTrainingDegree",
        predicate = "http://vivoweb.org/ontology/core#abbreviation"
    )
    private List<String> organizationForTrainingDegree;

    @FieldType(type = "pdates", indexed = false)
    @FieldSource(
        template = "organization/organizationForTrainingStartDate",
        predicate = "http://vivoweb.org/ontology/core#dateTime"
    )
    private List<String> organizationForTrainingStartDate;

    @FieldType(type = "pdates", indexed = false)
    @FieldSource(
        template = "organization/organizationForTrainingEndDate",
        predicate = "http://vivoweb.org/ontology/core#dateTime"
    )
    private List<String> organizationForTrainingEndDate;

    @FieldType(type = "strings")
    @NestedObject(properties = {
        @Reference(value = "peopleType", key = "type"),
        @Reference(value = "peopleTitle", key = "title")
    })
    @FieldSource(
        template = "organization/people",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> people;

    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "organization/peopleType",
        predicate = "http://vitro.mannlib.cornell.edu/ns/vitro/0.7#mostSpecificType",
        parse = true
    )
    private List<String> peopleType;

    @FieldType(type = "strings")
    @FieldSource(
        template = "organization/peopleTitle",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> peopleTitle;

    @NestedObject(properties = {
        @Reference(value = "hasSubOrganizationType", key = "type")
    })
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "organization/hasSubOrganization",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> hasSubOrganizations;

    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "organization/hasSubOrganizationType",
        predicate = "http://vitro.mannlib.cornell.edu/ns/vitro/0.7#mostSpecificType",
        parse = true
    )
    private List<String> hasSubOrganizationType;

    @NestedObject
    @FieldType(type = "strings", docValues = true)
    @FieldSource(
        template = "organization/organizationWithin",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> organizationWithin;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "organization/leadOrganizationOf",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> leadOrganizationOf;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "organization/hasCollaboratingOrganizationOrGroup",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> hasCollaboratingOrganizationOrGroup;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "organization/hasAffiliatedOrganization",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> hasAffiliatedOrganizations;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "organization/memberOf",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> memberOf;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "organization/clinicalActivity",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> clinicalActivities;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "organization/convenerOfEvent",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> convenerOfEvents;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "organization/attendedEvent",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> attendedEvents;

    @NestedObject
    @FieldType(type = "text_general", copyTo = "_text_")
    @FieldSource(
        template = "organization/publication",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> publications;

    @NestedObject
    @FieldType(type = "strings")
    @FieldSource(
        template = "organization/publisherOf",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label",
        unique = true
    )
    private List<String> publisherOf;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "organization/presentation",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> presentations;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "organization/featuredIn",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> featuredIn;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "organization/assigneeForPatent",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> assigneeForPatent;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "organization/translatorOf",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> translatorOf;

    @FieldType(type = "strings")
    @NestedObject(properties = { @Reference(value = "awardsGrantDate", key = "date") })
    @FieldSource(
        template = "organization/awardsGrant",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> awardsGrant;

    @FieldType(type = "pdates", indexed = false)
    @FieldSource(
        template = "organization/awardsGrantDate",
        predicate = "http://vivoweb.org/ontology/core#dateTime"
    )
    private List<String> awardsGrantDate;

    @FieldType(type = "strings", indexed = false)
    @NestedObject(properties = { @Reference(value = "administersGrantDate", key = "date") })
    @FieldSource(
        template = "organization/administersGrant",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> administersGrant;

    @FieldType(type = "pdates", indexed = false)
    @FieldSource(
        template = "organization/administersGrantDate",
        predicate = "http://vivoweb.org/ontology/core#dateTime"
    )
    private List<String> administersGrantDate;

    @FieldType(type = "strings", indexed = false)
    @NestedObject(properties = { @Reference(value = "subcontractsGrantDate", key = "date") })
    @FieldSource(
        template = "organization/subcontractsGrant",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> subcontractsGrant;

    @FieldType(type = "pdates", indexed = false)
    @FieldSource(
        template = "organization/subcontractsGrantDate",
        predicate = "http://vivoweb.org/ontology/core#dateTime"
    )
    private List<String> subcontractsGrantDate;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "organization/performsHumanStudy",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> performsHumanStudy;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "organization/contractOrProviderForService",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> contractOrProviderForService;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "organization/outreachAndCommunityServiceActivity",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> outreachAndCommunityServiceActivities;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "organization/hasEquipment",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> hasEquipment;

    @NestedObject
    @FieldType(type = "strings")
    @FieldSource(
        template = "organization/offersCourse",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> courses;

    @FieldType(type = "string", indexed = false)
    @FieldSource(
        template = "organization/phone",
        predicate = "http://www.w3.org/2006/vcard/ns#telephone"
    )
    private String phone;

    @FieldType(type = "string", indexed = false)
    @FieldSource(
        template = "organization/fax",
        predicate = "http://www.w3.org/2006/vcard/ns#fax"
    )
    private String fax;

    @FieldType(type = "string", indexed = false)
    @FieldSource(
        template = "organization/emailAddress",
        predicate = "http://www.w3.org/2006/vcard/ns#email"
    )
    private String emailAddress;

    @FieldType(type = "string", indexed = false)
    @FieldSource(
        template = "organization/streetAddress",
        predicate = "organization.streetAddress"
    )
    private String streetAddress;

    @FieldType(type = "string", indexed = false)
    @FieldSource(
        template = "organization/locality",
        predicate = "organization.locality"
    )
    private String locality;

    @FieldType(type = "string", indexed = false)
    @FieldSource(
        template = "organization/region",
        predicate = "organization.region"
    )
    private String region;

    @FieldType(type = "string", indexed = false)
    @FieldSource(
        template = "organization/postalCode",
        predicate = "organization.postalCode"
    )
    private String postalCode;

    @FieldType(type = "string", indexed = false)
    @FieldSource(
        template = "organization/country",
        predicate = "organization.country"
    )
    private String country;

    @FieldType(type = "string", indexed = false)
    @FieldSource(
        template = "organization/geographicLocation",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private String geographicLocation;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "organization/locatedAtFacility",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> locatedAtFacilities;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "organization/predecessorOrganization",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> predecessorOrganizations;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "organization/successorOrganization",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> successorOrganizations;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "organization/governingAuthorityFor",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> governingAuthorityFor;

    @NestedObject
    @FieldType(type = "strings", docValues = true)
    @FieldSource(
        template = "organization/affiliatedResearchArea",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label",
        unique = true
    )
    private List<String> affiliatedResearchAreas;

    @FieldType(type = "string", indexed = false)
    @FieldSource(
        template = "organization/orgId",
        predicate = "http://vivo.library.tamu.edu/ontology/TAMU#OrgID"
    )
    private String orgId;

    public Organization() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public List<String> getDegrees() {
        return degrees;
    }

    public void setDegrees(List<String> degrees) {
        this.degrees = degrees;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getSponsorsAwardOrHonor() {
        return sponsorsAwardOrHonor;
    }

    public void setSponsorsAwardOrHonor(List<String> sponsorsAwardOrHonor) {
        this.sponsorsAwardOrHonor = sponsorsAwardOrHonor;
    }

    public List<String> getAwardOrHonorGiven() {
        return awardOrHonorGiven;
    }

    public void setAwardOrHonorGiven(List<String> awardOrHonorGiven) {
        this.awardOrHonorGiven = awardOrHonorGiven;
    }

    public List<String> getAwardOrHonorReceived() {
        return awardOrHonorReceived;
    }

    public void setAwardOrHonorReceived(List<String> awardOrHonorReceived) {
        this.awardOrHonorReceived = awardOrHonorReceived;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public List<String> getOrganizationForTraining() {
        return organizationForTraining;
    }

    public void setOrganizationForTraining(List<String> organizationForTraining) {
        this.organizationForTraining = organizationForTraining;
    }

    public List<String> getOrganizationForTrainingTrainee() {
        return organizationForTrainingTrainee;
    }

    public void setOrganizationForTrainingTrainee(List<String> organizationForTrainingTrainee) {
        this.organizationForTrainingTrainee = organizationForTrainingTrainee;
    }

    public List<String> getOrganizationForTrainingDegree() {
        return organizationForTrainingDegree;
    }

    public void setOrganizationForTrainingDegree(List<String> organizationForTrainingDegree) {
        this.organizationForTrainingDegree = organizationForTrainingDegree;
    }

    public List<String> getOrganizationForTrainingStartDate() {
        return organizationForTrainingStartDate;
    }

    public void setOrganizationForTrainingStartDate(List<String> organizationForTrainingStartDate) {
        this.organizationForTrainingStartDate = organizationForTrainingStartDate;
    }

    public List<String> getOrganizationForTrainingEndDate() {
        return organizationForTrainingEndDate;
    }

    public void setOrganizationForTrainingEndDate(List<String> organizationForTrainingEndDate) {
        this.organizationForTrainingEndDate = organizationForTrainingEndDate;
    }

    public List<String> getPeople() {
        return people;
    }

    public void setPeople(List<String> people) {
        this.people = people;
    }

    public List<String> getPeopleType() {
        return peopleType;
    }

    public void setPeopleType(List<String> peopleType) {
        this.peopleType = peopleType;
    }

    public List<String> getPeopleTitle() {
        return peopleTitle;
    }

    public void setPeopleTitle(List<String> peopleTitle) {
        this.peopleTitle = peopleTitle;
    }

    public List<String> getHasSubOrganizations() {
        return hasSubOrganizations;
    }

    public void setHasSubOrganizations(List<String> hasSubOrganizations) {
        this.hasSubOrganizations = hasSubOrganizations;
    }

    public List<String> getHasSubOrganizationType() {
        return hasSubOrganizationType;
    }

    public void setHasSubOrganizationType(List<String> hasSubOrganizationType) {
        this.hasSubOrganizationType = hasSubOrganizationType;
    }

    public List<String> getOrganizationWithin() {
        return organizationWithin;
    }

    public void setOrganizationWithin(List<String> organizationWithin) {
        this.organizationWithin = organizationWithin;
    }

    public List<String> getLeadOrganizationOf() {
        return leadOrganizationOf;
    }

    public void setLeadOrganizationOf(List<String> leadOrganizationOf) {
        this.leadOrganizationOf = leadOrganizationOf;
    }

    public List<String> getHasCollaboratingOrganizationOrGroup() {
        return hasCollaboratingOrganizationOrGroup;
    }

    public void setHasCollaboratingOrganizationOrGroup(List<String> hasCollaboratingOrganizationOrGroup) {
        this.hasCollaboratingOrganizationOrGroup = hasCollaboratingOrganizationOrGroup;
    }

    public List<String> getHasAffiliatedOrganizations() {
        return hasAffiliatedOrganizations;
    }

    public void setHasAffiliatedOrganizations(List<String> hasAffiliatedOrganizations) {
        this.hasAffiliatedOrganizations = hasAffiliatedOrganizations;
    }

    public List<String> getMemberOf() {
        return memberOf;
    }

    public void setMemberOf(List<String> memberOf) {
        this.memberOf = memberOf;
    }

    public List<String> getClinicalActivities() {
        return clinicalActivities;
    }

    public void setClinicalActivities(List<String> clinicalActivities) {
        this.clinicalActivities = clinicalActivities;
    }

    public List<String> getConvenerOfEvents() {
        return convenerOfEvents;
    }

    public void setConvenerOfEvents(List<String> convenerOfEvents) {
        this.convenerOfEvents = convenerOfEvents;
    }

    public List<String> getAttendedEvents() {
        return attendedEvents;
    }

    public void setAttendedEvents(List<String> attendedEvents) {
        this.attendedEvents = attendedEvents;
    }

    public List<String> getPublications() {
        return publications;
    }

    public void setPublications(List<String> publications) {
        this.publications = publications;
    }

    public List<String> getPublisherOf() {
        return publisherOf;
    }

    public void setPublisherOf(List<String> publisherOf) {
        this.publisherOf = publisherOf;
    }

    public List<String> getPresentations() {
        return presentations;
    }

    public void setPresentations(List<String> presentations) {
        this.presentations = presentations;
    }

    public List<String> getFeaturedIn() {
        return featuredIn;
    }

    public void setFeaturedIn(List<String> featuredIn) {
        this.featuredIn = featuredIn;
    }

    public List<String> getAssigneeForPatent() {
        return assigneeForPatent;
    }

    public void setAssigneeForPatent(List<String> assigneeForPatent) {
        this.assigneeForPatent = assigneeForPatent;
    }

    public List<String> getTranslatorOf() {
        return translatorOf;
    }

    public void setTranslatorOf(List<String> translatorOf) {
        this.translatorOf = translatorOf;
    }

    public List<String> getAwardsGrant() {
        return awardsGrant;
    }

    public void setAwardsGrant(List<String> awardsGrant) {
        this.awardsGrant = awardsGrant;
    }

    public List<String> getAwardsGrantDate() {
        return awardsGrantDate;
    }

    public void setAwardsGrantDate(List<String> awardsGrantDate) {
        this.awardsGrantDate = awardsGrantDate;
    }

    public List<String> getAdministersGrant() {
        return administersGrant;
    }

    public void setAdministersGrant(List<String> administersGrant) {
        this.administersGrant = administersGrant;
    }

    public List<String> getAdministersGrantDate() {
        return administersGrantDate;
    }

    public void setAdministersGrantDate(List<String> administersGrantDate) {
        this.administersGrantDate = administersGrantDate;
    }

    public List<String> getSubcontractsGrant() {
        return subcontractsGrant;
    }

    public void setSubcontractsGrant(List<String> subcontractsGrant) {
        this.subcontractsGrant = subcontractsGrant;
    }

    public List<String> getSubcontractsGrantDate() {
        return subcontractsGrantDate;
    }

    public void setSubcontractsGrantDate(List<String> subcontractsGrantDate) {
        this.subcontractsGrantDate = subcontractsGrantDate;
    }

    public List<String> getPerformsHumanStudy() {
        return performsHumanStudy;
    }

    public void setPerformsHumanStudy(List<String> performsHumanStudy) {
        this.performsHumanStudy = performsHumanStudy;
    }

    public List<String> getContractOrProviderForService() {
        return contractOrProviderForService;
    }

    public void setContractOrProviderForService(List<String> contractOrProviderForService) {
        this.contractOrProviderForService = contractOrProviderForService;
    }

    public List<String> getOutreachAndCommunityServiceActivities() {
        return outreachAndCommunityServiceActivities;
    }

    public void setOutreachAndCommunityServiceActivities(List<String> outreachAndCommunityServiceActivities) {
        this.outreachAndCommunityServiceActivities = outreachAndCommunityServiceActivities;
    }

    public List<String> getHasEquipment() {
        return hasEquipment;
    }

    public void setHasEquipment(List<String> hasEquipment) {
        this.hasEquipment = hasEquipment;
    }

    public List<String> getCourses() {
        return courses;
    }

    public void setCourses(List<String> courses) {
        this.courses = courses;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGeographicLocation() {
        return geographicLocation;
    }

    public void setGeographicLocation(String geographicLocation) {
        this.geographicLocation = geographicLocation;
    }

    public List<String> getLocatedAtFacilities() {
        return locatedAtFacilities;
    }

    public void setLocatedAtFacilities(List<String> locatedAtFacilities) {
        this.locatedAtFacilities = locatedAtFacilities;
    }

    public List<String> getPredecessorOrganizations() {
        return predecessorOrganizations;
    }

    public void setPredecessorOrganizations(List<String> predecessorOrganizations) {
        this.predecessorOrganizations = predecessorOrganizations;
    }

    public List<String> getSuccessorOrganizations() {
        return successorOrganizations;
    }

    public void setSuccessorOrganizations(List<String> successorOrganizations) {
        this.successorOrganizations = successorOrganizations;
    }

    public List<String> getGoverningAuthorityFor() {
        return governingAuthorityFor;
    }

    public void setGoverningAuthorityFor(List<String> governingAuthorityFor) {
        this.governingAuthorityFor = governingAuthorityFor;
    }

    public List<String> getAffiliatedResearchAreas() {
        return affiliatedResearchAreas;
    }

    public void setAffiliatedResearchAreas(List<String> affiliatedResearchAreas) {
        this.affiliatedResearchAreas = affiliatedResearchAreas;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

}
