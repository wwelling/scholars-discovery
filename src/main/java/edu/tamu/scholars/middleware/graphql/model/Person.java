package edu.tamu.scholars.middleware.graphql.model;

import edu.tamu.scholars.middleware.graphql.model.Relationship;
import edu.tamu.scholars.middleware.graphql.model.person.ResearchArea;
import edu.tamu.scholars.middleware.graphql.model.person.GeographicFocus;
import edu.tamu.scholars.middleware.graphql.model.person.HeadOf;
import edu.tamu.scholars.middleware.graphql.model.person.MemberOf;
import edu.tamu.scholars.middleware.graphql.model.person.HasCollaborator;
import edu.tamu.scholars.middleware.graphql.model.person.ClinicalActivity;
import edu.tamu.scholars.middleware.graphql.model.person.AttendedEvent;
import edu.tamu.scholars.middleware.graphql.model.person.EducationAndTraining;
import edu.tamu.scholars.middleware.graphql.model.person.Credential;
import edu.tamu.scholars.middleware.graphql.model.person.CredentialEligibilityAttained;
import edu.tamu.scholars.middleware.graphql.model.person.AwardsAndHonors;
import edu.tamu.scholars.middleware.graphql.model.person.AdviseeOf;
import edu.tamu.scholars.middleware.graphql.model.Document;
import edu.tamu.scholars.middleware.graphql.model.person.CollectionOrSeriesEditorFor;
import edu.tamu.scholars.middleware.graphql.model.person.EditorOf;
import edu.tamu.scholars.middleware.graphql.model.person.Presentation;
import edu.tamu.scholars.middleware.graphql.model.person.FeaturedIn;
import edu.tamu.scholars.middleware.graphql.model.person.AssigneeForPatent;
import edu.tamu.scholars.middleware.graphql.model.person.TranslatorOf;
import edu.tamu.scholars.middleware.graphql.model.person.ResearcherOn;
import edu.tamu.scholars.middleware.graphql.model.person.OtherResearchActivity;
import edu.tamu.scholars.middleware.graphql.model.person.TeachingActivity;
import edu.tamu.scholars.middleware.graphql.model.person.Advisee;
import edu.tamu.scholars.middleware.graphql.model.person.ReviewerOf;
import edu.tamu.scholars.middleware.graphql.model.person.ContactOrProvidorForService;
import edu.tamu.scholars.middleware.graphql.model.person.OrganizerOfEvent;
import edu.tamu.scholars.middleware.graphql.model.person.ProfessionalServiceActivity;
import edu.tamu.scholars.middleware.graphql.model.person.OutreachAndCommunityServiceActivity;
import edu.tamu.scholars.middleware.graphql.model.person.PerformsTechnique;
import edu.tamu.scholars.middleware.graphql.model.person.HasExpertiseInTechnique;
import edu.tamu.scholars.middleware.graphql.model.person.EtdChairOf;
import edu.tamu.scholars.middleware.graphql.model.person.InTheNew;
import edu.tamu.scholars.middleware.graphql.model.person.FutureResearchIdea;
import edu.tamu.scholars.middleware.graphql.model.common.Website;
import edu.tamu.scholars.middleware.graphql.model.common.SameAs;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.leangen.graphql.annotations.types.GraphQLType;
import java.lang.String;
import java.util.List;

/**
 * This file is automatically generated on compile.
 *
 * Do not modify this file -- YOUR CHANGES WILL BE ERASED!
 */
@GraphQLType(
    name = "Person"
)
@JsonInclude(NON_EMPTY)
public class Person extends AbstractNestedDocument {
  private static final long serialVersionUID = -3444805L;

  private List<Relationship> positions;

  private List<ResearchArea> researchAreas;

  private List<GeographicFocus> geographicFocus;

  private List<HeadOf> headOf;

  private List<MemberOf> memberOf;

  private List<HasCollaborator> hasCollaborator;

  private List<ClinicalActivity> clinicalActivities;

  private List<AttendedEvent> attendedEvents;

