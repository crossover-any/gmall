package gmall.user.service.impl;

import gmall.user.bean.UmsMember;
import gmall.user.mapper.UserMapper;
import gmall.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public List<UmsMember> getAllUser() {
        return userMapper.selectAll();
    }
}
