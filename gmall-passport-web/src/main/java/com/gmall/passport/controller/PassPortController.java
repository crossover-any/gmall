package com.gmall.passport.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author Tengxq
 * @Date 2020/3/11 11:15
 * @Describe
 * @Version 1.0
 **/

@Controller
public class PassPortController {

    @RequestMapping("/index")
    public String index(){
        return "index";
    }
}
