package com.leon.solid.multitenant.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.leon.solid.multitenant.model.TenantDataSourceModel;
import com.leon.solid.multitenant.model.TenantRelationModel;
import com.leon.solid.multitenant.utils.StringUtils;

/**
 * The Class TenantRepositoryCustom.
 *
 * @author Leon.Tang
 * @date Aug 28, 2019
 */
@Repository
public class TenantRepositoryCustom extends JpaNativeQuerySupportRepository {
    public List<TenantRelationModel> findTenantRelation(String relationId) {

        Map<String, Object> map = new HashMap<String, Object>(16);
        StringBuilder sql = new StringBuilder();

        sql.append(" select ").append("   str.relation_id as \"relationId\" ")
        .append("  ,str.tenant_id as \"tenantId\" ").append("  ,str.package_name as \"packageName\" ")
        .append(" from ").append("   sys_tenant_relation str ").append(" where str.relation_id = :relationId ");

        map.put("relationId", relationId);

        return this.queryForList(sql.toString(), map, TenantRelationModel.class);
    }

    public TenantRelationModel findTenantRelationById(Integer id) {

        Map<String, Object> map = new HashMap<>(16);
        StringBuilder sql = new StringBuilder();

        sql.append(" select ").append("   str.relation_id as \"relationId\" ")
        .append("  ,str.tenant_id as \"tenantId\" ").append("  ,str.package_name as \"packageName\" ")
        .append(" from ").append("   sys_tenant_relation str ").append(" where str.id = :id ");

        map.put("id", id);

        return this.queryForSingle(sql.toString(), map, TenantRelationModel.class);
    }

    public TenantRelationModel findTenantRation(String relationId, String packageName) {

        Map<String, Object> map = new HashMap<String, Object>(16);
        map.put("relationId", relationId);
        StringBuilder sql = new StringBuilder();
        sql.append(" select ").append("   str.relation_id as \"relationId\" ")
        .append("  ,str.tenant_id as \"tenantId\" ").append("  ,str.package_name as \"packageName\" ")
        .append(" from ").append("   sys_tenant_relation str ").append(" where str.relation_id = :relationId ");

        if (StringUtils.isEmpty(packageName)) {
            sql.append(" and (str.package_name is null or str.package_name = '' )");
        } else {
            sql.append(" and str.package_name like :packageName ");
            map.put("packageName", "%" + packageName + "%");
        }

        return this.queryForSingle(sql.toString(), map, TenantRelationModel.class);
    }

    public List<TenantDataSourceModel> findTenantDataSourceList() {
        Map<String, Object> map = new HashMap<String, Object>(16);
        StringBuilder sql = new StringBuilder();
        sql.append(" select ").append("   td.tenant_id as \"tenantId\" ").append("  ,td.name as \"name\" ")
        .append("  ,td.db_type as \"dbType\" ").append("  ,td.db_url as \"dbUrl\" ")
        .append("  ,td.db_driver as \"dbDriver\" ").append("  ,td.db_extend as \"dbExtend\" ")
        .append("  ,td.db_username as \"dbUsername\" ").append("  ,td.db_password as \"dbPassword\" ")
        .append("  ,td.remark as \"remark\" ").append("  ,td.status as \"status\" ").append(" from ")
        .append("   sys_tenant_datasource td order by td.tenant_id");
        return this.queryForList(sql.toString(), map, TenantDataSourceModel.class);
    }

    public List<TenantDataSourceModel> findByDbTypeAndTenantIdLike(String dbType, String keyword) {
        Map<String, Object> map = new HashMap<>(16);
        StringBuilder sql = new StringBuilder();
        sql.append(" select ").append("   td.tenant_id as \"tenantId\" ").append("  ,td.name as \"name\" ")
        .append("  ,td.db_type as \"dbType\" ").append("  ,td.db_url as \"dbUrl\" ")
        .append("  ,td.db_driver as \"dbDriver\" ").append("  ,td.db_extend as \"dbExtend\" ")
        .append("  ,td.db_username as \"dbUsername\" ").append("  ,td.db_password as \"dbPassword\" ")
        .append("  ,td.remark as \"remark\" ").append("  ,td.status as \"status\" ").append(" from ")
        .append("   sys_tenant_datasource td ").append("   where  td.tenant_id like :keyword");

        map.put("keyword", "%" + keyword + "%");

        if (!StringUtils.isEmpty(dbType)) {
            sql.append(" and td.db_type = :dbType");
            map.put("dbType", dbType);
        }

        return this.queryForList(sql.toString(), map, TenantDataSourceModel.class);
    }

    public int insertTenantDataSource(TenantDataSourceModel dataSourceModel) {
        StringBuilder sql = new StringBuilder();
        sql.append(" insert into sys_tenant_datasource")
        .append(" (tenant_id, name, db_type, db_url, db_driver, db_extend, db_username, db_password, remark)")
        .append(" values (?,?,?,?,?,?,?,?,?)");
        return this.getEntityManager().createNativeQuery(sql.toString()).setParameter(1, dataSourceModel.getTenantId())
            .setParameter(2, dataSourceModel.getName()).setParameter(3, dataSourceModel.getDbType())
            .setParameter(4, dataSourceModel.getDbUrl()).setParameter(5, dataSourceModel.getDbDriver())
            .setParameter(6, dataSourceModel.getDbExtend()).setParameter(7, dataSourceModel.getDbUsername())
            .setParameter(8, dataSourceModel.getDbPassword()).setParameter(9, dataSourceModel.getRemark())
            .executeUpdate();
    }

