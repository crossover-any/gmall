package com.gmall.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gmall.bean.PmsSearchParam;
import com.gmall.bean.PmsSearchSkuInfo;
import com.gmall.service.SearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.channels.FileChannel;
import java.util.List;

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


    @RequestMapping("index")
    public String index(){
        return "index";
    }

    @RequestMapping("/list.html")
    public String list(PmsSearchParam pmsSearchParam, ModelMap modelMap){
        //调用搜索服务，返回搜索结果
        List<PmsSearchSkuInfo> list = searchService.getSearchInfo(pmsSearchParam);
        modelMap.addAttribute("skuLsInfoList",list);
        return "list";
    }

    @RequestMapping("/updateSkuInfo")
    @ResponseBody
    public String updateSkuInfo(){
        searchService.updateSkuInfoData();
        return "success";
    }
}
