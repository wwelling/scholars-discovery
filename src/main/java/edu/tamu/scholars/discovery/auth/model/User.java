package edu.tamu.scholars.discovery.auth.model;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;
import static edu.tamu.scholars.discovery.auth.AuthConstants.USERS_ACTIVE_COLUMN_NAME;
import static edu.tamu.scholars.discovery.auth.AuthConstants.USERS_CONFIRMED_COLUMN_NAME;
import static edu.tamu.scholars.discovery.auth.AuthConstants.USERS_CREATED_COLUMN_NAME;
import static edu.tamu.scholars.discovery.auth.AuthConstants.USERS_EMAIL_COLUMN_NAME;
import static edu.tamu.scholars.discovery.auth.AuthConstants.USERS_ENABLED_COLUMN_NAME;
import static edu.tamu.scholars.discovery.auth.AuthConstants.USERS_FIRST_NAME_COLUMN_NAME;
import static edu.tamu.scholars.discovery.auth.AuthConstants.USERS_ID_COLUMN_NAME;
import static edu.tamu.scholars.discovery.auth.AuthConstants.USERS_LAST_NAME_COLUMN_NAME;
import static edu.tamu.scholars.discovery.auth.AuthConstants.USERS_OLD_PASSWORDS_JOIN_COLUMN_NAME;
import static edu.tamu.scholars.discovery.auth.AuthConstants.USERS_OLD_PASSWORDS_TABLE_NAME;
import static edu.tamu.scholars.discovery.auth.AuthConstants.USERS_PASSWORD_COLUMN_NAME;
import static edu.tamu.scholars.discovery.auth.AuthConstants.USERS_ROLE_COLUMN_NAME;
import static edu.tamu.scholars.discovery.auth.AuthConstants.USERS_TABLE_NAME;
import static edu.tamu.scholars.discovery.auth.AuthConstants.USERS_TIMESTAMP_COLUMN_NAME;
import static edu.tamu.scholars.discovery.auth.AuthConstants.USER_EMAIL_INVALID_MESSAGE;
import static edu.tamu.scholars.discovery.auth.AuthConstants.USER_EMAIL_REQUIRED_MESSAGE;
import static edu.tamu.scholars.discovery.auth.AuthConstants.USER_FIRST_NAME_MAX_LENGTH;
import static edu.tamu.scholars.discovery.auth.AuthConstants.USER_FIRST_NAME_MIN_LENGTH;
import static edu.tamu.scholars.discovery.auth.AuthConstants.USER_FIRST_NAME_REQUIRED_MESSAGE;
import static edu.tamu.scholars.discovery.auth.AuthConstants.USER_FIRST_NAME_SIZE_MESSAGE;
import static edu.tamu.scholars.discovery.auth.AuthConstants.USER_LAST_NAME_MAX_LENGTH;
import static edu.tamu.scholars.discovery.auth.AuthConstants.USER_LAST_NAME_MIN_LENGTH;
import static edu.tamu.scholars.discovery.auth.AuthConstants.USER_LAST_NAME_REQUIRED_MESSAGE;
import static edu.tamu.scholars.discovery.auth.AuthConstants.USER_LAST_NAME_SIZE_MESSAGE;
import static edu.tamu.scholars.discovery.auth.AuthConstants.USER_ROLE_REQUIRED_MESSAGE;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.TemporalType.TIMESTAMP;
import static org.springframework.beans.BeanUtils.copyProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = USERS_TABLE_NAME)
public class User implements Serializable {

    private static final long serialVersionUID = -7535464109980348619L;

    @Id
    @JsonInclude(Include.NON_EMPTY)
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = USERS_ID_COLUMN_NAME)
    private Long id;

    @NotNull(message = USER_FIRST_NAME_REQUIRED_MESSAGE)
    @Size(
        max = USER_FIRST_NAME_MAX_LENGTH,
        min = USER_FIRST_NAME_MIN_LENGTH,
        message = USER_FIRST_NAME_SIZE_MESSAGE)
    @Column(
        name = USERS_FIRST_NAME_COLUMN_NAME,
        nullable = false)
    private String firstName;

    @NotNull(message = USER_LAST_NAME_REQUIRED_MESSAGE)
    @Size(
        max = USER_LAST_NAME_MAX_LENGTH,
        min = USER_LAST_NAME_MIN_LENGTH,
        message = USER_LAST_NAME_SIZE_MESSAGE)
    @Column(
        name = USERS_LAST_NAME_COLUMN_NAME,
        nullable = false)
    private String lastName;

    @NotNull(message = USER_EMAIL_REQUIRED_MESSAGE)
    @Email(message = USER_EMAIL_INVALID_MESSAGE)
    @Column(
        name = USERS_EMAIL_COLUMN_NAME,
        nullable = false,
        unique = true)
    private String email;

    @JsonProperty(access = WRITE_ONLY)
    @Column(name = USERS_PASSWORD_COLUMN_NAME)
    private String password;

    @JsonIgnore
    @ElementCollection(fetch = EAGER)
    @CollectionTable(
        name = USERS_OLD_PASSWORDS_TABLE_NAME,
        joinColumns = @JoinColumn(
            name = USERS_OLD_PASSWORDS_JOIN_COLUMN_NAME))
    private List<String> oldPasswords;

    @NotNull(message = USER_ROLE_REQUIRED_MESSAGE)
    @Enumerated(STRING)
    @Column(
        name = USERS_ROLE_COLUMN_NAME,
        nullable = false)
    private Role role;

    @JsonIgnore
    @CreationTimestamp
    @Temporal(TIMESTAMP)
    @Column(
        name = USERS_CREATED_COLUMN_NAME,
        nullable = false)
    private Calendar created;

    @JsonIgnore
    @UpdateTimestamp
    @Temporal(TIMESTAMP)
    @Column(
        name = USERS_TIMESTAMP_COLUMN_NAME,
        nullable = false)
    private Calendar timestamp;

    @JsonIgnore
    @Column(
        name = USERS_CONFIRMED_COLUMN_NAME,
        nullable = false)
    private boolean confirmed;

    @Column(
        name = USERS_ACTIVE_COLUMN_NAME,
        nullable = false)
    private boolean active;

    @Column(
        name = USERS_ENABLED_COLUMN_NAME,
        nullable = false)
    private boolean enabled;

    public User() {
        super();
        this.oldPasswords = new ArrayList<>();
        this.role = Role.ROLE_USER;
        this.created = Calendar.getInstance();
        this.timestamp = Calendar.getInstance();
        this.confirmed = false;
        this.active = true;
        this.enabled = false;
    }

    public User(String firstName, String lastName, String email) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public User(User user) {
        this();
        copyProperties(user, this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getOldPasswords() {
        return oldPasswords;
    }

    public void setOldPasswords(List<String> oldPasswords) {
        this.oldPasswords = oldPasswords;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Calendar getCreated() {
        return created;
    }

    public void setCreated(Calendar created) {
        this.created = created;
    }

    public Calendar getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Calendar timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}