    public int updateTenantDataSource(TenantDataSourceModel dataSourceModel) {
        StringBuilder sql = new StringBuilder();
        sql.append("update sys_tenant_datasource ")
        .append(" set name=?,db_type=?,db_url=?,db_driver=?,db_extend=?,db_username=?,db_password=?,remark=?")
        .append(" where tenant_id = ?");
        return this.getEntityManager().createNativeQuery(sql.toString()).setParameter(1, dataSourceModel.getName())
            .setParameter(2, dataSourceModel.getDbType()).setParameter(3, dataSourceModel.getDbUrl())
            .setParameter(4, dataSourceModel.getDbDriver()).setParameter(5, dataSourceModel.getDbExtend())
            .setParameter(6, dataSourceModel.getDbUsername()).setParameter(7, dataSourceModel.getDbPassword())
            .setParameter(8, dataSourceModel.getRemark()).setParameter(9, dataSourceModel.getTenantId())
            .executeUpdate();
    }

    public int updateTenantRelation(TenantRelationModel tenantRelation) {
        StringBuilder sql = new StringBuilder();
        sql.append("update sys_tenant_relation ").append(" set relation_id=?,package_name=?").append(" where id = ?");
        return this.getEntityManager().createNativeQuery(sql.toString()).setParameter(1, tenantRelation.getRelationId())
            .setParameter(2, tenantRelation.getPackageName()).setParameter(3, tenantRelation.getId()).executeUpdate();
    }

    public int updateTenantDataSourceStatus(String tenantId, Boolean status) {
        StringBuilder sql = new StringBuilder();
        sql.append("update sys_tenant_datasource ").append(" set status=?").append(" where tenant_id = ?");
        return this.getEntityManager().createNativeQuery(sql.toString()).setParameter(1, status)
            .setParameter(2, tenantId).executeUpdate();
    }

    public int deleteTenantDataSource(TenantDataSourceModel tenantDataSourceModel) {
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from sys_tenant_datasource").append(" where tenant_id = ?");
        return this.getEntityManager().createNativeQuery(sql.toString())
            .setParameter(1, tenantDataSourceModel.getTenantId()).executeUpdate();
    }

    public List<TenantRelationModel> findTenantRelationModelList() {
        Map<String, Object> map = new HashMap<String, Object>(16);
        StringBuilder sql = new StringBuilder();
        sql.append(" select ").append("   str.relation_id as \"relationId\" ")
        .append("  ,str.tenant_id as \"tenantId\" ").append("  ,str.package_name as \"packageName\" ")
        .append(" from ")
        .append("   sys_tenant_relation str order by str.relation_id,str.package_name,str.tenant_id");
        return this.queryForList(sql.toString(), map, TenantRelationModel.class);
    }

    public List<TenantRelationModel> findTenantRelations(TenantRelationModel tenantRelation) {
        String packageName = StringUtils.orDefault(tenantRelation.getPackageName(), "");

        Map<String, Object> map = new HashMap<String, Object>(16);
        StringBuilder sql = new StringBuilder();
        sql.append(" select ").append("   str.id as \"id\" ").append("  ,str.relation_id as \"relationId\" ")
        .append("  ,str.tenant_id as \"tenantId\" ").append("  ,str.package_name as \"packageName\" ")
        .append(" from ").append("   sys_tenant_relation str ").append(" where str.package_name like :packageName");

        map.put("packageName", "%" + packageName + "%");

        if (!StringUtils.isEmpty(tenantRelation.getTenantId())) {
            sql.append(" and str.tenant_id = :tenantId");
            map.put("tenantId", tenantRelation.getTenantId());
        }

        if (!StringUtils.isEmpty(tenantRelation.getRelationId())) {
            sql.append(" and str.relation_id like :relationId");
            map.put("relationId", "%" + tenantRelation.getRelationId() + "%");
        }

        sql.append(" order by  str.relation_id,str.package_name,str.tenant_id");

        return this.queryForList(sql.toString(), map, TenantRelationModel.class);
    }

    public boolean existsTenantId(String tenantId) {// primary
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(1) from sys_tenant_datasource where tenant_id = ?");
        Query nativeQuery = this.getEntityManager().createNativeQuery(sql.toString());
        int count = ((Number)nativeQuery.setParameter(1, tenantId).getSingleResult()).intValue();
        return count > 0;
    }

    public TenantDataSourceModel findByTenantId(String tenantId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ").append("   tenant_id as \"tenantId\" ").append("  ,name as \"name\" ")
        .append("  ,db_type as \"dbType\" ").append("  ,db_url as \"dbUrl\" ")
        .append("  ,db_driver as \"dbDriver\" ").append("  ,db_extend as \"dbExtend\" ")
        .append("  ,db_username as \"dbUsername\" ").append("  ,db_password as \"dbPassword\" ")
        .append("  ,remark as \"remark\" ").append("  ,status as \"status\" ").append(" from sys_tenant_datasource")
        .append(" where tenant_id = '" + tenantId + "'");
        return this.queryForSingle(sql.toString(), TenantDataSourceModel.class);
    }

    public int insertRelation(TenantRelationModel tenantRelationModel) {
        StringBuilder sql = new StringBuilder();
        sql.append(" insert into sys_tenant_relation").append(" (relation_id, tenant_id, package_name)")
        .append(" values (?,?,?)");
        return this.getEntityManager().createNativeQuery(sql.toString())
            .setParameter(1, tenantRelationModel.getRelationId()).setParameter(2, tenantRelationModel.getTenantId())
            .setParameter(3, tenantRelationModel.getPackageName()).executeUpdate();
    }

    public int deleteRelation(Integer id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from sys_tenant_relation").append(" where id = ?");
        return this.getEntityManager().createNativeQuery(sql.toString()).setParameter(1, id).executeUpdate();
    }
}