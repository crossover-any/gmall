package gmall.user.mapper;

import gmall.user.bean.UmsMember;

import java.util.List;

public interface UserMapper {
    List<UmsMember> selectAll();
}
