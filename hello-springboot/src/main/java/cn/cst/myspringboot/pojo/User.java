package cn.cst.myspringboot.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "user对象", description = "用户对象user")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @ApiModelProperty(value = "用户名", name = "username", example = "Shaotong Chen")
    private String username;
    @ApiModelProperty(value = "密码", name = "username", example = "123456")
    private String password;
}