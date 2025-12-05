package com.lifepill.identityservice.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration representing granular permissions for role-based access control.
 */
@Getter
@RequiredArgsConstructor
public enum Permission {

    // Owner permissions - full access
    OWNER_READ("owner:read"),
    OWNER_CREATE("owner:create"),
    OWNER_UPDATE("owner:update"),
    OWNER_DELETE("owner:delete"),

    // Manager permissions - branch level access
    MANAGER_READ("manager:read"),
    MANAGER_CREATE("manager:create"),
    MANAGER_UPDATE("manager:update"),
    MANAGER_DELETE("manager:delete"),

    // Cashier permissions - POS operations
    CASHIER_READ("cashier:read"),
    CASHIER_CREATE("cashier:create"),
    CASHIER_UPDATE("cashier:update"),
    CASHIER_DELETE("cashier:delete"),

    // User permissions - mobile app users
    USER_READ("user:read"),
    USER_CREATE("user:create"),
    USER_UPDATE("user:update"),
    USER_DELETE("user:delete"),

    // Other permissions - minimal access
    OTHER_READ("other:read"),
    OTHER_CREATE("other:create");

    private final String permission;
}
