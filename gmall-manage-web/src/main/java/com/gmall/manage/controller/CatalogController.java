package com.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gmall.bean.*;
import com.gmall.service.AttrInfoService;
import com.gmall.service.CatalogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author Tengxq
 * @Date 2020/2/23 17:51
 * @Describe
 * @Version 1.0
 **/

@RestController
@CrossOrigin
public class CatalogController {

    @Reference
    private CatalogService catalogService;

    @Reference
    private AttrInfoService attrInfoService;

    @RequestMapping("/getCatalog1")
    public List<PmsBaseCatalog1> getCatalog1(){
        return catalogService.selectCatalog1All();
    }

    @RequestMapping("/getCatalog2")
    public List<PmsBaseCatalog2> getCatalog2(String catalog1Id){
        PmsBaseCatalog2 pmsBaseCatalog2 = new PmsBaseCatalog2();
        pmsBaseCatalog2.setCatalog1Id(catalog1Id);
        return catalogService.selectCatalog2ByCatalog1Id(pmsBaseCatalog2);
    }

    @RequestMapping("/getCatalog3")
    public List<PmsBaseCatalog3> getCatalog3(String catalog2Id){
        PmsBaseCatalog3 pmsBaseCatalog3 = new PmsBaseCatalog3();
        pmsBaseCatalog3.setCatalog2Id(catalog2Id);
        return catalogService.selectCatalog3ByCatalog2Id(pmsBaseCatalog3);
    }

    @RequestMapping("/attrInfoList")
    public List<PmsBaseAttrInfo> getAttrInfoList(String catalog3Id){
        return attrInfoService.getInfoList(catalog3Id);
    }

    @RequestMapping("/saveAttrInfo")
    public String addAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo){
        return attrInfoService.addAttrInfo(pmsBaseAttrInfo);
    }

    @RequestMapping("/getAttrValueList")
    public List<PmsBaseAttrValue> getAttrValueList(String attrId){
        return attrInfoService.getAttrValueList(attrId);
    }
}
