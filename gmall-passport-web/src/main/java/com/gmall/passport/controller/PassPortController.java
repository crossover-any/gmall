package com.gmall.passport.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.gmall.bean.UmsMember;
import com.gmall.service.UserService;
import com.gmall.web.util.CookieUtil;
import com.gmall.web.util.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public String login(UmsMember umsMember, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response){
        String token = "token";
        if (umsMember == null || umsMember.getUsername() == null || umsMember.getPassword() == null){

        }else{
            UmsMember user = userService.getUser(umsMember);
            if (user == null){
                System.out.println("登录失败");
            }else{
                System.out.println("登录成功");

                //用JWT制作token
                Map<String,Object> userMap = new HashMap<>();
                userMap.put("memberId",user.getId());
                userMap.put("nickName",user.getNickname());
                //如果是nginx代理则得到请求的真实路径
                String ip = "";
                //request.getHeader("x-forwarded-for");
                if (StringUtils.isBlank(ip)){
                    ip = request.getRemoteAddr();
                }
                if (StringUtils.isBlank(ip)){
                    ip = "127.0.0.1";
                }
                token = JwtUtil.encode("gmall",userMap,ip);
                userService.addUserToken(user.getId(),token);
                if (StringUtils.isNotBlank(token)){
                    CookieUtil.setCookie(request,response,"oldToken",token,3600*2,true);
                }
            }
        }
        return token;
    }

    @RequestMapping("/verify")
    @ResponseBody
    public String verify(HttpServletRequest request,String token,String curIp){
        String ip = request.getRemoteAddr();
        if (StringUtils.isNotBlank(curIp)){
            ip = curIp;
        }
        Map<String,Object> userMap = JwtUtil.decode(token, "gmall", ip);
        return userMap==null?"failed": JSON.toJSONString(userMap);
    }
}
