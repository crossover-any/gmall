package com.gmall.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gmall.bean.PmsProductSaleAttr;
import com.gmall.bean.PmsSkuInfo;
import com.gmall.service.SkuService;
import com.gmall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Author Tengxq
 * @Date 2020/2/26 11:48
 * @Describe
 * @Version 1.0
 **/

@Controller
public class ItemController {

    @Reference
    private SkuService skuService;

    @Reference
    private SpuService spuService;

    @RequestMapping("/{skuId}.html")
    public String index(@PathVariable String skuId, ModelMap modelMap){
        PmsSkuInfo pmsSkuInfo = skuService.getPmsSkuInfo(skuId);
        List<PmsProductSaleAttr> spuSaleAttrListCheckBySku = spuService.spuSaleAttrListCheckBySku(pmsSkuInfo.getProductId(),skuId);
        modelMap.addAttribute("skuInfo",pmsSkuInfo);
        modelMap.addAttribute("spuSaleAttrListCheckBySku",spuSaleAttrListCheckBySku);
        return "item";
    }
}
