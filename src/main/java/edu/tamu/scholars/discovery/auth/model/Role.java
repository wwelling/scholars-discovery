package edu.tamu.scholars.discovery.auth.model;

public enum Role {

    // NOTE: inconsistent use of enum key
    // SecurityFilterChainConfig @PreAuthorize
    // `.hasRole("USER")` `@PreAuthorize("hasRole('ROLE_USER')")`
    // `.hasRole("MANAGER")` `@PreAuthorize("hasRole('ROLE_MANAGER')")`
    // `.hasRole("ADMIN")` `@PreAuthorize("hasRole('ROLE_ADMIN')")`
    // `.hasRole("SUPER_ADMIN")` `@PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")`

    ROLE_USER("USER"),
    ROLE_MANAGER("MANAGER"),
    ROLE_ADMIN("ADMIN"),
    ROLE_SUPER_ADMIN("SUPER_ADMIN");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
