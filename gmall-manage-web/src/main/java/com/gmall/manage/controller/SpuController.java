package com.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gmall.bean.PmsProductInfo;
import com.gmall.service.SpuService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author Tengxq
 * @Date 2020/2/24 17:13
 * @Describe
 * @Version 1.0
 **/

@RestController
@CrossOrigin
public class SpuController {

    @Reference
    private SpuService spuService;

    @RequestMapping("/spuList")
    public List<PmsProductInfo> spuList(String catalog3Id){
        return spuService.spuList(catalog3Id);
    }
}
