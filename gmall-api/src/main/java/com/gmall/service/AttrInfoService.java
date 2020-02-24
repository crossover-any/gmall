package com.gmall.service;

import com.gmall.bean.PmsBaseAttrInfo;
import com.gmall.bean.PmsBaseAttrValue;

import java.util.List;

public interface AttrInfoService {
    List<PmsBaseAttrInfo> getInfoList(String catalog3Id);

    String addAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);

    List<PmsBaseAttrValue> getAttrValueList(String attrId);
}
