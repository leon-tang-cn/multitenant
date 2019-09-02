package com.leon.solid.multitenant.datasource;

import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;

/**
 * This is a support class for building data sources easily using Spring Boot
 * {@link DataSourceBuilder}.
 * <p>
 * This class is a batch application that uses Spring Boot, and is provided to easily define
 * multiple data sources. The data source construction algorithm is implemented based on the
 * internal behavior of {@code NonEmbeddedConfiguration} of {@link DataSourceAutoConfiguration}.
 */
public class DataSourceFactory {

  /**
   * Construct {@link DataSource} based on the setting of {@link DataSourceProperties} of Spring Boot.
   *
   * @param datasourceProperties {@link DataSourceProperties} of Spring Boot 
   * @return Builded {@link DataSource}
   */
  public DataSource build(final DataSourceProperties datasourceProperties) {
    final DataSourceBuilder<? extends DataSource> factory =
        DataSourceBuilder.create(datasourceProperties.getClassLoader())
            .driverClassName(datasourceProperties.getDriverClassName())
            .url(datasourceProperties.getUrl())
            .username(datasourceProperties.getUsername())
            .password(datasourceProperties.getPassword());

    if (datasourceProperties.getType() != null) {
      factory.type(datasourceProperties.getType());
    }
    return factory.build();
  }
}
