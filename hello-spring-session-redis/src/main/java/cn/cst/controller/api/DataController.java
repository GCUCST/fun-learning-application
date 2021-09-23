package cn.cst.controller.api;

import cn.cst.entities.MyData;
import cn.cst.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;


@RestController
@RequestMapping("/api")
public class DataController {

    @Autowired
    public DataService dataService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/getData", method = RequestMethod.GET)
    public List<MyData> getData (HttpServletRequest request){
       return dataService.testMetaKey("aa","cc");
    }
    @RequestMapping(value = "/cacheEvict", method = RequestMethod.GET)
    public List<MyData> cacheEvict (HttpServletRequest request){
        return dataService.cacheEvict("aa","cc");
    }



}
