package com.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.gmall.bean.PmsBaseCatalog1;
import com.gmall.manage.mapper.Catalog1Mapper;
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

    @Override
    public List<PmsBaseCatalog1> selectCatalog1All() {
        return catalog1Mapper.selectAll();
    }
}
