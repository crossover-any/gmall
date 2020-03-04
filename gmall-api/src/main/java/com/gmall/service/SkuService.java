package com.gmall.service;

import com.gmall.bean.PmsSkuInfo;

import java.util.List;

public interface SkuService {
    void saveSkuInfo(PmsSkuInfo pmsSkuInfo);

    PmsSkuInfo getPmsSkuInfo(String skuId);

    List<PmsSkuInfo> getAllPmsSkuInfo();
}
