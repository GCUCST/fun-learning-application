package cn.cst.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @GetMapping("/findAll")
    public String findAll() {
        System.out.println(1111);
//        SecurityContextHolder.getContext().getAuthentication().getPrincipal();  //通过这个可以获取当前访问的用户
        return "产品列表查询成功";
    }


    @GetMapping(value = "/r1")
    @PreAuthorize("hasAuthority('p1')")//拥有p1权限方可访问此url
    public String r1() {
        //获取用户身份信息
//        UserDTO  userDTO = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return "访问资源1";
    }


}
