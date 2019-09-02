package com.leon.solid.multitenant.config;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.leon.solid.multitenant.config.MultiTenantProperties.DataSourceProperties;
import com.leon.solid.multitenant.utils.StringUtils;

/**
 * The Class MultiTenantJpaConfiguration.
 *
 * @author Leon.Tang
 * @date Aug 28, 2019
 */
@EnableConfigurationProperties({MultiTenantProperties.class, JpaProperties.class})
public class MultiTenantJpaConfiguration {
    private final Log log = LogFactory.getLog(MultiTenantJpaConfiguration.class);
    private String defaultTenantId = "";

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private MultiTenantProperties multiTenantProperties;

    @Autowired
    private ObjectProvider<JtaTransactionManager> jtaTxManager;

    @Autowired
    private JpaProperties jpaProperties;

    @Bean(name = "multiTenantDataSources")
    public Map<String, DataSource> multiTenantDataSources(@Qualifier("tenantBuilder") TenantBuilder tenantBuilder) {
        Map<String, DataSource> result = new HashMap<String, DataSource>(16);

        for (DataSourceProperties dsProperties : this.multiTenantProperties.getDataSources()) {
            DataSource ds = tenantBuilder.buildDataSourceBean(dsProperties);
            if (ds == null) {
                continue;
            }
            result.put(dsProperties.getTenantId(), ds);
            if (dsProperties.isDefaultTenant()) {
                this.defaultTenantId = dsProperties.getTenantId();
            }
        }
        tenantBuilder.setTenantDataSources(result);
        log.info("Create primary tenants DataSource finished (size: " + StringUtils.asString(result.size()) + ")");
        return result;
    }

    @Bean(name = "tenantBuilder")
    public TenantBuilder tenantBuilder() {
        return new TenantBuilder(this.jtaTxManager, this.multiTenantProperties, this.jpaProperties,
            this.applicationContext);
    }

    @Bean(name = "tenantEntityManagerFactories")
    public Map<String, LocalContainerEntityManagerFactoryBean> entityManagerFactories(
        @Qualifier("tenantBuilder") TenantBuilder tenantBuilder,
        @Qualifier("multiTenantDataSources") Map<String, DataSource> multiTenantDataSources) {
        Map<String, LocalContainerEntityManagerFactoryBean> entityManagerFactories = new HashMap<>(16);

        multiTenantDataSources.entrySet().forEach(entry -> {
            String beanName = tenantBuilder.buildEntityManagerFactoryBean(entry.getKey(), entry.getValue());
            if (StringUtils.isNotEmpty(beanName)) {
                entityManagerFactories.put(entry.getKey(),
                    (LocalContainerEntityManagerFactoryBean)applicationContext.getBean(beanName));
            }
        });
        tenantBuilder.setEntityFacotryMap(entityManagerFactories);
        log.info("Create primary tenants EntityManager finished (size: "
            + StringUtils.asString(entityManagerFactories.size()) + ")");
        return entityManagerFactories;
    }

    @Bean(name = "entityManagerFactory")
    @Primary
    public EntityManagerFactory entityManagerFactory(@Qualifier("tenantEntityManagerFactories") Map<String,
        LocalContainerEntityManagerFactoryBean> tenantEntityManagerFactories) {
        return (EntityManagerFactory)Proxy.newProxyInstance(this.getClass().getClassLoader(),
            new Class<?>[] {EntityManagerFactory.class},
            (proxy, method, args) -> method.invoke(getCurrentEntityManagerFacotry(tenantEntityManagerFactories), args));
    }

    public EntityManagerFactory
    getCurrentEntityManagerFacotry(Map<String, LocalContainerEntityManagerFactoryBean> map) {
        final String currentTenant = TenantHolder.getCurrentTenant();
        LocalContainerEntityManagerFactoryBean emb = null;
        if (currentTenant != null) {
            emb = map.get(currentTenant);
        } else {
            emb = map.get(this.defaultTenantId);
        }
        return emb.getObject();
    }
}
