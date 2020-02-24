package com.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.gmall.bean.PmsBaseAttrInfo;
import com.gmall.bean.PmsBaseAttrValue;
import com.gmall.bean.PmsBaseSaleAttr;
import com.gmall.bean.PmsProductSaleAttr;
import com.gmall.manage.mapper.AttrInfoMapper;
import com.gmall.manage.mapper.AttrValueMapper;
import com.gmall.manage.mapper.PmsBaseSaleAttrMapper;
import com.gmall.service.AttrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author Tengxq
 * @Date 2020/2/23 20:04
 * @Describe
 * @Version 1.0
 **/

@Service
public class AttrServiceImpl implements AttrService {

    @Autowired
    private AttrInfoMapper attrInfoMapper;

    @Autowired
    private AttrValueMapper attrValueMapper;

    @Autowired
    private PmsBaseSaleAttrMapper pmsBaseSaleAttrMapper;

    @Override
    public List<PmsBaseAttrInfo> getInfoList(String catalog3Id) {
        PmsBaseAttrInfo pmsBaseAttrInfo = new PmsBaseAttrInfo();
        pmsBaseAttrInfo.setCatalog3Id(catalog3Id);
        return attrInfoMapper.select(pmsBaseAttrInfo);
    }

    @Override
    public String addAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {
        if (StringUtils.isEmpty(pmsBaseAttrInfo.getId())){
            attrInfoMapper.insertSelective(pmsBaseAttrInfo);
            List<PmsBaseAttrValue> list = pmsBaseAttrInfo.getAttrValueList();
            for (PmsBaseAttrValue pmsBaseAttrValue : list) {
                pmsBaseAttrValue.setAttrId(pmsBaseAttrInfo.getId());
                attrValueMapper.insertSelective(pmsBaseAttrValue);
            };
        }else{
            Example example = new Example(PmsBaseAttrInfo.class);
            example.createCriteria().andEqualTo("id",pmsBaseAttrInfo.getId());
            attrInfoMapper.updateByExample(pmsBaseAttrInfo,example);
            List<PmsBaseAttrValue> list =  pmsBaseAttrInfo.getAttrValueList();
            PmsBaseAttrValue pmsBaseAttrValueDel = new PmsBaseAttrValue();
            pmsBaseAttrValueDel.setAttrId(pmsBaseAttrInfo.getId());
            attrValueMapper.delete(pmsBaseAttrValueDel);
            for (PmsBaseAttrValue attrValue:list) {
                attrValue.setAttrId(pmsBaseAttrInfo.getId());
                attrValueMapper.insertSelective(attrValue);
            }
        }
        return "success";
    }

    @Override
    public List<PmsBaseAttrValue> getAttrValueList(String attrId) {
        PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
        pmsBaseAttrValue.setAttrId(attrId);
        return attrValueMapper.select(pmsBaseAttrValue);
    }

    @Override
    public List<PmsBaseSaleAttr> baseSaleAttrList() {
        return pmsBaseSaleAttrMapper.selectAll();
    }
}
