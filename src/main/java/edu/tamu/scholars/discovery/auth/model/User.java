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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = USERS_TABLE_NAME)
public class User implements Serializable {

    private static final long serialVersionUID = 987654321012345678L;

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
    private List<String> oldPasswords = new ArrayList<>();

    @NotNull(message = USER_ROLE_REQUIRED_MESSAGE)
    @Enumerated(STRING)
    @Column(
        name = USERS_ROLE_COLUMN_NAME,
        nullable = false)
    private Role role = Role.ROLE_USER;

    @JsonIgnore
    @CreationTimestamp
    @Temporal(TIMESTAMP)
    @Column(
        name = USERS_CREATED_COLUMN_NAME,
        nullable = false)
    private Calendar created = Calendar.getInstance();

    @JsonIgnore
    @UpdateTimestamp
    @Temporal(TIMESTAMP)
    @Column(
        name = USERS_TIMESTAMP_COLUMN_NAME,
        nullable = false)
    private Calendar timestamp = Calendar.getInstance();

    @JsonIgnore
    @Column(
        name = USERS_CONFIRMED_COLUMN_NAME,
        nullable = false)
    private boolean confirmed = false;

    @Column(
        name = USERS_ACTIVE_COLUMN_NAME,
        nullable = false)
    private boolean active = true;

    @Column(
        name = USERS_ENABLED_COLUMN_NAME,
        nullable = false)
    private boolean enabled = false;

    public User(User user) {
        this();
        copyProperties(user, this);
    }

}