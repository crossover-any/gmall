package com.gmall.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gmall.bean.UmsMember;
import com.gmall.bean.UmsMemberReceiveAddress;
import com.gmall.service.UserService;
import com.gmall.user.mapper.UserAddressMapper;
import com.gmall.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

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

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public List<UmsMemberReceiveAddress> getReceiveAddressByUser(UmsMemberReceiveAddress address) {
        return userAddressMapper.select(address);
    }

    @Override
    public UmsMember getUser(UmsMember umsMember) {
        String key = "user:"+umsMember.getUsername()+umsMember.getPassword()+":password";
        UmsMember dbUser = null;
        if (redisTemplate != null){
            dbUser = JSON.parseObject(redisTemplate.opsForValue().get(key),UmsMember.class);
            if (dbUser == null){
                dbUser = userMapper.selectByPrimaryKey(umsMember.getId());
                if (dbUser!= null && dbUser.getPassword().equals(umsMember.getPassword())){
                    redisTemplate.opsForValue().set("user:"+dbUser.getUsername()+dbUser.getPassword()+":password",JSON.toJSONString(dbUser));
                    return dbUser;
                }
            }
        }else{
            dbUser = userMapper.selectByPrimaryKey(umsMember.getId());
            if (dbUser!= null && dbUser.getPassword().equals(umsMember.getPassword())){
                redisTemplate.opsForValue().set("user:"+dbUser.getUsername()+dbUser.getPassword()+":password",JSON.toJSONString(dbUser));
                return dbUser;
            }
        }
        return null;
    }

    @Override
    public List<UmsMember> getAllUser() {
        return userMapper.selectAll();
    }
}
