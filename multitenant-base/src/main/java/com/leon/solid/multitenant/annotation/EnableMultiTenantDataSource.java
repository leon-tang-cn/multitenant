package com.leon.solid.multitenant.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.leon.solid.multitenant.config.MultiTenantDataBaseConfiguration;
import com.leon.solid.multitenant.config.MultiTenantJpaConfiguration;

/**
 * The Interface EnableMultiTenantDataSource.
 *
 * @author Leon.Tang
 * @date Aug 28, 2019
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(value = {MultiTenantJpaConfiguration.class, MultiTenantDataBaseConfiguration.class})
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactory")
@EnableTransactionManagement
public @interface EnableMultiTenantDataSource {
    // setting of JPA Repository scan packages
    @AliasFor(annotation = EnableJpaRepositories.class)
    String[] value() default {};

    @AliasFor(annotation = EnableJpaRepositories.class)
    String[] basePackages() default {};
}
