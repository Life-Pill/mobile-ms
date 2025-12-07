package com.lifepill.identityservice.util;

import com.lifepill.identityservice.entity.enums.Role;
import lombok.experimental.UtilityClass;

/**
 * Utility class for validating role hierarchy.
 * Hierarchy: OWNER > MANAGER > CASHIER > OTHER
 */
@UtilityClass
public class HierarchyValidator {
    
    /**
     * Checks if requester can update target employee.
     * 
     * @param requesterRole Role of the person making the request
     * @param targetRole Role of the employee being updated
     * @param requesterId ID of requester
     * @param targetId ID of target employee
     * @return true if update is allowed
     */
    public static boolean canUpdate(Role requesterRole, Role targetRole, Long requesterId, Long targetId) {
        // If updating self, always allowed
        if (requesterId.equals(targetId)) {
            return true;
        }
        
        // Apply hierarchy rules
        return switch (requesterRole) {
            case OWNER -> true; // OWNER can update anyone
            case MANAGER -> targetRole == Role.CASHIER || targetRole == Role.OTHER; // MANAGER can update CASHIER and OTHER
            case CASHIER, OTHER -> false; // CASHIER and OTHER can only update themselves (handled above)
            default -> false; // Any other roles cannot update others
        };
    }
    
    /**
     * Checks if requester can view target employee's sensitive data.
     *
     * @param requesterRole Role of the person making the request
     * @param targetRole Role of the employee being viewed
     * @param requesterId ID of requester
     * @param targetId ID of target employee
     * @return true if viewing is allowed
     */
    public static boolean canView(Role requesterRole, Role targetRole, Long requesterId, Long targetId) {
        // Same logic as canUpdate for now
        return canUpdate(requesterRole, targetRole, requesterId, targetId);
    }
    
    /**
     * Gets a human-readable error message for hierarchy violations.
     *
     * @param requesterRole Role of requester
     * @param targetRole Role of target
     * @return Error message
     */
    public static String getHierarchyViolationMessage(Role requesterRole, Role targetRole) {
        if (requesterRole == Role.CASHIER || requesterRole == Role.OTHER) {
            return "You can only update your own password/PIN";
        }
        if (requesterRole == Role.MANAGER) {
            return "Managers can only update passwords/PINs for CASHIER and OTHER roles";
        }
        return "Access denied due to role hierarchy";
    }
}
