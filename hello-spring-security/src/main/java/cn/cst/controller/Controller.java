package cn.cst.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @GetMapping("/get/guest")
    public String simpleApi() {
        return "guest";
    }

    @GetMapping("/get/admin")
    public String simpleApi2() {
        return "admin";
    }
}
