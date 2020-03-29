package com.gmall.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gmall.bean.OmsCartItem;
import com.gmall.bean.PmsSkuInfo;
import com.gmall.service.CartService;
import com.gmall.service.SkuService;
import com.gmall.web.annotations.LoginRequired;
import com.gmall.web.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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

    @RequestMapping("/cartList")
    public String cartListPage(HttpServletRequest request,ModelMap modelMap){
        String userId = "1";
        String page = "cartList";
        List<OmsCartItem> omsCartItemList = null;
        //用户已经登陆
        if (StringUtils.isNotBlank(userId)){
            omsCartItemList = cartService.cartList(userId);
        }else{
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            omsCartItemList = JSON.parseArray(cartListCookie,OmsCartItem.class);
        }
        modelMap.addAttribute("cartList",omsCartItemList);
        modelMap.addAttribute("totalCount",getTotalCount(omsCartItemList));
        return page;
    }

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
                //用户没有添加过此商品
                omsCartItem.setQuantity(new BigDecimal(quantity));
                omsCartItem.setMemberId(userId);
                cartService.insertCart(omsCartItem);
            }else{
                //用户添加过此商品
                oms.setQuantity(oms.getQuantity().add(new BigDecimal(quantity)));
                cartService.updateCart(oms);
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

    @RequestMapping("/checkCart")
    public String checkCart(String skuId,String isChecked,HttpServletRequest request, HttpServletResponse response,ModelMap map){
        if (StringUtils.isBlank(skuId) || StringUtils.isBlank(isChecked)){
            return "error";
        }
        String userId = (String) request.getAttribute("userId");
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId("1");
        omsCartItem.setProductSkuId(skuId);
        omsCartItem.setIsChecked(isChecked);
        List<OmsCartItem> omsCartItemList = null;
        if (StringUtils.isNotBlank(userId)){
            //用户登录，更新数据库并从缓存中查找数据
            cartService.updateCart(omsCartItem);
            omsCartItemList = cartService.getCartCache(userId);
        }else{
            //用户未登录，更新Cookie中数据
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            omsCartItemList = JSONObject.parseArray(cartListCookie,OmsCartItem.class);
            for (OmsCartItem cartItem : omsCartItemList) {
                if (skuId.equals(cartItem.getProductSkuId())){
                    cartItem.setIsChecked(isChecked);
                    break;
                }
            }
            CookieUtil.setCookie(request,response,"cartListCookie",JSON.toJSONString(omsCartItemList),3600*72,true);
        }
        map.addAttribute("cartList",omsCartItemList);
        map.addAttribute("totalCount",getTotalCount(omsCartItemList));
        return "cartListInner";
    }

    @RequestMapping("/toTrade")
    @LoginRequired
    public String toTrade(HttpServletRequest request){
        String memberId = (String) request.getAttribute("memberId");
        String nickName = (String) request.getAttribute("nickName");
        System.out.println(memberId+nickName);
        return "toTrade";
    }

    public BigDecimal getTotalCount(List<OmsCartItem> omsCartItems){
        BigDecimal total = new BigDecimal(0).setScale(2,BigDecimal.ROUND_HALF_DOWN);
        if (omsCartItems != null){
            for (OmsCartItem omsCartItem : omsCartItems) {
                if ("1".equals(omsCartItem.getIsChecked())){
                    total = total.add(omsCartItem.getTotalPrice());
                }
            }
        }
        return total;
    }
}
