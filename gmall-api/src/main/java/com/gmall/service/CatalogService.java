package com.gmall.service;

import com.gmall.bean.PmsBaseCatalog1;
import com.gmall.bean.PmsBaseCatalog2;
import com.gmall.bean.PmsBaseCatalog3;

import java.util.List;

public interface CatalogService {
    List<PmsBaseCatalog1> selectCatalog1All();

    List<PmsBaseCatalog2> selectCatalog2ByCatalog1Id(PmsBaseCatalog2 pmsBaseCatalog2);

    List<PmsBaseCatalog3> selectCatalog3ByCatalog2Id(PmsBaseCatalog3 pmsBaseCatalog3);
}
