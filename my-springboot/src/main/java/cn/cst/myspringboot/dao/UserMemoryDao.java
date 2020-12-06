package cn.cst.myspringboot.dao;

import cn.cst.myspringboot.pojo.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserMemoryDao implements UserDao {
    static List<User> userList;

    static {
        userList = new ArrayList<>();
        userList.add(User.builder().username("张一").password("123").build());
        userList.add(User.builder().username("张二").password("123").build());
        userList.add(User.builder().username("张三").password("123").build());
        userList.add(User.builder().username("张四").password("123").build());
        userList.add(User.builder().username("张五").password("123").build());
    }

    public Boolean saveUser(User user) {
        return userList.add(user);
    }

    public Integer updateUserPassword(User user) {
        Integer succ = 0;
        for (User u : userList) {
            if (u.getUsername().equals(user.getUsername())) {
                u.setPassword(user.getPassword());
                succ = 1;
            }
        }
        return succ;
    }

    @Override
    public Integer deleteUser(String username) {
        User targetUser = null;
        for (User u : userList) {
            if (u.getUsername().equals(username)) {
                targetUser = u;
            }
        }
        boolean remove = userList.remove(targetUser);
        if (remove)
            return 1;
        else return 0;
    }

    @Override
    public User selectUser(String username) {
        for (User u : userList) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public List<User> findAllUser() {
        return userList;
    }

//    public static void main(String[] args) {
//        List<User> list  = new ArrayList<>();
//        list.add(User.builder().username("张一").password("123").build());
//        list.add(User.builder().username("张2").password("123").build());
//        for (User s: list) {
//            s.setPassword("5555");
//        }
//        System.out.println(list
//        );
//    }

}
