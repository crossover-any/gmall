package com.gmall.service;


import com.gmall.bean.OmsCartItem;

import java.util.List;

public interface CartService {
    OmsCartItem ifCartExist(String userId,String skuId);

    void updateCart(OmsCartItem cartInfoDb);

    void insertCart(OmsCartItem cartInfo);

    void syncCache(String userId);

    List<OmsCartItem> getCartCache(String userId);

    void updateCartChecked(OmsCartItem cartInfo);

    void combineCart(List<OmsCartItem> cartInfos, String userId);

    List<OmsCartItem> getCartCacheByChecked(String userId);

    void deleteCartById(List<OmsCartItem> cartInfos);
}
