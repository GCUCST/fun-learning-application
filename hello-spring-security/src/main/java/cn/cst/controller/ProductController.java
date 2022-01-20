package cn.cst.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class ProductController {

  @RequestMapping("findAll")
  public String findAll() {
    return "success";
  }

  @RequestMapping("any")
  @Secured({"ROLE_ADMIN", "ROLE_GUEST"})
  public String any() {
    return "any";
  }

  @RequestMapping("admin")
  @Secured("ROLE_ADMIN")
  public String admin() {
    return "admin";
  }

  @RequestMapping("guest")
  @Secured("ROLE_GUEST")
  public String guest() {
    return "guest";
  }
}
