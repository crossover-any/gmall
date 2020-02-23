package com.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.gmall.bean.PmsBaseCatalog1;
import com.gmall.bean.PmsBaseCatalog2;
import com.gmall.bean.PmsBaseCatalog3;
import com.gmall.manage.mapper.Catalog1Mapper;
import com.gmall.manage.mapper.Catalog2Mapper;
import com.gmall.manage.mapper.Catalog3Mapper;
import com.gmall.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author Tengxq
 * @Date 2020/2/23 17:58
 * @Describe
 * @Version 1.0
 **/

@Service
public class Catalog1ServiceImpl implements CatalogService {

    @Autowired
    private Catalog1Mapper catalog1Mapper;

    @Autowired
    private Catalog2Mapper catalog2Mapper;

    @Autowired
    private Catalog3Mapper catalog3Mapper;

    @Override
    public List<PmsBaseCatalog1> selectCatalog1All() {
        return catalog1Mapper.selectAll();
    }

    @Override
    public List<PmsBaseCatalog2> selectCatalog2ByCatalog1Id(PmsBaseCatalog2 pmsBaseCatalog2) {
        return catalog2Mapper.select(pmsBaseCatalog2);
    }

    @Override
    public List<PmsBaseCatalog3> selectCatalog3ByCatalog2Id(PmsBaseCatalog3 pmsBaseCatalog3) {
        return catalog3Mapper.select(pmsBaseCatalog3);
    }
}
