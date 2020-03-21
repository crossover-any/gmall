package gmall.user.service.impl;

import com.gmall.bean.UmsMemberReceiveAddress;
import com.gmall.bean.UmsMember;
import com.gmall.service.UserService;
import gmall.user.mapper.UserAddressMapper;
import gmall.user.mapper.UserMapper;
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

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Override
    public List<UmsMemberReceiveAddress> getReceiveAddressByUser(UmsMemberReceiveAddress address) {
        return userAddressMapper.select(address);
    }

    @Override
    public UmsMember getUser(UmsMember umsMember) {
        return null;
    }

    @Override
    public void addUserToken(String userId, String token) {

    }

    @Override
    public List<UmsMember> getAllUser() {
        return userMapper.selectAll();
    }
}
