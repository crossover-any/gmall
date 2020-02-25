package com.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.gmall.bean.*;
import com.gmall.manage.mapper.PmsProductImageMapper;
import com.gmall.manage.mapper.PmsProductInfoMapper;
import com.gmall.manage.mapper.PmsProductSaleAttrMapper;
import com.gmall.manage.mapper.PmsProductSaleAttrValueMapper;
import com.gmall.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author Tengxq
 * @Date 2020/2/24 17:17
 * @Describe
 * @Version 1.0
 **/
@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    private PmsProductInfoMapper pmsProductInfoMapper;

    @Autowired
    private PmsProductImageMapper pmsProductImageMapper;

    @Autowired
    private PmsProductSaleAttrMapper pmsProductSaleAttrMapper;

    @Autowired
    private PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;

    @Override
    public List<PmsProductInfo> spuList(String catalog3Id) {
        PmsProductInfo pmsProductInfo = new PmsProductInfo();
        pmsProductInfo.setCatalog3Id(catalog3Id);
        return pmsProductInfoMapper.select(pmsProductInfo);
    }

    @Override
    public void saveSpuInfo(PmsProductInfo pmsProductInfo) {
        /*保存spu信息*/
        pmsProductInfoMapper.insert(pmsProductInfo);
        String productId = pmsProductInfo.getId();
        List<PmsProductImage> spuImageList = pmsProductInfo.getSpuImageList();

        /*保存图片信息*/
        for (PmsProductImage image:spuImageList) {
            image.setProductId(productId);
            pmsProductImageMapper.insert(image);
        }

        /*保存销售属性信息*/
        List<PmsProductSaleAttr> spuSaleAttrList = pmsProductInfo.getSpuSaleAttrList();
        for(PmsProductSaleAttr pmsProductSaleAttr : spuSaleAttrList){
            pmsProductSaleAttr.setProductId(productId);
            pmsProductSaleAttrMapper.insert(pmsProductSaleAttr);

            /*保存销售属性的值信息*/
            List<PmsProductSaleAttrValue> spuSaleAttrValueList = pmsProductSaleAttr.getSpuSaleAttrValueList();
            for(PmsProductSaleAttrValue pmsProductSaleAttrValue:spuSaleAttrValueList){
                pmsProductSaleAttrValue.setProductId(productId);
                pmsProductSaleAttrValueMapper.insert(pmsProductSaleAttrValue);
            }
        }
    }
}
