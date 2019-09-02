Multiple Tenant
===
![][bg-jdk] ![][bg-springboot] ![][bg-spring-data-jpa]

[bg-jdk]: https://img.shields.io/badge/jdk-1.8-brightgreen.svg?style=flat&logo=java&color=information&labelColor=important
[bg-springboot]: https://img.shields.io/badge/SpringBoot-2.1.4-information.svg?labelColor=blue
[bg-spring-data-jpa]: https://img.shields.io/badge/SpringDataJpa-2.1.6-information.svg?labelColor=blue

## Base
  
Base on `Spring Data JPA V2.1.6`, extended to support add/remove tenant entity manager(include datasource connection) in a running application.

## Usage

First, create tables from `src\main\resouces\db\tenant_ddl.sql` which located in `multitenant-base`.

```sql
-- ----------------------------
-- Table structure for sys_tenant_relation
-- ----------------------------
CREATE TABLE sys_tenant_relation
( 
    id SERIAL NOT NULL constraint sys_tenant_relation_pk  primary key,
    relation_id  varchar(64) NOT NULL DEFAULT ' '::character varying,
    tenant_id    varchar(64) NOT NULL DEFAULT ' '::character varying,
    package_name varchar(255)
);

-- ----------------------------
-- Table structure for sys_tenant_datasource
-- ----------------------------
create table sys_tenant_datasource
(
    tenant_id   varchar(64) not null
        constraint sys_tenant_datasource_pk primary key,
    db_type     varchar(20) not null,
    name        varchar(64),
    db_url      varchar(255),
    db_driver   varchar(64),
    db_extend   varchar(255),
    db_username varchar(64),
    db_password varchar(64),
    remark      varchar(64),
    status      varchar(1) default '1'
);

```

- Application congiration (use application.yml or application.properties)
  - multitenant.entityScanPackages (entity scan path)
  - multitenant.dataSources.tenantId (tenant id)
    <br>*Notice: tenantId should be unique*
  - multitenant.dataSources.defaultTenant (is it a default datasource)
    <br>**default: false**
    <br>*Notice: At least 1 datasource should be configured*
  - multitenant.dataSources.url (database connection url)
  - multitenant.dataSources.driverClassName (databse driver)
  - multitenant.dataSources.username (user name)
  - multitenant.dataSources.password (password)
  - multitenant.dataSources.xa.dataSourceClassName (XA datasource class name)
  
- Configuration sample
  ```yaml
  multitenant:
      entityScanPackages: 
      -
        "com.blank.domain.entity"
      dataSources:
      -
          tenantId: tenant1
          url: jdbc:postgresql://localhost:5432/multitenant
          defaultTenant: true
          driverClassName: org.postgresql.Driver
          username: multitenant
          password: multitenant
          xa:
            dataSourceClassName: org.postgresql.xa.PGXADataSource
  ```

  In case of using multitenant component, use `@EnableMultiTenantDataSource` replace the `@EnableJpaRepositories`. `basePackages` attribute in `@EnableMultiTenantDataSource` equivalent to `basePackages` attribute in `@EnableJpaRepositories`.
   
- Using `TenantUserDetails` interface in Spring Security Enviroment.

