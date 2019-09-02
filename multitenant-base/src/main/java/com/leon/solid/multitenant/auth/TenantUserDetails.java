package com.leon.solid.multitenant.auth;

/**
 * The Interface TenantUserDetails.
 *
 * @author Leon.Tang
 * @date Aug 28, 2019
 */
public interface TenantUserDetails {
    /**
     * Relation id getter interface
     * 
     * @return
     */
    String getRelationId();

    /**
     * Tenant id getter interface
     * 
     * @return
     */
    String getTenantId();
}
