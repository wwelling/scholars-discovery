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
@CollectionSource(name = "collections", predicate = "http://purl.org/ontology/bibo/Collection")
public class Collection extends Common {

    @FieldType(type = "text_general", copyTo = { "_text_", "name_t_sort" })
    @FieldSource(
        template = "collection/name",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private String name;

    @JsonProperty(ABSTRACT)
    @FieldType(type = "text_general", name = ABSTRACT, copyTo = "_text_")
    @FieldSource(
        template = "collection/abstract",
        predicate = "http://purl.org/ontology/bibo/abstract"
    )
    private String abstractText;

    @FieldType(type = "string")
    @FieldSource(
        template = "collection/abbreviation",
        predicate = "http://vivoweb.org/ontology/core#abbreviation"
    )
    private String abbreviation;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "collection/publicationVenueFor",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> publicationVenueFor;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "collection/editor",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> editors;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "collection/translator",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> translators;

    @FieldType(type = "pdate", docValues = true)
    @FieldSource(
        template = "collection/publicationDate",
        predicate = "http://vivoweb.org/ontology/core#dateTime"
    )
    private String publicationDate;

    @FieldType(type = "string")
    @NestedObject(properties = { @Reference(value = "publisherType", key = "type") })
    @FieldSource(
        template = "collection/publisher",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label",
        unique = true
    )
    private String publisher;

    @FieldType(type = "string")
    @FieldSource(
        template = "collection/publisherType",
        predicate = "http://vitro.mannlib.cornell.edu/ns/vitro/0.7#mostSpecificType",
        parse = true
    )
    private String publisherType;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "collection/hasSubjectArea",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> subjectAreas;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "collection/feature",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> features;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "collection/outputOfProcessOrEvent",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> outputOfProcessOrEvent;

    @FieldType(type = "strings", copyTo = "_text_")
    @FieldSource(
        template = "collection/keyword",
        predicate = "http://vivoweb.org/ontology/core#freetextKeyword"
    )
    private List<String> keywords;

    @FieldType(type = "string")
    @FieldSource(
        template = "collection/issn",
        predicate = "http://purl.org/ontology/bibo/issn"
    )
    private String issn;

    @FieldType(type = "string")
    @FieldSource(
        template = "collection/eissn",
        predicate = "http://purl.org/ontology/bibo/eissn"
    )
    private String eissn;

    @FieldType(type = "string")
    @FieldSource(
        template = "collection/doi",
        predicate = "http://purl.org/ontology/bibo/doi"
    )
    private String doi;

    @FieldType(type = "string")
    @FieldSource(
        template = "collection/oclcnum",
        predicate = "http://purl.org/ontology/bibo/oclcnum"
    )
    private String oclcnum;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "collection/isAbout",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> isAbout;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "collection/specifiedOutputOf",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> specifiedOutputOf;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "collection/mention",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> mentions;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "collection/participatesIn",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> participatesIn;

    @NestedObject
    @FieldType(type = "strings", indexed = false)
    @FieldSource(
        template = "collection/supportedBy",
        predicate = "http://www.w3.org/2000/01/rdf-schema#label"
    )
    private List<String> supportedBy;

    public Collection() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public List<String> getPublicationVenueFor() {
        return publicationVenueFor;
    }

    public void setPublicationVenueFor(List<String> publicationVenueFor) {
        this.publicationVenueFor = publicationVenueFor;
    }

    public List<String> getEditors() {
        return editors;
    }

    public void setEditors(List<String> editors) {
        this.editors = editors;
    }

    public List<String> getTranslators() {
        return translators;
    }

    public void setTranslators(List<String> translators) {
        this.translators = translators;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublisherType() {
        return publisherType;
    }

    public void setPublisherType(String publisherType) {
        this.publisherType = publisherType;
    }

    public List<String> getSubjectAreas() {
        return subjectAreas;
    }

    public void setSubjectAreas(List<String> subjectAreas) {
        this.subjectAreas = subjectAreas;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public List<String> getOutputOfProcessOrEvent() {
        return outputOfProcessOrEvent;
    }

    public void setOutputOfProcessOrEvent(List<String> outputOfProcessOrEvent) {
        this.outputOfProcessOrEvent = outputOfProcessOrEvent;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getEissn() {
        return eissn;
    }

    public void setEissn(String eissn) {
        this.eissn = eissn;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public String getOclcnum() {
        return oclcnum;
    }

    public void setOclcnum(String oclcnum) {
        this.oclcnum = oclcnum;
    }

    public List<String> getIsAbout() {
        return isAbout;
    }

    public void setIsAbout(List<String> isAbout) {
        this.isAbout = isAbout;
    }

    public List<String> getSpecifiedOutputOf() {
        return specifiedOutputOf;
    }

    public void setSpecifiedOutputOf(List<String> specifiedOutputOf) {
        this.specifiedOutputOf = specifiedOutputOf;
    }

    public List<String> getMentions() {
        return mentions;
    }

    public void setMentions(List<String> mentions) {
        this.mentions = mentions;
    }

    public List<String> getParticipatesIn() {
        return participatesIn;
    }

    public void setParticipatesIn(List<String> participatesIn) {
        this.participatesIn = participatesIn;
    }

    public List<String> getSupportedBy() {
        return supportedBy;
    }

    public void setSupportedBy(List<String> supportedBy) {
        this.supportedBy = supportedBy;
    }

}
