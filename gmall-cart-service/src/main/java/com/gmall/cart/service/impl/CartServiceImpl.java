package com.gmall.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gmall.bean.OmsCartItem;
import com.gmall.cart.mapper.OmsCartItemMapper;
import com.gmall.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Tengxq
 * @Date 2020/3/7 21:40
 * @Describe
 * @Version 1.0
 **/

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private OmsCartItemMapper omsCartItemMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public OmsCartItem ifCartExist(String userId,String skuId) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(userId);
        omsCartItem.setProductSkuId(skuId);
        return omsCartItemMapper.selectOne(omsCartItem);
    }

    @Override
    public void updateCart(OmsCartItem cartInfoDb) {
        Example e = new Example(OmsCartItem.class);
        e.createCriteria().andEqualTo("memberId",cartInfoDb.getMemberId()).andEqualTo("productSkuId",cartInfoDb.getProductSkuId());
        omsCartItemMapper.updateByExampleSelective(cartInfoDb,e);
        syncCache(cartInfoDb.getMemberId());
    }

    @Override
    public void insertCart(OmsCartItem cartInfo) {
        omsCartItemMapper.insert(cartInfo);
    }

    @Override
    public void syncCache(String userId,List<OmsCartItem> omsCartItems) {
     Map<String,String> map = new HashMap<>();
     if (omsCartItems != null){
         for (OmsCartItem cartItem : omsCartItems) {
             cartItem.setTotalPrice(cartItem.getPrice().multiply(cartItem.getQuantity()));
             map.put(cartItem.getProductSkuId(), JSON.toJSONString(cartItem));
         }
     }
        redisTemplate.opsForHash().putAll("user:"+userId+":cart",map);
    }

    public void syncCache(String userId) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(userId);
        List<OmsCartItem> omsCartItemList = omsCartItemMapper.select(omsCartItem);
        Map<String,String> map = new HashMap<>();
        if (omsCartItemList != null){
            for (OmsCartItem cartItem : omsCartItemList) {
                cartItem.setTotalPrice(cartItem.getPrice().multiply(cartItem.getQuantity()));
                map.put(cartItem.getProductSkuId(), JSON.toJSONString(cartItem));
            }
        }
        redisTemplate.opsForHash().putAll("user:"+userId+":cart",map);
    }

    @Override
    public List<OmsCartItem> getCartCache(String userId) {
        HashOperations<String, Object, Object> stringObjectObjectHashOperations = redisTemplate.opsForHash();
        Map<Object, Object> map = stringObjectObjectHashOperations.entries("user:"+userId+":cart");
        List<OmsCartItem> omsCartItemList = new ArrayList<>();
        if (map != null){
            for (Map.Entry<Object, Object> mapEntry : map.entrySet()) {
                OmsCartItem omsCartItem = JSONObject.parseObject(mapEntry.getValue().toString(),OmsCartItem.class);
                omsCartItemList.add(omsCartItem);
            }
        }
        return omsCartItemList;
    }

    @Override
    public List<OmsCartItem> cartList(String userId) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(userId);
        List<OmsCartItem> cartItems = getCartCache(userId);
        if (cartItems == null){
            cartItems = omsCartItemMapper.select(omsCartItem);
            syncCache(userId,cartItems);
        }
        return cartItems;
    }
}
