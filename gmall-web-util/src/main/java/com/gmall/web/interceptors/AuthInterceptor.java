package com.gmall.web.interceptors;

import com.alibaba.fastjson.JSON;
import com.gmall.common.util.HttpclientUtil;
import com.gmall.web.annotations.LoginRequired;
import com.gmall.web.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Tengxq
 * @Date 2020/3/11 11:45
 * @Describe
 * @Version 1.0
 **/

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println(request.getRequestURL());
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        LoginRequired methodAnnotation = handlerMethod.getMethodAnnotation(LoginRequired.class);
        //对登录状态无限制
        if (methodAnnotation == null) {
            return true;
        } else {
            //具有登录状态判断
            String oldToken = CookieUtil.getCookieValue(request, "oldToken", true);
            String newToken = request.getParameter("token");
            String token = "";
            if (StringUtils.isNotBlank(oldToken)) {
                token = oldToken;
            }
            if (StringUtils.isNotBlank(newToken)) {
                token = newToken;
            }
            boolean loginSuccess = methodAnnotation.loginSuccess();
            String statusStr = "failed";
            Map<String,String> map = new HashMap<>();
            if (StringUtils.isNotBlank(token)){
                statusStr = HttpclientUtil.doGet("http://localhost:10052/verify?token=" + token+"&curIp="+request.getRemoteAddr());
            }
            //需要登录后才能访问
            if (loginSuccess) {
                //验证token
                if (StringUtils.isNotBlank(token) && !"failed".equals(statusStr)) {
                        map = JSON.parseObject(statusStr,Map.class);
                        request.setAttribute("userId",map.get("userId"));
                        request.setAttribute("nickName",map.get("nickName"));
                } else {
                    StringBuffer requestURL = request.getRequestURL();
                    response.sendRedirect("http://passport.gmall.com:10052/index?returnURL="+requestURL.toString());
                    return false;
                }
            }else{
               return true;
            }
            return true;
        }
    }
}
