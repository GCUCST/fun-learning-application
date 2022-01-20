package cn.cst.controller;

import cn.cst.entities.User;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

  //    @GetMapping("/home")
  //    public String home(){
  //        return "home";
  //    }

  @RequestMapping(value = "/sessionInfo", method = RequestMethod.GET)
  public Map<String, String> addSession(HttpServletRequest request) {
    String sessionId = request.getSession().getId();
    String requestURI = request.getRequestURI();

    Map<String, String> sessionInfoMap = new HashMap<>(2);
    sessionInfoMap.put("sessionId", sessionId);
    sessionInfoMap.put("requestURI", requestURI);
    request.getSession().setAttribute("testKey", "742981086@qq.com");
    request.getSession().setMaxInactiveInterval(10 * 1000);
    String testKey = (String) request.getSession().getAttribute("testKey");
    return sessionInfoMap;
  }

  @GetMapping("/set")
  public boolean setSess(HttpSession session) {
    session.setAttribute("USER_SESSION", User.builder().name("陈少桐").pass("123456").build());
    return true;
  }

  @RequestMapping(value = "/get", method = RequestMethod.GET)
  public User getSession(HttpServletRequest request) {
    HttpSession session = request.getSession();
    return (User) session.getAttribute("USER_SESSION");
  }

  @RequestMapping(value = "/invalid", method = RequestMethod.GET)
  public String invalid(HttpSession session) {
    session.invalidate();
    return "ok";
  }
}
