package com.gmall.passport.controller;

import com.gmall.bean.UmsMember;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author Tengxq
 * @Date 2020/3/11 11:15
 * @Describe
 * @Version 1.0
 **/

@Controller
public class PassPortController {

    @RequestMapping("/index")
    public String index(String returnURL,ModelMap modelMap){
        if (StringUtils.isNotBlank(returnURL)){
            modelMap.addAttribute("returnURL",returnURL);
        }
        return "index";
    }

    @RequestMapping("/login")
    @ResponseBody
    public String login(UmsMember umsMember, ModelMap modelMap){
        return "token";
    }

    @RequestMapping("/verify")
    @ResponseBody
    public String verify(String token){
        return "success";
    }
}
