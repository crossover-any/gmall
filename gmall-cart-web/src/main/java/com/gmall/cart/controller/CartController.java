package com.gmall.cart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author Tengxq
 * @Date 2020/3/7 15:37
 * @Describe
 * @Version 1.0
 **/

@Controller
@CrossOrigin
public class CartController {

    @RequestMapping("/addToCart")
    public String toCartPage(){
        return "redirect:/success.html";
    }
}
