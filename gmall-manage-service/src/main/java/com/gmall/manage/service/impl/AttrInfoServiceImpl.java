package com.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.gmall.bean.PmsBaseAttrInfo;
import com.gmall.manage.mapper.AttrInfoMapper;
import com.gmall.service.AttrInfoService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author Tengxq
 * @Date 2020/2/23 20:04
 * @Describe
 * @Version 1.0
 **/

@Service
public class AttrInfoServiceImpl implements AttrInfoService {

    @Autowired
    private AttrInfoMapper attrInfoMapper;

    @Override
    public List<PmsBaseAttrInfo> getInfoList(String catalog3Id) {
        PmsBaseAttrInfo pmsBaseAttrInfo = new PmsBaseAttrInfo();
        pmsBaseAttrInfo.setCatalog3Id(catalog3Id);
        return attrInfoMapper.select(pmsBaseAttrInfo);
    }
}
