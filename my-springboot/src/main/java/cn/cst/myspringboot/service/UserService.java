package cn.cst.myspringboot.service;

import cn.cst.myspringboot.dao.UserDao;
import cn.cst.myspringboot.pojo.User;
import cn.cst.myspringboot.util.CommonResponse;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class UserService {

    private final UserDao userMemoryDao;

    public UserService(UserDao userMemoryDao) {
        this.userMemoryDao = userMemoryDao;
    }

    public CommonResponse saveUser(User user) {
        Boolean succ = userMemoryDao.saveUser(user);
        if (succ)
            return CommonResponse.builder().code(200).msg("success").playload(succ).time(LocalTime.now()).build();
        else
            return CommonResponse.builder().code(400).msg("save fail").playload(null).time(LocalTime.now()).build();
    }

    public CommonResponse getUser(String username) {
        User user = userMemoryDao.selectUser(username);
        if (user != null)
            return CommonResponse.builder().code(200).msg("success").playload(user).time(LocalTime.now()).build();
        else
            return CommonResponse.builder().code(404).msg("not found").playload(null).time(LocalTime.now()).build();
    }

    public CommonResponse getAllUser() {
        List<User> allUser = userMemoryDao.findAllUser();
        return CommonResponse.builder().code(200).msg("success").playload(allUser).time(LocalTime.now()).build();
    }

    public CommonResponse deleteUser(String username) {
        Integer integer = userMemoryDao.deleteUser(username);
        if (integer == 1)
            return CommonResponse.builder().code(200).msg("success").playload(integer).time(LocalTime.now()).build();
        else return CommonResponse.builder().code(404).msg("not found").playload(integer).time(LocalTime.now()).build();
    }

    public CommonResponse updateUser(User user) {
        Integer integer = userMemoryDao.updateUserPassword(user);
        if (integer == 1)
            return CommonResponse.builder().code(200).msg("success").playload(integer).time(LocalTime.now()).build();
        else return CommonResponse.builder().code(404).msg("not found").playload(integer).time(LocalTime.now()).build();
    }


}