  private List<EducationAndTraining> educationAndTraining;

  private List<Credential> credentials;

  private List<CredentialEligibilityAttained> credentialEligibilityAttained;

  private List<AwardsAndHonors> awardsAndHonors;

  private List<AdviseeOf> adviseeOf;

  private List<Document> publications;

  private List<CollectionOrSeriesEditorFor> collectionOrSeriesEditorFor;

  private List<EditorOf> editorOf;

  private List<Presentation> presentations;

  private List<FeaturedIn> featuredIn;

  private List<AssigneeForPatent> assigneeForPatent;

  private List<TranslatorOf> translatorOf;

  private List<ResearcherOn> researcherOn;

  private List<OtherResearchActivity> otherResearchActivities;

  private List<TeachingActivity> teachingActivities;

  private List<Advisee> advisee;

  private List<ReviewerOf> reviewerOf;

  private List<ContactOrProvidorForService> contactOrProvidorForService;

  private List<OrganizerOfEvent> organizerOfEvent;

  private List<ProfessionalServiceActivity> professionalServiceActivities;

  private List<OutreachAndCommunityServiceActivity> outreachAndCommunityServiceActivities;

  private List<PerformsTechnique> performsTechnique;

  private List<HasExpertiseInTechnique> hasExpertiseInTechnique;

  private List<EtdChairOf> etdChairOf;

  private List<InTheNew> inTheNews;

  private List<FutureResearchIdea> futureResearchIdeas;

  private List<Website> websites;

  private List<SameAs> sameAs;

  private String name;

  private String primaryEmail;

  private List<String> additionalEmails;

  private String phone;

  private String orcidId;

  private String preferredTitle;

  private String overview;

  private String hrJobTitle;

  private List<String> keywords;

  private String researchOverview;

  private List<String> principalInvestigatorOn;

  private List<String> coPrincipalInvestigatorOn;

  private String teachingOverview;

  private String outreachOverview;

  private String isni;

  private String netid;

  private String researcherId;

  private String twitter;

  private String uid;

  private String uin;

  private String youtube;

  private String eraCommonsId;

  private String isiResearcherId;

  private String scopusId;

  private String healthCareProviderId;

  private String email;

  private String firstName;

  private String middleName;

  private String lastName;

  private String streetAddress;

  private String locality;

  private String region;

  private String postalCode;

  private String country;

  private String geographicLocation;

  private List<String> locatedInFacility;

  private String fax;

  private String featuredProfileDisplay;

  private List<String> organizations;

  private List<String> schools;

  private List<String> type;

  private String image;

  private String thumbnail;

  private String modTime;

  @JsonProperty("class")
  private String clazz;

  public Person() {
    super();
  }

  public List<Relationship> getPositions() {
    return positions;
  }

  public void setPositions(List<Relationship> positions) {
    this.positions = positions;
  }

  public List<ResearchArea> getResearchAreas() {
    return researchAreas;
  }

  public void setResearchAreas(List<ResearchArea> researchAreas) {
    this.researchAreas = researchAreas;
  }

  public List<GeographicFocus> getGeographicFocus() {
    return geographicFocus;
  }

  public void setGeographicFocus(List<GeographicFocus> geographicFocus) {
    this.geographicFocus = geographicFocus;
  }

  public List<HeadOf> getHeadOf() {
    return headOf;
  }

  public void setHeadOf(List<HeadOf> headOf) {
    this.headOf = headOf;
  }

  public List<MemberOf> getMemberOf() {
    return memberOf;
  }

  public void setMemberOf(List<MemberOf> memberOf) {
    this.memberOf = memberOf;
  }

  public List<HasCollaborator> getHasCollaborator() {
    return hasCollaborator;
  }

  public void setHasCollaborator(List<HasCollaborator> hasCollaborator) {
    this.hasCollaborator = hasCollaborator;
  }

  public List<ClinicalActivity> getClinicalActivities() {
    return clinicalActivities;
  }

