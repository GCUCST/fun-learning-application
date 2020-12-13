package cn.cst.myspringboot.dao;

import cn.cst.myspringboot.pojo.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserDao {
    Boolean saveUser(User user);

     Integer updateUserPassword(User user);

     Integer deleteUser(String username);

     User selectUser(String username);

     List<User> findAllUser();
}
