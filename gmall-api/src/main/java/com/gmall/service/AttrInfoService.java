package com.gmall.service;

import com.gmall.bean.PmsBaseAttrInfo;

import java.util.List;

public interface AttrInfoService {
    List<PmsBaseAttrInfo> getInfoList(String catalog3Id);

    String addAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);
}
