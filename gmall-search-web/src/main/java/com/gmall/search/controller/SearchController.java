package com.gmall.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gmall.bean.*;
import com.gmall.service.AttrService;
import com.gmall.service.SearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.channels.FileChannel;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author Tengxq
 * @Date 2020/3/2 10:59
 * @Describe
 * @Version 1.0
 **/

@Controller
@CrossOrigin
public class SearchController {

    @Reference
    private SearchService searchService;


    @Reference
    private AttrService attrService;


    @RequestMapping("index")
    public String index(){
        return "index";
    }

    @RequestMapping("/list.html")
    public String list(PmsSearchParam pmsSearchParam, ModelMap modelMap){
        //调用搜索服务，返回搜索结果
        List<PmsSearchSkuInfo> list = searchService.getSearchInfo(pmsSearchParam);
        Set<String> set = new HashSet<>();
        for (PmsSearchSkuInfo pmsSearchSkuInfo : list) {
            List<PmsSkuAttrValue> skuAttrValueList = pmsSearchSkuInfo.getSkuAttrValueList();
            for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
                set.add(pmsSkuAttrValue.getValueId());
            }
        }
        List<PmsBaseAttrInfo> saleAttrs = attrService.getAttrValueListByValueId(set);
        modelMap.addAttribute("skuLsInfoList",list);
        modelMap.addAttribute("attrList",saleAttrs);
        return "list";
    }

    @RequestMapping("/updateSkuInfo")
    @ResponseBody
    public String updateSkuInfo(){
        searchService.updateSkuInfoData();
        return "success";
    }
}
