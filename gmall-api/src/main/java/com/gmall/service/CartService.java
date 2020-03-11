package com.gmall.service;


import com.gmall.bean.OmsCartItem;

import java.util.List;

public interface CartService {
    OmsCartItem ifCartExist(String userId,String skuId);

    void updateCart(OmsCartItem cartInfoDb);

    void insertCart(OmsCartItem cartInfo);

    void syncCache(String userId,List<OmsCartItem> omsCartItems);

    List<OmsCartItem> getCartCache(String userId);

    List<OmsCartItem> cartList(String userId);
}
