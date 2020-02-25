package com.gmall.service;

import com.gmall.bean.PmsProductImage;
import com.gmall.bean.PmsProductInfo;
import com.gmall.bean.PmsProductSaleAttr;
import com.gmall.bean.PmsSkuAttrValue;

import java.util.List;

public interface SpuService {
    List<PmsProductInfo> spuList(String catalog3Id);

    void saveSpuInfo(PmsProductInfo pmsProductInfo);

    List<PmsProductImage> getSpuImageList(String spuId);

    List<PmsProductSaleAttr> getSpuSaleAttrList(String spuId);
}
