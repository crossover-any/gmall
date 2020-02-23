package com.gmall.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.gmall.bean.UmsMember;
import com.gmall.bean.UmsMemberReceiveAddress;
import com.gmall.service.UserService;
import com.gmall.user.mapper.UserAddressMapper;
import com.gmall.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author Tengxq
 * @Date 2020/2/21 13:32
 * @Describe
 * @Version 1.0
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Override
    public List<UmsMemberReceiveAddress> getReceiveAddressByUser(UmsMemberReceiveAddress address) {
        return userAddressMapper.select(address);
    }

    @Override
    public List<UmsMember> getAllUser() {
        return userMapper.selectAll();
    }
}
