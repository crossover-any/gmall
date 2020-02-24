package com.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gmall.bean.PmsBaseSaleAttr;
import com.gmall.bean.PmsProductSaleAttr;
import com.gmall.service.AttrService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author Tengxq
 * @Date 2020/2/24 18:14
 * @Describe
 * @Version 1.0
 **/

@RestController
@CrossOrigin
public class AttrController {

    @Reference
    private AttrService attrService;


    @RequestMapping("/baseSaleAttrList")
    public List<PmsBaseSaleAttr> baseSaleAttrList(){
        List list = attrService.baseSaleAttrList();
        return list;

    }
}
