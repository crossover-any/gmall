package gmall.user.service;

import gmall.user.bean.UmsMember;
import gmall.user.bean.UmsMemberReceiveAddress;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Tengxq
 * @Date 2020/2/21 13:31
 * @Describe
 * @Version 1.0
 **/
public interface UserService {
    List<UmsMember> getAllUser();

    List<UmsMemberReceiveAddress> getReceiveAddressByUser(UmsMemberReceiveAddress address);
}
