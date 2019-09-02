package com.leon.solid.multitenant.model;

import java.io.Serializable;

/**
 * The Class TenantRelationModel.
 *
 * @author Leon.Tang
 * @date Aug 28, 2019
 */
public class TenantRelationModel implements Serializable {

    private static final long serialVersionUID = 1L;

    Integer id;

    String relationId;

    String tenantId;

    String packageName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

}
