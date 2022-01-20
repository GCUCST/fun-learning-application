package cn.cst.myspringboot.dao;

import cn.cst.myspringboot.pojo.User;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public interface UserDao {
  Boolean saveUser(User user);

  Integer updateUserPassword(User user);

  Integer deleteUser(String username);

  User selectUser(String username);

  List<User> findAllUser();
}
