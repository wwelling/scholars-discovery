package edu.tamu.scholars.discovery.theme.model;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

import edu.tamu.scholars.discovery.model.Named;

/**
 * 
 */
@Entity
@Table(name = "themes")
public class Theme extends Named {

    private static final long serialVersionUID = 3711737239238294248L;

    @Column(nullable = false)
    private String organization;

    @Column(nullable = false)
    private String organizationId;

    @Column(nullable = false)
    private boolean active;

    @Embedded
    private Home home;

    @Embedded
    private Header header;

    @Embedded
    private Footer footer;

    @ElementCollection
    private List<Style> colors;

    @ElementCollection
    private List<Style> variants;

    @ElementCollection
    private List<Style> variables;

    public Theme() {
        super();
        this.active = false;
        this.home = new Home();
        this.header = new Header();
        this.footer = new Footer();
        this.colors = new ArrayList<Style>();
        this.variants = new ArrayList<Style>();
        this.variables = new ArrayList<Style>();
    }

    public Theme(String name, String organization, String organizationId) {
        this();
        setName(name);
        this.organization = organization;
        this.organizationId = organizationId;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Footer getFooter() {
        return footer;
    }

    public void setFooter(Footer footer) {
        this.footer = footer;
    }

    public List<Style> getColors() {
        return colors;
    }

    public void setColors(List<Style> colors) {
        this.colors = colors;
    }

    public List<Style> getVariants() {
        return variants;
    }

    public void setVariants(List<Style> variants) {
        this.variants = variants;
    }

    public List<Style> getVariables() {
        return variables;
    }

    public void setVariables(List<Style> variables) {
        this.variables = variables;
    }

}