  public void setClinicalActivities(List<ClinicalActivity> clinicalActivities) {
    this.clinicalActivities = clinicalActivities;
  }

  public List<AttendedEvent> getAttendedEvents() {
    return attendedEvents;
  }

  public void setAttendedEvents(List<AttendedEvent> attendedEvents) {
    this.attendedEvents = attendedEvents;
  }

  public List<EducationAndTraining> getEducationAndTraining() {
    return educationAndTraining;
  }

  public void setEducationAndTraining(List<EducationAndTraining> educationAndTraining) {
    this.educationAndTraining = educationAndTraining;
  }

  public List<Credential> getCredentials() {
    return credentials;
  }

  public void setCredentials(List<Credential> credentials) {
    this.credentials = credentials;
  }

  public List<CredentialEligibilityAttained> getCredentialEligibilityAttained() {
    return credentialEligibilityAttained;
  }

  public void setCredentialEligibilityAttained(
      List<CredentialEligibilityAttained> credentialEligibilityAttained) {
    this.credentialEligibilityAttained = credentialEligibilityAttained;
  }

  public List<AwardsAndHonors> getAwardsAndHonors() {
    return awardsAndHonors;
  }

  public void setAwardsAndHonors(List<AwardsAndHonors> awardsAndHonors) {
    this.awardsAndHonors = awardsAndHonors;
  }

  public List<AdviseeOf> getAdviseeOf() {
    return adviseeOf;
  }

  public void setAdviseeOf(List<AdviseeOf> adviseeOf) {
    this.adviseeOf = adviseeOf;
  }

  public List<Document> getPublications() {
    return publications;
  }

  public void setPublications(List<Document> publications) {
    this.publications = publications;
  }

  public List<CollectionOrSeriesEditorFor> getCollectionOrSeriesEditorFor() {
    return collectionOrSeriesEditorFor;
  }

  public void setCollectionOrSeriesEditorFor(
      List<CollectionOrSeriesEditorFor> collectionOrSeriesEditorFor) {
    this.collectionOrSeriesEditorFor = collectionOrSeriesEditorFor;
  }

  public List<EditorOf> getEditorOf() {
    return editorOf;
  }

  public void setEditorOf(List<EditorOf> editorOf) {
    this.editorOf = editorOf;
  }

  public List<Presentation> getPresentations() {
    return presentations;
  }

  public void setPresentations(List<Presentation> presentations) {
    this.presentations = presentations;
  }

  public List<FeaturedIn> getFeaturedIn() {
    return featuredIn;
  }

  public void setFeaturedIn(List<FeaturedIn> featuredIn) {
    this.featuredIn = featuredIn;
  }

  public List<AssigneeForPatent> getAssigneeForPatent() {
    return assigneeForPatent;
  }

  public void setAssigneeForPatent(List<AssigneeForPatent> assigneeForPatent) {
    this.assigneeForPatent = assigneeForPatent;
  }

  public List<TranslatorOf> getTranslatorOf() {
    return translatorOf;
  }

  public void setTranslatorOf(List<TranslatorOf> translatorOf) {
    this.translatorOf = translatorOf;
  }

  public List<ResearcherOn> getResearcherOn() {
    return researcherOn;
  }

  public void setResearcherOn(List<ResearcherOn> researcherOn) {
    this.researcherOn = researcherOn;
  }

  public List<OtherResearchActivity> getOtherResearchActivities() {
    return otherResearchActivities;
  }

  public void setOtherResearchActivities(List<OtherResearchActivity> otherResearchActivities) {
    this.otherResearchActivities = otherResearchActivities;
  }

  public List<TeachingActivity> getTeachingActivities() {
    return teachingActivities;
  }

  public void setTeachingActivities(List<TeachingActivity> teachingActivities) {
    this.teachingActivities = teachingActivities;
  }

  public List<Advisee> getAdvisee() {
    return advisee;
  }

  public void setAdvisee(List<Advisee> advisee) {
    this.advisee = advisee;
  }

  public List<ReviewerOf> getReviewerOf() {
    return reviewerOf;
  }

