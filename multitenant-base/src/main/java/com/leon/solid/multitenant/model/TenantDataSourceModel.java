package com.leon.solid.multitenant.model;

import java.io.Serializable;

/**
 * The Class TenantDataSourceModel.
 *
 * @author Leon.Tang
 * @date Aug 28, 2019
 */
public class TenantDataSourceModel implements Serializable {

    private static final long serialVersionUID = -4444433780855358554L;

    String tenantId;

    String name;

    String dbUrl;

    String dbDriver;

    String dbExtend;

    String dbType;

    String dbUsername;

    String dbPassword;

    String remark;

    Boolean status;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbDriver() {
        return dbDriver;
    }

    public void setDbDriver(String dbDriver) {
        this.dbDriver = dbDriver;
    }

    public String getDbExtend() {
        return dbExtend;
    }

    public void setDbExtend(String dbExtend) {
        this.dbExtend = dbExtend;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
