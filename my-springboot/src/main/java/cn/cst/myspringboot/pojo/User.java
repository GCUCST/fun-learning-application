package cn.cst.myspringboot.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "user对象", description = "用户对象user")
public class User {
    @ApiModelProperty(value = "用户名", name = "username", example = "Shaotong Chen")
    public String username;
    @ApiModelProperty(value = "密码", name = "username", example = "123456")
    public String password;
}