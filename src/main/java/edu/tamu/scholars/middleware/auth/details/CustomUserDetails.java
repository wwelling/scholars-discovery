package edu.tamu.scholars.middleware.auth.details;

import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import edu.tamu.scholars.middleware.auth.config.PasswordConfig;
import edu.tamu.scholars.middleware.auth.model.User;

public class CustomUserDetails extends User implements UserDetails {

    private static final long serialVersionUID = 6674712962625174202L;

    private final transient PasswordConfig passwordConfig;

    public CustomUserDetails(User user, PasswordConfig passwordConfig) {
        super(user);
        this.passwordConfig = passwordConfig;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(getRole().toString());
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return isActive();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return isEnabled() && isConfirmed();
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return ChronoUnit.DAYS.between(
            getTimestamp().toInstant(),
            Calendar.getInstance().toInstant()
        ) < passwordConfig.getDuration();
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return getEmail();
    }

}
