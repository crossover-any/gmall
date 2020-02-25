package com.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gmall.bean.PmsProductInfo;
import com.gmall.manage.util.FastDFSUtil;
import com.gmall.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private FastDFSUtil fastDFSUtil;

    @Reference
    private SpuService spuService;

    @RequestMapping("/spuList")
    public List<PmsProductInfo> spuList(String catalog3Id){
        return spuService.spuList(catalog3Id);
    }

    @RequestMapping("/saveSpuInfo")
    public String saveSpuInfo(@RequestBody PmsProductInfo pmsProductInfo){
        spuService.saveSpuInfo(pmsProductInfo);
        return "success";
    }

    @RequestMapping("fileUpload")
    public String fileUpload(@RequestParam("file")MultipartFile multipartFile){
        String url = fastDFSUtil.uploadImg(multipartFile);
        return url;
    }

}
