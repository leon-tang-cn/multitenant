package com.leon.solid.multitenant.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.leon.solid.multitenant.model.TenantDataSourceModel;
import com.leon.solid.multitenant.model.TenantRelationModel;
import com.leon.solid.multitenant.service.TenantDataSourceService;
import com.leon.solid.multitenant.utils.CollectionUtils;
import com.leon.solid.multitenant.utils.StringUtils;

/**
 * The Class MultiTenantDataBaseConfiguration.
 *
 * @author Leon.Tang
 * @date Aug 28, 2019
 */
@Configuration
@ComponentScan(basePackages = {"com.leon.solid.multitenant"})
public class MultiTenantDataBaseConfiguration {
    private final Log log = LogFactory.getLog(MultiTenantDataBaseConfiguration.class);
    @Autowired
    private TenantDataSourceService tenantDataSourceService;

    @Autowired
    @Qualifier("tenantBuilder")
    private TenantBuilder tenantBuilder;

    @PostConstruct
    public void init() {
        Map<String, HashMap<String, String>> tenantPackageRelation = new HashMap<String, HashMap<String, String>>(16);
        List<TenantRelationModel> list = tenantDataSourceService.findAllTenantRelation();
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(relation -> {
                if (StringUtils.isNotEmpty(relation.getPackageName())) {
                    if (tenantPackageRelation.containsKey(relation.getRelationId())) {
                        tenantPackageRelation.get(relation.getRelationId()).put(relation.getPackageName(),
                            relation.getTenantId());
                    } else {
                        HashMap<String, String> packageMap = new HashMap<String, String>(16);
                        packageMap.put(relation.getPackageName(), relation.getTenantId());
                        tenantPackageRelation.put(relation.getRelationId(), packageMap);
                    }
                }
            });
        }

        tenantBuilder.setTenantPackageRelation(tenantPackageRelation);
        List<TenantDataSourceModel> datasourceList = tenantDataSourceService.getAllTenantDataSources();
        if (CollectionUtils.isNotEmpty(datasourceList)) {
            datasourceList.forEach(dsModel -> {
                tenantBuilder.build(dsModel);
            });
            log.info("Create extended tenants from database configuration finished (size: "
                + StringUtils.asString(datasourceList.size()) + ")");
        }
    }
}
