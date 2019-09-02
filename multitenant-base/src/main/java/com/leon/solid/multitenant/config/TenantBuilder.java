package com.leon.solid.multitenant.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.jta.bitronix.PoolingDataSourceBean;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.leon.solid.multitenant.config.MultiTenantProperties.DataSourceProperties;
import com.leon.solid.multitenant.datasource.PoolingDataSourceFactory;
import com.leon.solid.multitenant.model.TenantDataSourceModel;
import com.leon.solid.multitenant.utils.StringUtils;

/**
 * The Class TenantBuilder.
 *
 * @author Leon.Tang
 * @date Aug 28, 2019
 */
public class TenantBuilder {
    private static final String DB_TYPE_JDBC = "jdbc";
    private static final String DB_TYPE_JNDI = "jndi";
    private static final String DB_TYPE_BEAN = "bean";
    private static final String ENTITY_MANAGER_FACTORY_PREFIX = "&";
    private static final String ENTITY_MANAGER_FACTORY_SUFFIX = "EntityManagerFactory";

    private ApplicationContext applicationContext;

    private Map<String, DataSource> tenantDataSources;

    private Map<String, HashMap<String, String>> tenantPackageRelation;

    private ObjectProvider<JtaTransactionManager> jtaTxManager;

    private MultiTenantProperties multiTenantProperties;

    private Map<String, LocalContainerEntityManagerFactoryBean> entityFacotryMap;

    private JpaProperties jpaProperties;

    private final PoolingDataSourceFactory poolingDataSourceFactory = new PoolingDataSourceFactory();

    private DefaultListableBeanFactory defaultListableBeanFactory;

    public TenantBuilder(ObjectProvider<JtaTransactionManager> jtaTxManager,
        MultiTenantProperties multiTenantProperties, JpaProperties jpaProperties,
        ApplicationContext applicationContext) {
        this(jtaTxManager, multiTenantProperties, jpaProperties, applicationContext, null, null, null);
    }

    public TenantBuilder(ObjectProvider<JtaTransactionManager> jtaTxManager,
        MultiTenantProperties multiTenantProperties, JpaProperties jpaProperties, ApplicationContext applicationContext,
        Map<String, DataSource> tenantDataSources, Map<String, LocalContainerEntityManagerFactoryBean> entityFacotryMap,
        Map<String, HashMap<String, String>> tenantPackageRelation) {
        setJtaTxManager(jtaTxManager);
        setMultiTenantProperties(multiTenantProperties);
        setJpaProperties(jpaProperties);
        setApplicationContext(applicationContext);
        setTenantDataSources(tenantDataSources);
        setEntityFacotryMap(entityFacotryMap);
        setTenantPackageRelation(tenantPackageRelation);
    }

    public void build(TenantDataSourceModel model) {
        if (this.multiTenantProperties == null || this.jtaTxManager == null || this.jpaProperties == null) {
            return;
        }
        if (this.applicationContext == null || this.tenantDataSources == null || this.entityFacotryMap == null) {
            return;
        }
        if (model == null || model.getStatus() == false) {
            return;
        }
        if (!this.tenantDataSources.containsKey(model.getTenantId())) {
            switch (model.getDbType()) {
                case DB_TYPE_JDBC:
                    DataSourceProperties dsProperties = new DataSourceProperties();
                    dsProperties.setUrl(model.getDbUrl());
                    dsProperties.setUsername(model.getDbUsername());
                    dsProperties.setPassword(model.getDbPassword());
                    dsProperties.setDriverClassName(model.getDbDriver());
                    dsProperties.getXa().setDataSourceClassName(model.getDbExtend());
                    dsProperties.setTenantId(model.getTenantId());

                    DataSource ds = buildDataSourceBean(dsProperties);
                    if (ds == null) {
                        break;
                    }
                    this.tenantDataSources.put(model.getTenantId(), ds);
                    break;
                case DB_TYPE_JNDI:
                    JndiDataSourceLookup lookup = new JndiDataSourceLookup();
                    lookup.setResourceRef(true);
                    this.tenantDataSources.put(model.getTenantId(), lookup.getDataSource(model.getName()));
                case DB_TYPE_BEAN:
                    this.tenantDataSources.put(model.getTenantId(),
                        (javax.sql.DataSource)this.applicationContext.getBean(model.getName()));
                default:
                    break;
            }
            definitionEntityFactoryBean(model);
        }
    }

