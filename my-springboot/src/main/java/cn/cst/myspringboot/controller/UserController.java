package cn.cst.myspringboot.controller;

import cn.cst.myspringboot.pojo.User;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

@Api(value = "用户controller", tags = {"用户操作接口"})
@RestController
public class UserController {
    @ApiOperation(value = "获取用户信息", tags = {"获取用户信息——单独"}, notes = "实现笔记") //单独
    @GetMapping("/getUser")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名称", dataType = "String", paramType = "query")})
    public User getUserInfo(@ApiParam(name = "username", value = "用户名", required = true, example = "老王") @PathParam("username") String username) {
        User user = new User();
        user.password = "pass";
        user.username = username;
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
