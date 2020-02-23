package com.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.gmall.bean.PmsBaseAttrInfo;
import com.gmall.bean.PmsBaseAttrValue;
import com.gmall.manage.mapper.AttrInfoMapper;
import com.gmall.manage.mapper.AttrValueMapper;
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

    @Autowired
    private AttrValueMapper attrValueMapper;

    @Override
    public List<PmsBaseAttrInfo> getInfoList(String catalog3Id) {
        PmsBaseAttrInfo pmsBaseAttrInfo = new PmsBaseAttrInfo();
        pmsBaseAttrInfo.setCatalog3Id(catalog3Id);
        return attrInfoMapper.select(pmsBaseAttrInfo);
    }

    @Override
    public String addAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {
        attrInfoMapper.insertSelective(pmsBaseAttrInfo);
        List<PmsBaseAttrValue> list = pmsBaseAttrInfo.getAttrValueList();
        for (PmsBaseAttrValue pmsBaseAttrValue : list) {
            pmsBaseAttrValue.setAttrId(pmsBaseAttrInfo.getId());
            attrValueMapper.insertSelective(pmsBaseAttrValue);
        };
        return "success";
    }
}
