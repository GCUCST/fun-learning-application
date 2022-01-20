package cn.cst.myspringboot;

import cn.cst.myspringboot.dao.CourseMapper;
import cn.cst.myspringboot.dao.UdictMapper;
import cn.cst.myspringboot.pojo.Course;
import cn.cst.myspringboot.pojo.Udict;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MySpringbootApplicationTests {


    @Autowired
    CourseMapper courseMapper;
    @Autowired
    UdictMapper udictMapper;

    @Test
    void udictInsert() {
        Udict udict = new Udict();
        udict.setUdkey("key1");
        udict.setUdvalue("value1");
        udictMapper.insert(udict);
    }

    @Test
    void udictDel() {
        QueryWrapper wrapper = new QueryWrapper<Udict>();
        wrapper.eq("udid","690871441201037313");
        udictMapper.delete(wrapper);
    }

    @Test
    void contextLoads() {
        for (int i = 0; i < 100; i++) {
            Course course = new Course();
            course.setCname("CST"+i);
            course.setUserId(100L+i);
            course.setCstatus("OK");
            courseMapper.insert(course);
        }
    }

}