  public void setReviewerOf(List<ReviewerOf> reviewerOf) {
    this.reviewerOf = reviewerOf;
  }

  public List<ContactOrProvidorForService> getContactOrProvidorForService() {
    return contactOrProvidorForService;
  }

  public void setContactOrProvidorForService(
      List<ContactOrProvidorForService> contactOrProvidorForService) {
    this.contactOrProvidorForService = contactOrProvidorForService;
  }

  public List<OrganizerOfEvent> getOrganizerOfEvent() {
    return organizerOfEvent;
  }

  public void setOrganizerOfEvent(List<OrganizerOfEvent> organizerOfEvent) {
    this.organizerOfEvent = organizerOfEvent;
  }

  public List<ProfessionalServiceActivity> getProfessionalServiceActivities() {
    return professionalServiceActivities;
  }

  public void setProfessionalServiceActivities(
      List<ProfessionalServiceActivity> professionalServiceActivities) {
    this.professionalServiceActivities = professionalServiceActivities;
  }

  public List<OutreachAndCommunityServiceActivity> getOutreachAndCommunityServiceActivities() {
    return outreachAndCommunityServiceActivities;
  }

  public void setOutreachAndCommunityServiceActivities(
      List<OutreachAndCommunityServiceActivity> outreachAndCommunityServiceActivities) {
    this.outreachAndCommunityServiceActivities = outreachAndCommunityServiceActivities;
  }

  public List<PerformsTechnique> getPerformsTechnique() {
    return performsTechnique;
  }

  public void setPerformsTechnique(List<PerformsTechnique> performsTechnique) {
    this.performsTechnique = performsTechnique;
  }

  public List<HasExpertiseInTechnique> getHasExpertiseInTechnique() {
    return hasExpertiseInTechnique;
  }

  public void setHasExpertiseInTechnique(List<HasExpertiseInTechnique> hasExpertiseInTechnique) {
    this.hasExpertiseInTechnique = hasExpertiseInTechnique;
  }

  public List<EtdChairOf> getEtdChairOf() {
    return etdChairOf;
  }

  public void setEtdChairOf(List<EtdChairOf> etdChairOf) {
    this.etdChairOf = etdChairOf;
  }

  public List<InTheNew> getInTheNews() {
    return inTheNews;
  }

  public void setInTheNews(List<InTheNew> inTheNews) {
    this.inTheNews = inTheNews;
  }

  public List<FutureResearchIdea> getFutureResearchIdeas() {
    return futureResearchIdeas;
  }

  public void setFutureResearchIdeas(List<FutureResearchIdea> futureResearchIdeas) {
    this.futureResearchIdeas = futureResearchIdeas;
  }

  public List<Website> getWebsites() {
    return websites;
  }

  public void setWebsites(List<Website> websites) {
    this.websites = websites;
  }

  public List<SameAs> getSameAs() {
    return sameAs;
  }

