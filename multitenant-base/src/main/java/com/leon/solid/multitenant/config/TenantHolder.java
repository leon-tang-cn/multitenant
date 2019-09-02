package com.leon.solid.multitenant.config;

/**
 * Thread local storage of the tenant name. This is the only place, where the tenant name is available across all calls
 * and beans.
 *
 * @author Leon.Tang
 * @date Aug 28, 2019
 */
public class TenantHolder {

    private static final InheritableThreadLocal<String> CURRENT_TENANT_NAME = new InheritableThreadLocal<>();

    public static String getCurrentTenant() {
        return CURRENT_TENANT_NAME.get();
    }

    public static void setTenant(final String tenantName) {
        CURRENT_TENANT_NAME.set(tenantName);
    }

    public static void cleanupTenant() {
        CURRENT_TENANT_NAME.remove();
    }
}
