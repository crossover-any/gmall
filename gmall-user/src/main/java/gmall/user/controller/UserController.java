package gmall.user.controller;

import gmall.user.bean.UmsMember;
import gmall.user.bean.UmsMemberReceiveAddress;
import gmall.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author Tengxq
 * @Date 2020/2/21 13:31
 * @Describe
 * @Version 1.0
 **/

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/index")
    @ResponseBody
    public List<UmsMember> index(){
        return userService.getAllUser();
    }

    @RequestMapping("/address")
    @ResponseBody
    public List<UmsMemberReceiveAddress> addresses(String memberid){
        if (StringUtils.isEmpty(memberid)){
            return null;
        }
        UmsMemberReceiveAddress address =  new UmsMemberReceiveAddress();
        address.setMemberId(memberid);
        List<UmsMemberReceiveAddress> addresses = userService.getReceiveAddressByUser(address);
        return addresses;
    }
}
