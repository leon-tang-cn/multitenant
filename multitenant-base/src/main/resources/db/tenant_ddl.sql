CREATE TABLE sys_tenant_relation
( 
    id SERIAL NOT NULL constraint sys_tenant_relation_pk  primary key,
    relation_id  varchar(64) NOT NULL DEFAULT ' '::character varying,
    tenant_id    varchar(64) NOT NULL DEFAULT ' '::character varying,
    package_name varchar(255)
);

create table sys_tenant_datasource
(
    tenant_id   varchar(64) not null
        constraint sys_tenant_datasource_pk
            primary key,
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
comment on column sys_tenant_datasource.status is '0-disable 1-enable';
alter table sys_tenant_datasource
    owner to polarwind;