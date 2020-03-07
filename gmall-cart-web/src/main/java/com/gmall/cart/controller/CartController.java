package com.gmall.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.gmall.bean.OmsCartItem;
import com.gmall.bean.PmsSkuInfo;
import com.gmall.service.CartService;
import com.gmall.service.SkuService;
import com.gmall.web.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author Tengxq
 * @Date 2020/3/7 15:37
 * @Describe
 * @Version 1.0
 **/

@Controller
@CrossOrigin
public class CartController {

    @Reference
    private SkuService skuService;

    @Reference
    private CartService cartService;

    @RequestMapping("/addToCart")
    public String toCartPage(String skuId, int quantity, HttpServletRequest request, HttpServletResponse response){
        if (StringUtils.isBlank(skuId)){
            return "error";
        }
        List<OmsCartItem> cartInfos = new ArrayList<>();
        PmsSkuInfo pmsSkuInfo = skuService.getPmsSkuInfo(skuId);
        String userId = "1";
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setCreateDate(new Date());
        omsCartItem.setDeleteStatus(0);
        omsCartItem.setModifyDate(new Date());
        omsCartItem.setPrice(pmsSkuInfo.getPrice());
        omsCartItem.setProductAttr(JSON.toJSONString(pmsSkuInfo.getSkuAttrValueList()));
        omsCartItem.setProductCategoryId(pmsSkuInfo.getCatalog3Id());
        omsCartItem.setProductPic(pmsSkuInfo.getSkuDefaultImg());
        omsCartItem.setProductId(pmsSkuInfo.getProductId());
        omsCartItem.setProductName(pmsSkuInfo.getSkuName());
        omsCartItem.setProductSkuId(skuId);

        if (StringUtils.isNotBlank(userId)){
            OmsCartItem oms = cartService.ifCartExist(userId, skuId);
            if (oms == null){
                omsCartItem.setQuantity(new BigDecimal(quantity));
                omsCartItem.setMemberId(userId);
                cartService.insertCart(omsCartItem);
            }else{

            }
        }else{
            //用户未登录
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            //如果有缓存，更新缓存
            if (StringUtils.isNotBlank(cartListCookie)){
                cartInfos = JSON.parseArray(cartListCookie,OmsCartItem.class);
                boolean isNewOrder = true;
                for (OmsCartItem cartInfo : cartInfos) {
                    if (skuId.equals(cartInfo.getProductSkuId())){
                        cartInfo.setPrice(cartInfo.getPrice().add(pmsSkuInfo.getPrice()));
                        cartInfo.setQuantity(cartInfo.getQuantity().add(new BigDecimal(quantity)));
                        isNewOrder = false;
                        break;
                    }
                }
                if (isNewOrder){
                    cartInfos.add(omsCartItem);
                }
            }
            CookieUtil.setCookie(request,response,"cartListCookie",JSON.toJSONString(cartInfos),3600*72,true);
        }



        return "redirect:/success.html";
    }
}
