package com.gmall.web.interceptors;

import com.gmall.common.util.HttpclientUtil;
import com.gmall.web.annotations.LoginRequired;
import com.gmall.web.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
            String success = "failed";
            if (StringUtils.isNotBlank(token)){
                success = HttpclientUtil.doGet("http://localhost:10052/verify?token=" + token);
            }
            //需要登录后才能访问
            if (loginSuccess) {
                //验证token
                if (StringUtils.isNotBlank(token) && "success".equals(success)) {
                    request.setAttribute("userId","1");
                    request.setAttribute("nickName","jerry");
                } else {
                    StringBuffer requestURL = request.getRequestURL();
                    response.sendRedirect("http://localhost:10052/index?returnURL="+requestURL.toString());
                    return false;
                }
            }else{
               return true;
            }

            if (StringUtils.isNotBlank(token)){
                CookieUtil.setCookie(request,response,"oldToken",token,3600*2,true);
            }
            return true;
        }
    }
}