  public void setSameAs(List<SameAs> sameAs) {
    this.sameAs = sameAs;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPrimaryEmail() {
    return primaryEmail;
  }

  public void setPrimaryEmail(String primaryEmail) {
    this.primaryEmail = primaryEmail;
  }

  public List<String> getAdditionalEmails() {
    return additionalEmails;
  }

  public void setAdditionalEmails(List<String> additionalEmails) {
    this.additionalEmails = additionalEmails;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getOrcidId() {
    return orcidId;
  }

  public void setOrcidId(String orcidId) {
    this.orcidId = orcidId;
  }

  public String getPreferredTitle() {
    return preferredTitle;
  }

  public void setPreferredTitle(String preferredTitle) {
    this.preferredTitle = preferredTitle;
  }

  public String getOverview() {
    return overview;
  }

  public void setOverview(String overview) {
    this.overview = overview;
  }

  public String getHrJobTitle() {
    return hrJobTitle;
  }

  public void setHrJobTitle(String hrJobTitle) {
    this.hrJobTitle = hrJobTitle;
  }

  public List<String> getKeywords() {
    return keywords;
  }

  public void setKeywords(List<String> keywords) {
    this.keywords = keywords;
  }

  public String getResearchOverview() {
    return researchOverview;
  }

  public void setResearchOverview(String researchOverview) {
    this.researchOverview = researchOverview;
  }

  public List<String> getPrincipalInvestigatorOn() {
    return principalInvestigatorOn;
  }

  public void setPrincipalInvestigatorOn(List<String> principalInvestigatorOn) {
    this.principalInvestigatorOn = principalInvestigatorOn;
  }

  public List<String> getCoPrincipalInvestigatorOn() {
    return coPrincipalInvestigatorOn;
  }

  public void setCoPrincipalInvestigatorOn(List<String> coPrincipalInvestigatorOn) {
    this.coPrincipalInvestigatorOn = coPrincipalInvestigatorOn;
  }

  public String getTeachingOverview() {
    return teachingOverview;
  }

  public void setTeachingOverview(String teachingOverview) {
    this.teachingOverview = teachingOverview;
  }

  public String getOutreachOverview() {
    return outreachOverview;
  }

  public void setOutreachOverview(String outreachOverview) {
    this.outreachOverview = outreachOverview;
  }

  public String getIsni() {
    return isni;
  }

  public void setIsni(String isni) {
    this.isni = isni;
  }

  public String getNetid() {
    return netid;
  }

  public void setNetid(String netid) {
    this.netid = netid;
  }

  public String getResearcherId() {
    return researcherId;
  }

  public void setResearcherId(String researcherId) {
    this.researcherId = researcherId;
  }

  public String getTwitter() {
    return twitter;
  }

  public void setTwitter(String twitter) {
    this.twitter = twitter;
  }

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public String getUin() {
    return uin;
  }

  public void setUin(String uin) {
    this.uin = uin;
  }

  public String getYoutube() {
    return youtube;
  }

  public void setYoutube(String youtube) {
    this.youtube = youtube;
  }

  public String getEraCommonsId() {
    return eraCommonsId;
  }

  public void setEraCommonsId(String eraCommonsId) {
    this.eraCommonsId = eraCommonsId;
  }

  public String getIsiResearcherId() {
    return isiResearcherId;
  }

  public void setIsiResearcherId(String isiResearcherId) {
    this.isiResearcherId = isiResearcherId;
  }

  public String getScopusId() {
    return scopusId;
  }

  public void setScopusId(String scopusId) {
    this.scopusId = scopusId;
  }

  public String getHealthCareProviderId() {
    return healthCareProviderId;
  }

  public void setHealthCareProviderId(String healthCareProviderId) {
    this.healthCareProviderId = healthCareProviderId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
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

  public List<String> getLocatedInFacility() {
    return locatedInFacility;
  }

  public void setLocatedInFacility(List<String> locatedInFacility) {
    this.locatedInFacility = locatedInFacility;
  }

  public String getFax() {
    return fax;
  }

  public void setFax(String fax) {
    this.fax = fax;
  }

  public String getFeaturedProfileDisplay() {
    return featuredProfileDisplay;
  }

  public void setFeaturedProfileDisplay(String featuredProfileDisplay) {
    this.featuredProfileDisplay = featuredProfileDisplay;
  }

  public List<String> getOrganizations() {
    return organizations;
  }

  public void setOrganizations(List<String> organizations) {
    this.organizations = organizations;
  }

  public List<String> getSchools() {
    return schools;
  }

  public void setSchools(List<String> schools) {
    this.schools = schools;
  }

  public List<String> getType() {
    return type;
  }

  public void setType(List<String> type) {
    this.type = type;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getThumbnail() {
    return thumbnail;
  }

  public void setThumbnail(String thumbnail) {
    this.thumbnail = thumbnail;
  }

  public String getModTime() {
    return modTime;
  }

  public void setModTime(String modTime) {
    this.modTime = modTime;
  }

  public String getClazz() {
    return clazz;
  }

  public void setClazz(String clazz) {
    this.clazz = clazz;
  }
}
