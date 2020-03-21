package com.gmall.passport.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gmall.bean.UmsMember;
import com.gmall.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Tengxq
 * @Date 2020/3/11 11:15
 * @Describe
 * @Version 1.0
 **/

@Controller
public class PassPortController {

    @Reference
    private UserService userService;

    @RequestMapping("/index")
    public String index(String returnURL,ModelMap modelMap){
        if (StringUtils.isNotBlank(returnURL)){
            modelMap.addAttribute("returnURL",returnURL);
        }
        return "index";
    }

    @RequestMapping("/login")
    @ResponseBody
    public String login(UmsMember umsMember, ModelMap modelMap, HttpServletRequest request){
        if (umsMember == null || umsMember.getUsername() == null || umsMember.getPassword() == null){

        }else{
            UmsMember user = userService.getUser(umsMember);
            if (user == null){
                System.out.println("登录失败");
            }else{
                System.out.println("登录成功");

                //用JWT制作token
                String userId = user.getId();
                String nickName = user.getNickname();
                Map<String,Object> userMap = new HashMap<>();
                userMap.put("memberId",userId);
                userMap.put("nickname",nickName);

                //如果是nginx代理则得到请求的真实路径
                String ip = request.getHeader("x-forwarded-for");
                if (StringUtils.isBlank(ip)){
                    ip = request.getRemoteAddr();
                }
            }
        }
        return "token";
    }

    @RequestMapping("/verify")
    @ResponseBody
    public String verify(String token){
        return "success";
    }
}
