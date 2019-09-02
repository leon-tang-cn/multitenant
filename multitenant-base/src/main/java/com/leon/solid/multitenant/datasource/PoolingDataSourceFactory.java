package com.leon.solid.multitenant.datasource;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jta.bitronix.PoolingDataSourceBean;

import com.leon.solid.multitenant.utils.PropertiesBuilder;
import com.leon.solid.multitenant.utils.StringUtils;

import bitronix.tm.resource.jdbc.PoolingDataSource;

/**
 * This is a support class for building Bitronix Transaction Manager compatible data sources easily.
 */
public class PoolingDataSourceFactory {
    private final int minPoolSize;
    private final int maxPoolSize;
    private static final Map<String, String> URL_PROPERTIES_NAMES;

    static {
        // Oracle XA responds to the fact that the property name receiving the connection URL is proprietary "URL" in upper case
        URL_PROPERTIES_NAMES = new HashMap<String, String>();
        URL_PROPERTIES_NAMES.put("oracle.jdbc.xa.client.OracleXADataSource", "URL");
    }

    /**
     * Creates a new instance of this class with the default pool size.
     */
    public PoolingDataSourceFactory() {
        this(10, 15);
    }

    /**
     * Create a new instance of this class, specifying the pool's maximum / minimum values.
     *
     * @param minPoolSize minimum of pool size
     * @param maxPoolSize maximum of pool size
     */
    public PoolingDataSourceFactory(final int minPoolSize, final int maxPoolSize) {
        this.minPoolSize = minPoolSize;
        this.maxPoolSize = maxPoolSize;
    }

    /**
     * Construct {@link PoolingDataSource} based on the setting of {@link DataSourceProperties} of Spring Boot.
     *
     * @param datasourceProperties {@link DataSourceProperties} of Spring Boot
     * @return Builded {@link PoolingDataSource}
     */
    public PoolingDataSourceBean build(final DataSourceProperties datasourceProperties) {
        final String dataSourceClassName = datasourceProperties.getXa().getDataSourceClassName();
        final PoolingDataSourceBean ds = new PoolingDataSourceBean();
        ds.setMinPoolSize(minPoolSize);
        ds.setMaxPoolSize(maxPoolSize);
        ds.setClassName(dataSourceClassName);

        final Map<String, String> xaProperties = datasourceProperties.getXa().getProperties();
        // Prefer datasource.xa.propperties if defined
        if (!xaProperties.isEmpty()) {
            ds.setDriverProperties(new PropertiesBuilder().putAll(xaProperties).build());
        } else {
            final String urlPropName = StringUtils.orDefault(URL_PROPERTIES_NAMES.get(dataSourceClassName), "url");
            ds.setDriverProperties(
                new PropertiesBuilder()
                .put("user", datasourceProperties.getUsername())
                .put("password", datasourceProperties.getPassword())
                .put(urlPropName, datasourceProperties.getUrl())
                .build());
        }
        return ds;
    }
}