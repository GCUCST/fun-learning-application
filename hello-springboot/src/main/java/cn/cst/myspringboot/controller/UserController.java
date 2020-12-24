package cn.cst.myspringboot.controller;

import cn.cst.myspringboot.pojo.User;
import cn.cst.myspringboot.service.UserService;
import cn.cst.myspringboot.util.CommonResponse;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.time.LocalTime;

@Api(value = "用户controller", tags = {"用户操作接口"})
@RestController
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/getUser")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名称", dataType = "String", paramType = "query")})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "succ", response = CommonResponse.class)})
    public CommonResponse getUser(String username) {
        return userService.getUser(username);
    }

    @GetMapping("/getAllUsers")
    @ApiOperation("获取所有用户")
    public CommonResponse getUser() {
        log.info("get all users." + LocalTime.now());
        return userService.getAllUser();
    }

    @GetMapping("/saveUser")
    @ApiOperation("保存用户.")
    public CommonResponse saveUser(String username) {
        log.info("save user." + LocalTime.now());
        return userService.saveUser(User.builder().username(username).password("123456").build());
    }

    @GetMapping("/deleteUser")
    @ApiOperation("删除用户.")
    public CommonResponse delUser(String username) {
        log.info("del user." + LocalTime.now());
        return userService.deleteUser(username);
    }


    @GetMapping("/updateUser")
    @ApiOperation("更新密码用户.")
    public CommonResponse updateUser(User user) {
        log.info("del user." + LocalTime.now());
        return userService.updateUser(user);
    }

    @ApiOperation(value = "获取用户信息", tags = {"获取用户信息——单独"}, notes = "实现笔记") //单独
    @GetMapping("/getUserWithSingle")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "username", value = "用户名称", dataType = "String", paramType = "query")})
    public User getUserInfo(@ApiParam(name = "username", value = "用户名11111", required = true, example = "老王") String username) {
        User user = new User();
        return user;
    }

    @ApiOperation("更改用户信息")
    @PostMapping("/updateUserInfo")
    public User updateUserInfo(@RequestBody @ApiParam(name = "用户对象", value = "传入json格式", required = true) User user) {
        return user;
    }

    @ApiOperation("查询测试")
    @GetMapping("select")
    //@ApiImplicitParam(name="name",value="用户名",dataType="String", paramType = "query")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "用户名", dataType = "string", paramType = "query", example = "chen"),
            @ApiImplicitParam(name = "id", value = "用户id", dataType = "long", paramType = "query")})
    public int select() {
        return 1;
    }


}
