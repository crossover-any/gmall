package gmall.user.service;

import com.gmall.bean.UmsMemberReceiveAddress;
import com.gmall.bean.UmsMember;
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
