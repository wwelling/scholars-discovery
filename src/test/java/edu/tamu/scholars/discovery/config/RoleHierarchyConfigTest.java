package edu.tamu.scholars.discovery.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import edu.tamu.scholars.discovery.auth.model.Role;

@ExtendWith(MockitoExtension.class)
class RoleHierarchyConfigTest {

    @InjectMocks
    RoleHierarchyConfig roleHierarchyConfig;

    @Test
    void testRoleSuperAdminHierarchy() {
        List<GrantedAuthority> assignableAuthorities = assignableAuthorities(Role.ROLE_SUPER_ADMIN);
        List<? extends GrantedAuthority> reachableAuthorities = reachableAuthorities(assignableAuthorities);
        assertEquals(4, reachableAuthorities.size());
        assertEquals("ROLE_USER", reachableAuthorities.get(0).getAuthority());
        assertEquals("ROLE_MANAGER", reachableAuthorities.get(1).getAuthority());
        assertEquals("ROLE_ADMIN", reachableAuthorities.get(2).getAuthority());
        assertEquals("ROLE_SUPER_ADMIN", reachableAuthorities.get(3).getAuthority());
    }

    @Test
    void testRoleAdminHierarchy() {
        List<GrantedAuthority> assignableAuthorities = assignableAuthorities(Role.ROLE_ADMIN);
        List<? extends GrantedAuthority> reachableAuthorities = reachableAuthorities(assignableAuthorities);
        assertEquals(3, reachableAuthorities.size());
        assertEquals("ROLE_USER", reachableAuthorities.get(0).getAuthority());
        assertEquals("ROLE_MANAGER", reachableAuthorities.get(1).getAuthority());
        assertEquals("ROLE_ADMIN", reachableAuthorities.get(2).getAuthority());
    }

    @Test
    void testRoleManagerHierarchy() {
        List<GrantedAuthority> assignableAuthorities = assignableAuthorities(Role.ROLE_MANAGER);
        List<? extends GrantedAuthority> reachableAuthorities = reachableAuthorities(assignableAuthorities);
        assertEquals(2, reachableAuthorities.size());
        assertEquals("ROLE_USER", reachableAuthorities.get(0).getAuthority());
        assertEquals("ROLE_MANAGER", reachableAuthorities.get(1).getAuthority());
    }

    @Test
    void testRoleUserHierarchy() {
        List<GrantedAuthority> assignableAuthorities = assignableAuthorities(Role.ROLE_USER);
        List<? extends GrantedAuthority> reachableAuthorities = reachableAuthorities(assignableAuthorities);
        assertEquals(1, reachableAuthorities.size());
        assertEquals("ROLE_USER", reachableAuthorities.get(0).getAuthority());
    }

    private List<GrantedAuthority> assignableAuthorities(Role role) {
        return Lists.list(new SimpleGrantedAuthority(role.toString()));
    }

    private List<? extends GrantedAuthority> reachableAuthorities(List<GrantedAuthority> assignableAuthorities) {
        RoleHierarchy roleHierarchy = roleHierarchyConfig.roleHierarchy();
        return new ArrayList<GrantedAuthority>(roleHierarchy.getReachableGrantedAuthorities(assignableAuthorities));
    }

}
