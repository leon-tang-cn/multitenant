package com.leon.solid.multitenant.config;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The Class MultiTenantProperties.
 *
 * @author Leon.Tang
 * @date Aug 28, 2019
 */
@ConfigurationProperties(prefix = MultiTenantProperties.MULTITENANT_PREFIX)
public class MultiTenantProperties {

    public final static String MULTITENANT_PREFIX = "multitenant";

    private String[] entityScanPackages;

    private List<DataSourceProperties> dataSourcesProps;

    public List<DataSourceProperties> getDataSources() {
        return this.dataSourcesProps;
    }

    public void setDataSources(List<DataSourceProperties> dataSourcesProps) {
        this.dataSourcesProps = dataSourcesProps;
    }

    public String[] getEntityScanPackages() {
        return entityScanPackages;
    }

    public void setEntityScanPackages(String[] entityScanPackages) {
        this.entityScanPackages = entityScanPackages;
    }

    /**
     * The Class DataSourceProperties.
     *
     * @author YMSLX
     * @date Aug 28, 2019
     */
    public static class DataSourceProperties extends org.springframework.boot.autoconfigure.jdbc.DataSourceProperties {

        private String tenantId;

        private boolean defaultTenant;

        public String getTenantId() {
            return tenantId;
        }

        public void setTenantId(String tenantId) {
            this.tenantId = tenantId;
        }

        public boolean isDefaultTenant() {
            return defaultTenant;
        }

        public void setDefaultTenant(boolean defaultTenant) {
            this.defaultTenant = defaultTenant;
        }
    }
}
