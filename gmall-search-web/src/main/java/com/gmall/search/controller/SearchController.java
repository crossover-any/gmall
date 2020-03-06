package com.gmall.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gmall.bean.*;
import com.gmall.service.AttrService;
import com.gmall.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

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
        saleAttrs = removeSelectedAttr(saleAttrs,pmsSearchParam.getValueId());
        String urlParam = getUrlParam(pmsSearchParam);
        modelMap.addAttribute("urlParam",urlParam);
        modelMap.addAttribute("skuLsInfoList",list);
        modelMap.addAttribute("attrList",saleAttrs);
        return "list";
    }

    private List<PmsBaseAttrInfo> removeSelectedAttr(List<PmsBaseAttrInfo> saleAttrs,String[] valueIds) {
        if (valueIds != null){
            Iterator<PmsBaseAttrInfo> iterator = saleAttrs.iterator();
            HashSet<String> valueSet = new HashSet<>();
            for (String valueId : valueIds) {
                valueSet.add(valueId);
            }
            while (iterator.hasNext()){
                PmsBaseAttrInfo pmsBaseAttrInfo = iterator.next();
                List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
                for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                    if (valueSet.contains(pmsBaseAttrValue.getId())){
                        iterator.remove();
                    }
                }
            }
        }
        return saleAttrs;
    }

    private String getUrlParam(PmsSearchParam pmsSearchParam) {
        String[] valueIds = pmsSearchParam.getValueId();
        String urlParam = "";
        String keyWord = pmsSearchParam.getKeyword();
        String catalog3Id = pmsSearchParam.getCatalog3Id();
        if (StringUtils.isNotBlank(keyWord)) {
            if (StringUtils.isNotBlank(catalog3Id)) {
                urlParam += "&";
            }
            urlParam+="keyword="+keyWord;
        }
        if (StringUtils.isNotBlank(catalog3Id)) {
            if (StringUtils.isNotBlank(keyWord)) {
                urlParam += "&";
            }
            urlParam+="catalog3Id="+catalog3Id;
        }
        if(valueIds != null){
            for (String valueId : valueIds) {
                urlParam+="&valueId="+valueId;
            }
        }
        return urlParam;
    }

    @RequestMapping("/updateSkuInfo")
    @ResponseBody
    public String updateSkuInfo(){
        searchService.updateSkuInfoData();
        return "success";
    }
}
