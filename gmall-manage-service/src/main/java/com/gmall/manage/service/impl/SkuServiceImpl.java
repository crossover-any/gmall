package com.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.gmall.bean.PmsSkuAttrValue;
import com.gmall.bean.PmsSkuImage;
import com.gmall.bean.PmsSkuInfo;
import com.gmall.bean.PmsSkuSaleAttrValue;
import com.gmall.manage.mapper.PmsSkuAttrValueMapper;
import com.gmall.manage.mapper.PmsSkuImageMapper;
import com.gmall.manage.mapper.PmsSkuInfoMapper;
import com.gmall.manage.mapper.PmsSkuSaleAttrValueMapper;
import com.gmall.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author Tengxq
 * @Date 2020/2/26 1:00
 * @Describe
 * @Version 1.0
 **/

@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private PmsSkuInfoMapper pmsSkuInfoMapper;

    @Autowired
    private PmsSkuImageMapper pmsSkuImageMapper;

    @Autowired
    private PmsSkuAttrValueMapper pmsSkuAttrValueMapper;

    @Autowired
    private PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    @Override
    public void saveSkuInfo(PmsSkuInfo pmsSkuInfo) {
        int i = pmsSkuInfoMapper.insert(pmsSkuInfo);
        String skuId = pmsSkuInfo.getId();

        List<PmsSkuImage> images = pmsSkuInfo.getSkuImageList();
        for (PmsSkuImage image: images) {
            image.setSkuId(skuId);
            pmsSkuImageMapper.insert(image);
        }

        List<PmsSkuAttrValue> attrValues = pmsSkuInfo.getSkuAttrValueList();
        for (PmsSkuAttrValue attrValue:attrValues) {
            attrValue.setSkuId(skuId);
            pmsSkuAttrValueMapper.insert(attrValue);
        }

        List<PmsSkuSaleAttrValue> saleAttrValues = pmsSkuInfo.getSkuSaleAttrValueList();
        for (PmsSkuSaleAttrValue saleAttrValue: saleAttrValues) {
            saleAttrValue.setSkuId(skuId);
            pmsSkuSaleAttrValueMapper.insert(saleAttrValue);
        }
    }

    @Override
    public PmsSkuInfo getPmsSkuInfo(String skuId) {
        String key = "sku"+skuId+"info";
        String skuInfoJSonStr = redisTemplate.opsForValue().get(key);
        PmsSkuInfo pmsSkuInfo = null;
        if (StringUtils.isEmpty(skuInfoJSonStr)){
            pmsSkuInfo = pmsSkuInfoMapper.selectByPrimaryKey(skuId);
            if (pmsSkuInfo == null){
                return null;
            }
            PmsSkuImage image = new PmsSkuImage();
            image.setSkuId(skuId);
            List<PmsSkuImage> images = pmsSkuImageMapper.select(image);
            pmsSkuInfo.setSkuImageList(images);
            redisTemplate.opsForValue().set(key, JSON.toJSONString(pmsSkuInfo));
        }else{
            pmsSkuInfo = JSON.parseObject(skuInfoJSonStr,PmsSkuInfo.class);
        }
        return pmsSkuInfo;
    }
}
