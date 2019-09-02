package com.leon.solid.multitenant.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leon.solid.multitenant.config.TenantBuilder;
import com.leon.solid.multitenant.config.TenantHolder;
import com.leon.solid.multitenant.exceptions.ResourceNotFoundException;
import com.leon.solid.multitenant.model.TenantDataSourceModel;
import com.leon.solid.multitenant.model.TenantRelationModel;
import com.leon.solid.multitenant.repository.TenantRepositoryCustom;
import com.leon.solid.multitenant.utils.StringUtils;

/**
 * The Class TenantDataSourceService.
 *
 * @author Leon.Tang
 * @date Aug 28, 2019
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TenantDataSourceService {
    @Inject
    TenantRepositoryCustom tenantRepositoryCustom;

    @Autowired
    @Qualifier("multiTenantDataSources")
    private Map<String, DataSource> multiTenantDataSources;

    @Autowired
    @Qualifier("tenantBuilder")
    private TenantBuilder tenantBuilder;

    public List<TenantDataSourceModel> getAllTenantDataSources() {
        return tenantRepositoryCustom.findTenantDataSourceList();
    }

    public DataSource getDataSource(String tenantId) {
        return multiTenantDataSources.get(tenantId);
    }

    public DataSource getDataSourceByRelationId(String relationId) {
        TenantRelationModel tenantRelation = tenantRepositoryCustom.findTenantRation(relationId, null);
        if (tenantRelation != null) {
            return multiTenantDataSources.get(tenantRelation.getTenantId());
        }
        return null;
    }

    /**
     * change Datasource by tenant id in Current Thread
     */
    public void changeDataSourceByTenantId(String tenantId) {
        TenantHolder.setTenant(tenantId);
    }

    /**
     * change Datasource by relation id in Current Thread
     */
    public void changeDataSourceByRelationId(String relationId) {
        TenantRelationModel tenantRelation = tenantRepositoryCustom.findTenantRation(relationId, null);
        if (tenantRelation != null) {
            TenantHolder.setTenant(tenantRelation.getTenantId());
        } else {
            throw new ResourceNotFoundException(relationId + " Not Found!");
        }
    }

    public String getTenantIdByRelationId(String relationId) {
        TenantRelationModel tenantRelation = tenantRepositoryCustom.findTenantRation(relationId, null);
        if (tenantRelation != null) {
            return tenantRelation.getTenantId();
        }
        return null;
    }

    /**
     * 根据数据类型，或关键字赛选数据源
     *
     * @param dbType
     *            jdbc,jndi,bean
     * @param keyword
     *            tenantId关键字
     * @return
     */
    public List<TenantDataSourceModel> findAllTenantDataSource(String dbType, String keyword) {
        if (StringUtils.isEmpty(dbType) && StringUtils.isEmpty(keyword)) {
            return findAllTenantDataSource();
        }
        return tenantRepositoryCustom.findByDbTypeAndTenantIdLike(dbType, keyword);
    }

    public List<TenantDataSourceModel> findAllTenantDataSource() {
        return tenantRepositoryCustom.findTenantDataSourceList();
    }

    public int insertAndPublishTenantDataSource(TenantDataSourceModel dataSourceModel) {
        dataSourceModel.setStatus(true);
        int insertCount = insertTenantDataSource(dataSourceModel);
        publishTenantDataSource(dataSourceModel);
        return insertCount;
    }

    public int insertTenantDataSource(TenantDataSourceModel dataSourceModel) {
        return tenantRepositoryCustom.insertTenantDataSource(dataSourceModel);
    }

    public int updateTenantDataSource(TenantDataSourceModel model) {
        return tenantRepositoryCustom.updateTenantDataSource(model);
    }

    public void publishTenantDataSource(TenantDataSourceModel dataSourceModel) {
        tenantBuilder.build(dataSourceModel);
    }

    public int deleteAndDestroyTenantDataSource(TenantDataSourceModel tenantDataSourceModel) {
        multiTenantDataSources.remove(tenantDataSourceModel.getTenantId());
        tenantBuilder.destory(tenantDataSourceModel);
        return tenantRepositoryCustom.deleteTenantDataSource(tenantDataSourceModel);
    }

    public List<TenantRelationModel> findAllTenantRelation() {
        return tenantRepositoryCustom.findTenantRelationModelList();
    }

    public boolean existsTenantId(String tenantId) {
        return tenantRepositoryCustom.existsTenantId(tenantId);
    }

    public TenantDataSourceModel findByTenantId(String tenantId) {
        return tenantRepositoryCustom.findByTenantId(tenantId);
    }

    public int updateTenantDataSourceStatus(TenantDataSourceModel tenantDataSourceModel) {
        if (!tenantDataSourceModel.getStatus()) {
            tenantBuilder.destory(tenantDataSourceModel);
        } else {
            publishTenantDataSource(tenantDataSourceModel);
        }
        return tenantRepositoryCustom.updateTenantDataSourceStatus(tenantDataSourceModel.getTenantId(),
            tenantDataSourceModel.getStatus());
    }

    public int insertRelation(TenantRelationModel tenantRelationModel) {
        insertPackageRelation(tenantRelationModel);
        return tenantRepositoryCustom.insertRelation(tenantRelationModel);
    }

    public List<TenantRelationModel> findTenantRelations(TenantRelationModel tenantRelationModel) {
        return tenantRepositoryCustom.findTenantRelations(tenantRelationModel);
    }

    public boolean existsRelationIdAndPackageName(TenantRelationModel tenantRelationModel) {
        TenantRelationModel tenantRation = tenantRepositoryCustom.findTenantRation(tenantRelationModel.getRelationId(),
            tenantRelationModel.getPackageName());
        boolean flag = false;
        if (tenantRation != null) {
            flag = true;
        }
        return flag;
    }

    public int deleteRelation(Integer id) {
        TenantRelationModel relationModel = tenantRepositoryCustom.findTenantRelationById(id);
        removePackageRelation(relationModel);
        return tenantRepositoryCustom.deleteRelation(id);
    }

    public int updateTenantRelation(TenantRelationModel tenantRelation) {
        TenantRelationModel dbRelation = tenantRepositoryCustom.findTenantRelationById(tenantRelation.getId());
        removePackageRelation(dbRelation);
        insertPackageRelation(tenantRelation);

        return tenantRepositoryCustom.updateTenantRelation(tenantRelation);
    }

    private void removePackageRelation(TenantRelationModel relation) {
        Map<String, HashMap<String, String>> tenantPackageRelation = tenantBuilder.getTenantPackageRelation();
        HashMap<String, String> map = tenantPackageRelation.get(relation.getRelationId());
        if (map != null) {
            map.remove(relation.getPackageName());
        }
    }

    private void insertPackageRelation(TenantRelationModel tenantRelationModel) {
        Map<String, HashMap<String, String>> tenantPackageRelation = tenantBuilder.getTenantPackageRelation();
        HashMap<String, String> map = tenantPackageRelation.get(tenantRelationModel.getRelationId());
        if (map == null) {
            map = new HashMap<String, String>(16);
        }

        map.put(tenantRelationModel.getPackageName(), tenantRelationModel.getTenantId());
        tenantPackageRelation.put(tenantRelationModel.getRelationId(), map);
    }
}
