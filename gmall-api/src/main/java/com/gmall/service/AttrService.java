package com.gmall.service;

import com.gmall.bean.PmsBaseAttrInfo;
import com.gmall.bean.PmsBaseAttrValue;
import com.gmall.bean.PmsBaseSaleAttr;
import com.gmall.bean.PmsProductSaleAttr;

import java.util.List;

public interface AttrService {
    List<PmsBaseAttrInfo> getInfoList(String catalog3Id);

    String addAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);

    List<PmsBaseAttrValue> getAttrValueList(String attrId);

    List<PmsBaseSaleAttr> baseSaleAttrList();
}