    public PoolingDataSourceBean buildDataSourceBean(DataSourceProperties dsProperties) {
        if (dsProperties == null) {
            return null;
        }
        PoolingDataSourceBean ds = this.poolingDataSourceFactory.build(dsProperties);
        ds.setUniqueName(dsProperties.getTenantId());
        return ds;
    }

    public String buildEntityManagerFactoryBean(String tenantId, DataSource targetDataSource) {
        if (this.multiTenantProperties == null || this.jtaTxManager == null || this.jpaProperties == null) {
            return null;
        }
        if (StringUtils.isEmpty(tenantId) || targetDataSource == null) {
            return null;
        }
        BeanDefinitionBuilder beanDefinitionBuilder =
            BeanDefinitionBuilder.genericBeanDefinition(LocalContainerEntityManagerFactoryBean.class);
        beanDefinitionBuilder.addPropertyValue("packagesToScan", this.multiTenantProperties.getEntityScanPackages());
        beanDefinitionBuilder.addPropertyValue("jpaVendorAdapter", new HibernateJpaVendorAdapter());
        beanDefinitionBuilder.addPropertyValue("persistenceUnitName", tenantId);

        if (this.jtaTxManager.getIfAvailable() != null) {
            beanDefinitionBuilder.addPropertyValue("jtaDataSource", targetDataSource);
        } else {
            beanDefinitionBuilder.addPropertyValue("dataSource", targetDataSource);
        }
        beanDefinitionBuilder.addPropertyValue("jpaPropertyMap", this.jpaProperties.getProperties());
        beanDefinitionBuilder.addPropertyValue("mappingResources", this.jpaProperties.getMappingResources()
            .toArray(new String[this.jpaProperties.getMappingResources().size()]));

        defaultListableBeanFactory.registerBeanDefinition(buildEntityManagerBeanName(tenantId),
            beanDefinitionBuilder.getBeanDefinition());
        return ENTITY_MANAGER_FACTORY_PREFIX + buildEntityManagerBeanName(tenantId);
    }

    public TenantBuilder setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        if (this.applicationContext != null) {
            setDefaultListableBeanFactory();
        }
        return this;
    }

    public TenantBuilder setTenantDataSources(Map<String, DataSource> tenantDataSources) {
        this.tenantDataSources = tenantDataSources;
        return this;
    }

    public TenantBuilder setJtaTxManager(ObjectProvider<JtaTransactionManager> jtaTxManager) {
        this.jtaTxManager = jtaTxManager;
        return this;
    }

    public TenantBuilder setMultiTenantProperties(MultiTenantProperties multiTenantProperties) {
        this.multiTenantProperties = multiTenantProperties;
        return this;
    }

    public TenantBuilder setEntityFacotryMap(Map<String, LocalContainerEntityManagerFactoryBean> entityFacotryMap) {
        this.entityFacotryMap = entityFacotryMap;
        return this;
    }

    public TenantBuilder setJpaProperties(JpaProperties jpaProperties) {
        this.jpaProperties = jpaProperties;
        return this;
    }

    public Map<String, HashMap<String, String>> getTenantPackageRelation() {
        return tenantPackageRelation;
    }

    public void setTenantPackageRelation(Map<String, HashMap<String, String>> tenantPackageRelation) {
        this.tenantPackageRelation = tenantPackageRelation;
    }

    private String buildEntityManagerBeanName(String tenantId) {
        return tenantId + ENTITY_MANAGER_FACTORY_SUFFIX;
    }

    public static String getEntityManagerPrefixName() {
        return ENTITY_MANAGER_FACTORY_PREFIX;
    }

    public void setDefaultListableBeanFactory() {
        this.defaultListableBeanFactory =
            (DefaultListableBeanFactory)this.applicationContext.getAutowireCapableBeanFactory();
    }

    private void definitionEntityFactoryBean(TenantDataSourceModel model) {
        String beanName =
            buildEntityManagerFactoryBean(model.getTenantId(), this.tenantDataSources.get(model.getTenantId()));
        if (StringUtils.isNotEmpty(beanName)) {
            this.entityFacotryMap.put(model.getTenantId(),
                (LocalContainerEntityManagerFactoryBean)this.applicationContext.getBean(beanName));
        }
    }

    public void destory(TenantDataSourceModel tenantDataSourceModel) {
        entityFacotryMap.remove(tenantDataSourceModel.getTenantId());
        if (DB_TYPE_JDBC.equals(tenantDataSourceModel.getDbType())) {
            defaultListableBeanFactory.destroyBean(buildEntityManagerBeanName(tenantDataSourceModel.getTenantId()));
        }
    }
}
