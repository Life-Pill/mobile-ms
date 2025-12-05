package com.lifepill.identityservice.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lifepill.identityservice.entity.enums.Permission.*;

/**
 * Enumeration representing employer roles in the system with permission hierarchy.
 * Role Hierarchy: OWNER > MANAGER > CASHIER > USER > OTHER
 */
@RequiredArgsConstructor
public enum Role {
    
    /**
     * Owner role - Full access to all endpoints and operations across all branches.
     */
    OWNER(
            Set.of(
                    OWNER_READ, OWNER_CREATE, OWNER_UPDATE, OWNER_DELETE,
                    MANAGER_READ, MANAGER_CREATE, MANAGER_UPDATE, MANAGER_DELETE,
                    CASHIER_READ, CASHIER_CREATE, CASHIER_UPDATE, CASHIER_DELETE,
                    USER_READ, USER_CREATE, USER_UPDATE, USER_DELETE,
                    OTHER_READ, OTHER_CREATE
            )
    ),

    /**
     * Manager role - Branch-level access, can manage own branch employees and inventory.
     */
    MANAGER(
            Set.of(
                    MANAGER_READ, MANAGER_CREATE, MANAGER_UPDATE, MANAGER_DELETE,
                    CASHIER_READ, CASHIER_CREATE, CASHIER_UPDATE, CASHIER_DELETE,
                    USER_READ, USER_CREATE, USER_UPDATE, USER_DELETE,
                    OTHER_READ, OTHER_CREATE
            )
    ),

    /**
     * Cashier role - POS operations, can read items/inventory and create orders.
     */
    CASHIER(
            Set.of(
                    CASHIER_READ, CASHIER_CREATE, CASHIER_UPDATE, CASHIER_DELETE,
                    USER_READ, USER_CREATE,
                    OTHER_READ, OTHER_CREATE
            )
    ),

    /**
     * User role - Mobile app users who can search medicines, place orders, view profile.
     */
    USER(
            Set.of(
                    USER_READ, USER_CREATE, USER_UPDATE, USER_DELETE,
                    OTHER_READ, OTHER_CREATE
            )
    ),

    /**
     * Other role - Minimal access for basic read operations.
     */
    OTHER(
            Set.of(
                    OTHER_READ, OTHER_CREATE
            )
    );

    @Getter
    private final Set<Permission> permissions;

    /**
     * Gets the Spring Security authorities for this role.
     * Includes both the role authority (ROLE_OWNER, etc.) and individual permission authorities.
     *
     * @return List of granted authorities
     */
    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
