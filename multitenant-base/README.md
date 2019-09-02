A1 Solid Multiple Tenant
===
![][bg-jdk] ![][bg-springboot] ![][bg-spring-data-jpa]

[bg-jdk]: https://img.shields.io/badge/jdk-1.8-brightgreen.svg?style=flat&logo=java&color=information&labelColor=important
[bg-springboot]: https://img.shields.io/badge/SpringBoot-2.1.4-information.svg?labelColor=blue
[bg-spring-data-jpa]: https://img.shields.io/badge/SpringDataJpa-2.1.6-information.svg?labelColor=blue

## 介绍
  
以 `Spring Data JPA V2.1.6` 为基础，扩展支持多租户功能.

## 使用说明

首先需要从 `a1-solid-multitenant` 包中获取创建表的sql文在数据库中执行表创建。

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

- yml配置
  - a1solid.multitenant.entityScanPackages (扫描的Entity包路径)
  - a1solid.multitenant.dataSources.tenantId (租户ID)
    *注意：tenantId必须确保唯一*
  - a1solid.multitenant.dataSources.defaultTenant (是否默认数据源)
    默认值：false
    *注意：必须设置一个默认数据源*
  - a1solid.multitenant.dataSources.url (数据源连接地址)
  - a1solid.multitenant.dataSources.driverClassName (数据源驱动)
  - a1solid.multitenant.dataSources.username (用户名)
  - a1solid.multitenant.dataSources.password (密码)
  - a1solid.multitenant.dataSources.xa.dataSourceClassName (分布式数据源类)
  
- 示例
  ```yaml
  a1solid.multitenant:
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

  使用 `@EnableMultiTenantDataSource` 替换 `@EnableJpaRepositories`. `@EnableJpaRepositories`中的`basePackages`（需要扫描的包）属性相当于`@EnableJpaRepositories`的`basePackages`。
   
- 使用TenantUserDetails接口

### 页面

框架默认提供一套完整的基于VIY的数据源查询编辑页面，服务启动后，可通过访问`/multitenant/datasources`进行查看编辑。

获取页面html组件作为开发项目的页面组件使用。

